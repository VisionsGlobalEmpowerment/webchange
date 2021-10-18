(ns webchange.migrations.kitkit.book-import
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [webchange.db.core :as db]
            [webchange.course.core :as core]
            [webchange.assets.core :as assets]
            [webchange.templates.core :as t]
            [webchange.common.files :as f]))

(def template-id 24)

(defn- read-title [book row]
  (-> book
      (assoc :title (get row 1))
      (assoc :audio (get row 2))
      (assoc :image (get row 3))
      (assoc :orientation (get row 4))))

(defn- read-page [book row]
  (-> book
      (update :pages conj {:page (get row 1)
                           :image (get row 2)
                           :paragraphs []})
      (update :pages-last inc)
      (assoc :paragraphs-last -1)))

(defn- read-paragraph [book row]
  (-> book
      (update-in [:pages (:pages-last book) :paragraphs] conj {:sentences []})
      (update :paragraphs-last inc)
      (assoc :sentences-last -1)))

(defn- read-sentence [book row]
  (-> book
      (update-in [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences]
                 conj
                 {:audio (get row 1)
                  :start (edn/read-string (get row 2))
                  :words []})
      (update :sentences-last inc)
      (assoc :word-last -1)))

(defn- read-word [book row]
  (-> book
      (update-in [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences (:sentences-last book) :words]
                 conj
                 {:start (edn/read-string (get row 1))
                  :end (edn/read-string (get row 2))
                  :word (get row 3)
                  :audio (get row 4)
                  :length (edn/read-string (get row 5))})))

(defn- read-book-info
  [dir]
  (let [file (str dir "/bookinfo.csv")]
    (with-open [reader (io/reader file)]
      (let [rows (csv/read-csv reader)]
        (reduce (fn [book row]
                  (case (get row 0)
                    "title" (read-title book row)
                    "page" (read-page book row)
                    "paragraph" (read-paragraph book row)
                    "sentence" (read-sentence book row)
                    "word" (read-word book row)
                    book))
                {:pages [] :paragraphs-last -1 :sentences-last -1 :pages-last -1}
                rows)))))

(defn- prepare-book-info [book-info]
  {:pages (flatten (map (fn [page]
                          {:img (:image page)
                           :text
                           (clojure.string/join "\n " (map (fn [p]
                                                             (clojure.string/join " " (flatten  (map (fn [s]
                                                                                                       (map (fn [w] (:word w)) (:words s))
                                                                                                       )  (:sentences p)))))
                                                           (:paragraphs page)))
                           :audios (:audios (reduce (fn [parsed audio]
                                                      {
                                                       :audios (conj (:audios parsed) {:audio (:audio audio)
                                                                                       :anim (map-indexed (fn [idx anim] (assoc anim :chunk (+ idx (:start parsed))) ) (:anim audio))
                                                                                       })
                                                       :start (+ (:start parsed) (count (:anim audio)))
                                                       }
                                                      ) {:start 0 :audios []} (flatten (map
                                                                                         (fn [p]
                                                                                           (map (fn [s]
                                                                                                  {
                                                                                                   :audio (:audio s)
                                                                                                   :text (clojure.string/join " " (flatten  (map (fn [w] (:word w)) (:words s))))
                                                                                                   :anim (flatten
                                                                                                           (map-indexed (fn [idx w] {
                                                                                                                                     :at (:start w),
                                                                                                                                     :end (:end w),
                                                                                                                                     :chunk idx,
                                                                                                                                     :start (:start w),
                                                                                                                                     :duration (- (:end w) (:start w))
                                                                                                                                     }) (:words s))
                                                                                                           )}
                                                                                                  ) (:sentences p))
                                                                                           )
                                                                                         (:paragraphs page)))))
                           }
                          )
                        (:pages book-info)))
   :title (:title book-info)
   :audio (:audio book-info)
   :image (:image book-info)
   :orientation (:orientation book-info)})

(defn- endWithFun
  [arg1 arg2]
  (if (< (count arg1) (count arg2)) false
      (= (subs arg1 (- (count arg1) (count arg2)) (count arg1)) arg2)))

(defn- prepare-filename
  [file extension]
  (if (endWithFun file extension) file (str file extension)))

(defn- is-file-exists-with-extension
  [dir file extension]
  (let [name (prepare-filename file extension)]
    (.exists (io/file (str dir name)))))

(defn find-extension
  [dir file]
  (cond
    (is-file-exists-with-extension dir file ".png") ".png"
    (is-file-exists-with-extension dir file ".jpg") ".jpg"))

(defn copy-file
  [source target]
  (with-open [in (io/input-stream source)
              out (io/output-stream target)]
    (io/copy in out)
    (assets/store-asset-hash! target)))

