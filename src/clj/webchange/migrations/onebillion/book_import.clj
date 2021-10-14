(ns webchange.migrations.onebillion.book-import
  (:require [clojure.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [webchange.course.core :as core]
            [webchange.db.core :as db]
            [webchange.migrations.kitkit.book-import :as kitkit]
            [webchange.templates.core :as t]
            [webchange.common.files :as f]))

(def template-id 24)

(defn- get-image-dir
  [dir]
  (if (.exists (io/file (str dir  "img/shared_3/"))) (str dir "img/shared_3/") (str dir "img/shared_4/")))

(defn- get-image-path
  [dir name]
  (let [img-dir (get-image-dir dir)]
    (str img-dir name (kitkit/find-extension img-dir name))))

(defn- get-audio-path
  [name ext]
  (str "local/en_GB/" name ext))

(defn- parse-title
  [book]
  (let [title-page (->> book :pages first)
        book (assoc book :pages (drop 1 (:pages book)))]
    (-> book
        (assoc :title (:text title-page))
        (assoc :image (:img title-page))
        (assoc :orientation "landscape")
        (assoc :audio (:audio title-page))
        (assoc :anim (:anim title-page)))))

(defn- get-words-info
  [dir page para]
  (try
    (let [full-path (str dir (get-audio-path (str "p" page "_" para) ".etpa"))
          xml (-> full-path io/file xml/parse)
          timings (->> xml
                       :content
                       (filter #(= (:tag %) :timings))
                       first
                       :content)]
      (mapv (fn [timing]
              {:start (Float/parseFloat (get-in timing [:attrs :start]))
               :end (Float/parseFloat (get-in timing [:attrs :end]))
               :word (get-in timing [:attrs :text])
               :id (Integer/parseInt (get-in timing [:attrs :id]))})
            timings))
    (catch java.io.FileNotFoundException e (println (str "File not found " (get-audio-path (str "p" page "_" para) ".etpa"))))))

(defn- parse-pages
  [book xml dir]
  (assoc book :pages (map
                       (fn [page] {:page (Integer/parseInt (get-in page [:attrs :pageno]))
                                   :image (str "p" (get-in page [:attrs :pageno]))
                                   :paragraphs [{:sentences
                                                 (->> (get-in page [:content])
                                                      (map-indexed (fn [idx para]
                                                                     {:audio (str "p" (get-in page [:attrs :pageno]) "_" (+ idx 1))
                                                                      :text  (get-in para [:content 0])
                                                                      :words (get-words-info dir (get-in page [:attrs :pageno]) (+ idx 1))}))
                                                      (vec))}]})
                       (get-in xml [:content]))))

(defn- prepare-assets
  [book dir public-dir target-dir]
  (as-> book result
    (assoc result :pages (mapv
                           (fn [page]
                             (let [img (get-image-path dir (:image page))
                                   img-name (.getName (io/file img))
                                   target-path (str target-dir img-name)
                                   public-path (str public-dir img-name)]
                               (clojure.java.io/make-parents target-path)
                               (kitkit/copy-file img target-path)
                               (assoc page :image public-path)))
                           (:pages result)))
    (assoc result :pages (mapv
                           (fn [page]
                             (assoc page :paragraphs
                                    (mapv
                                      (fn [paragraph]
                                        (assoc paragraph :sentences
                                               (map
                                                 (fn [sentence]
                                                   (let [audio (str dir (get-audio-path (:audio sentence) ".m4a"))
                                                         audio-name (.getName (io/file audio))
                                                         target-path (str target-dir audio-name)
                                                         public-path (str public-dir audio-name)]
                                                     (clojure.java.io/make-parents target-path)
                                                     (kitkit/copy-file audio target-path)
                                                     (assoc sentence :audio public-path)))
                                                 (:sentences paragraph))))
                                      (:paragraphs page))))
                           (:pages result)))))

(defn- prepare-book-info-by-sentence
  [book-info]
  {:pages (vec (flatten (map (fn [page]
                               (map
                                 (fn [p]
                                   (map (fn [s]
                                          {:text (:text s)                ; (clojure.string/join " " (map (fn [w] (:word w)) (:words s)))
                                           :audio (:audio s)
                                           :img (:image page)
                                           :anim (flatten
                                                   (map-indexed (fn [idx w]
                                                                  {:at (:start w),
                                                                   :end (:end w),
                                                                   :chunk idx,
                                                                   :start (:start w),
                                                                   :duration (- (:end w) (:start w))})
                                                                (:words s)))})
                                        (:sentences p)))
                                 (:paragraphs page)))
                             (:pages book-info))))})

(defn- update-last-page-dialog!
  [activity {:keys [audio anim text]}]
  (let [last-page-id (get-in activity [:metadata :next-page-id] 0)
        
        text-name (str "page-text-" last-page-id)
        dialog-name (keyword (str "page-" last-page-id "-action"))

        anim (if anim anim [])
        end (if (seq anim) (->> anim (map :end) (reduce max)) 0)
        dialog [{:type        "sequence-data"
                 :data        [{:type "empty" :duration 0}
                               {:data        anim,
                                :type        "text-animation",
                                :fill        0x00B2FF
                                :phrase-text "Text animation"
                                :audio       audio,
                                :target      text-name
                                :animation   "color"
                                :start 0
                                :end end
                                :duration end}]}]
        asset {:url audio :size 1 :type "audio"}]
    (-> activity
        (assoc-in [:actions dialog-name :data] dialog)
        (update :assets concat [asset]))))

(defn- update-title-dialog!
  [activity {:keys [audio anim]}]
  (let [text-name (str "page-cover-title-text")
        dialog-name (keyword (str "page-cover-action"))
        
        anim (if anim anim [])
        end (->> anim (map :end) (reduce max))
        dialog [{:type        "sequence-data"
                 :data        [{:type "empty" :duration 0}
                               {:data        anim,
                                :type        "text-animation",
                                :fill        0x00B2FF
                                :phrase-text "Text animation"
                                :audio       audio,
                                :target      text-name
                                :animation   "color"
                                :start 0
                                :end end
                                :duration end}]}]
        asset {:url audio :size 1 :type "audio"}]
    (-> activity
        (assoc-in [:actions dialog-name :data] dialog)
        (update :assets concat [asset]))))

(defn- read-book-info
  [dir public-dir target-dir]
  (let [file (str dir "local/en_GB/book.xml")
        xml (try (-> file
                     io/file
                     slurp
                     .trim
                     (.replaceAll "[^\\x20-\\x7e]", "")
                     .getBytes
                     java.io.ByteArrayInputStream.
                     xml/parse)
                 (catch org.xml.sax.SAXParseException e
                   (println "Trying to fix & symbol")
                   (-> file
                       io/file
                       slurp
                       .trim
                       (.replaceAll "[^\\x20-\\x7e]", "")
                       (.replaceAll "&", "")
                       .getBytes
                       java.io.ByteArrayInputStream.
                       xml/parse)))]
    (-> {}
        (parse-pages xml dir)
        (prepare-assets dir public-dir target-dir)
        (prepare-book-info-by-sentence)
        (parse-title))))

(defn- prepare-text
  [text]
  (str/replace text #"/" ""))

(defn- save-book!
  [activity metadata owner-id book-name]
  (let [data {:name book-name
              :lang "english"
              :skills [1]}
        [course] (db/find-courses-by-name {:name book-name})
        course (if course course (second (core/create-course (assoc data :name book-name) owner-id)))
        course-slug (-> course :slug)
        scene-slug (core/slug book-name)]
    (if (core/get-scene-data course-slug scene-slug)
      (second (core/save-scene! course-slug scene-slug activity owner-id))
      (second (core/create-scene! activity metadata course-slug scene-slug (:skills data) owner-id)))
    (str "/s/" course-slug "/" scene-slug)))

(defn import-book
  [owner-id source-dir public-dir target-dir]
  (let [book-info (read-book-info source-dir public-dir target-dir)
        metadata (t/get-template-metadata-by-id template-id)
        created-activity (-> (t/activity-from-template {:template-id template-id
                                                        :cover-layout "title-bottom"
                                                        :cover-title (prepare-text (:title book-info))
                                                        :cover-image {:src (:image book-info)}
                                                        :authors ["onebillion"]
                                                        :illustrators ["onebillion"]})
                             (update-title-dialog! book-info))
        updated-activity (reduce (fn [activity page]
                                   (-> activity 
                                       (t/update-activity-from-template {:action "add-page"
                                                                         :data {:type "page"
                                                                                :page-layout "text-small-at-bottom"
                                                                                :text (prepare-text (:text page))
                                                                                :image {:src (:img page)}}})
                                       (update-last-page-dialog! page)))
                                 created-activity
                                 (:pages book-info))
        book-name (prepare-text (:title book-info))]
    (save-book! updated-activity metadata owner-id book-name)))

(comment
  (let [owner-id 1
        book-name "xr-allthethings"
        import-dir "/Users/ikhaldeev/books/onebillion-books/"

        source-dir  (str import-dir book-name "/")
        public-dir  (str "/raw/book-onebillion/" book-name "/")
        target-dir  (f/relative->absolute-path public-dir)]
    (import-book owner-id source-dir public-dir target-dir))

  (let [owner-id 1
        book-name "xr-allthethings"
        import-dir "/Users/ikhaldeev/books/onebillion-books/"

        source-dir  (str import-dir book-name "/")
        public-dir  (str "/raw/book-onebillion/" book-name "/")
        target-dir  (f/relative->absolute-path public-dir)
        
        book-info (read-book-info source-dir public-dir target-dir)
        metadata (t/get-template-metadata-by-id template-id)
        created-activity (t/activity-from-template {:template-id template-id
                                                    :cover-layout "title-bottom"
                                                    :cover-title (prepare-text (:title book-info))
                                                    :cover-image {:src (:image book-info)}
                                                    :authors ["onebillion"]
                                                    :illustrators ["onebillion"]})
        updated-activity (reduce (fn [activity page]
                                   (-> activity 
                                       (t/update-activity-from-template {:action "add-page"
                                                                         :data {:type "page"
                                                                                :page-layout "text-small-at-bottom"
                                                                                :text (prepare-text (:text page))
                                                                                :image {:src (:img page)}}})
                                       (update-last-page-dialog! page)))
                                 created-activity
                                 (:pages book-info))
        book-name (prepare-text (:title book-info))]
    updated-activity)
  )