(defn- import-assets-book-info
  [book-info source-dir target-dir public-dir]
  (clojure.java.io/make-parents (str target-dir "1.txt"))
  (let [sentences (->> book-info
                       (map :pages)
                       (mapcat :paragraphs)
                       (mapcat :sentences))]
    (doall
      (for [sentence sentences]
        (copy-file (str source-dir "page/" (:audio sentence))
                   (str target-dir (:audio sentence))))))
  

  (let [image-map (into {} (map (fn [page]
                                  (let [file (:image page)
                                        extension (find-extension (str source-dir "page/") file)
                                        file (prepare-filename file extension)]
                                    (copy-file (str source-dir "page/" file)
                                               (str target-dir file))
                                    [(:image page) file]
                                    )) (:pages book-info)))]
    (-> book-info
        (assoc :pages (map (fn [page]
                             (-> page
                                 (assoc :paragraphs
                                        (map (fn [paragraph]
                                               (assoc paragraph :sentences
                                                      (map (fn [sentence]
                                                             (assoc sentence :audio (str public-dir (:audio sentence))))
                                                           (:sentences paragraph))))
                                             (:paragraphs page)))
                                 (assoc :image (str public-dir (get image-map (:image page)))))
                             ) (:pages book-info)))
        (assoc :image (let [file (:image book-info)
                            extension (find-extension (str source-dir "page/") file)
                            file (prepare-filename file extension)]
                        (copy-file (str source-dir "page/" file)
                                   (str target-dir file))
                        (str public-dir file)))
        (assoc :audio (do
                        (copy-file (str source-dir "page/" (:audio book-info))
                                   (str target-dir (:audio book-info)))
                        (str public-dir (:audio book-info)))))))

(defn- prepare-text
  [text]
  (str/replace text #"/" ""))

(defn- update-last-page-dialog!
  [activity {:keys [audios]}]
  (let [last-page-id (get-in activity [:metadata :next-page-id] 0)
        
        text-name (str "page-text-" last-page-id)
        dialog-name (keyword (str "page-" last-page-id "-action"))

        dialogs (mapv (fn [{:keys [text audio anim]}]
                        (let [anim (if anim anim [])
                              end (if (seq anim) (->> anim (map :end) (reduce max)) 0)]
                          {:type "sequence-data"
                           :data [{:type "empty" :duration 0}
                                  {:data        anim,
                                   :type        "text-animation",
                                   :fill        0x00B2FF
                                   :phrase-text "Text animation"
                                   :audio       audio,
                                   :target      text-name
                                   :animation   "color"
                                   :start 0
                                   :end end
                                   :duration end}]}))
                      audios)
        asset (mapv (fn [{:keys [audio]}]
                      {:url audio :size 1 :type "audio"})
                    audios)]
    (-> activity
        (assoc-in [:actions dialog-name :data] dialogs)
        (update :assets concat asset))))

(defn- update-title-dialog!
  [activity {:keys [audio anim]}]
  (let [text-name (str "page-cover-title-text")
        dialog-name (keyword (str "page-cover-action"))
        
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

(defn- save-book!
  [activity metadata owner-id book-name image-src]
  (let [[course] (db/find-courses-by-name {:name book-name})
        lang "english"
        course-data (-> {:name book-name
                         :type "book"
                         :lang lang
                         :skills [1]}
                        (assoc :name book-name)
                        (assoc :image-src
                               (assets/make-thumbnail image-src :course)))
        course (if course
                 course
                 (second (core/create-course course-data owner-id)))
        course-slug (or (-> course :slug) (core/course-slug book-name lang))
        scene-slug (core/slug book-name)]
    (core/save-course-info! (:id course) (assoc course-data :slug course-slug))
    (if (core/get-scene-data course-slug scene-slug)
      (second (core/save-scene! course-slug scene-slug activity owner-id))
      (second (core/create-scene! activity metadata course-slug scene-slug (:skills course-data) owner-id)))
    (str "/s/" course-slug "/" scene-slug)))

(defn import-book
  [owner-id source-dir public-dir target-dir]
  (let [book-info (-> source-dir
                      (read-book-info)
                      (import-assets-book-info source-dir target-dir public-dir)
                      (prepare-book-info))
        metadata (t/get-template-metadata-by-id template-id)
        created-activity (-> (t/activity-from-template {:template-id template-id
                                                        :cover-layout "title-bottom"
                                                        :cover-title (prepare-text (:title book-info))
                                                        :cover-image {:src (:image book-info)}
                                                        :authors ["kitkit"]
                                                        :illustrators ["kitkit"]})
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
        book-name (prepare-text (:title book-info))
        image-src (:image book-info)]
    (save-book! updated-activity metadata owner-id book-name image-src)))

(comment
  (let [owner-id 1
        book-name "en_101"
        import-dir "/Users/ikhaldeev/books/kitkit-books/"

        source-dir  (str import-dir book-name "/")
        public-dir  (str "/raw/book/" book-name "/")
        target-dir  (f/relative->absolute-path public-dir)]
    (import-book owner-id source-dir public-dir target-dir))
  
  (let [owner-id 1
        book-name "en_101"
        import-dir "/Users/ikhaldeev/books/kitkit-books/"

        source-dir  (str import-dir book-name "/")
        public-dir  (str "/raw/book/" book-name "/")
        target-dir  (f/relative->absolute-path public-dir)

        book-info (-> source-dir
                      (read-book-info)
                      (import-assets-book-info source-dir target-dir public-dir)
                      (prepare-book-info))
        metadata (t/get-template-metadata-by-id template-id)
        created-activity (-> (t/activity-from-template {:template-id template-id
                                                        :cover-layout "title-bottom"
                                                        :cover-title (prepare-text (:title book-info))
                                                        :cover-image {:src (:image book-info)}
                                                        :authors ["kitkit"]
                                                        :illustrators ["kitkit"]})
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
    updated-activity)
  )
