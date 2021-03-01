(ns webchange.migrations.onebillion.book-import
  (:require [clojure.xml :as xml]
            [clojure.java.io :as io]
            [webchange.migrations.onebillion.book-horizontal :as horizontal-template]
            [webchange.course.core :as core]
            [camel-snake-kebab.core :refer [->kebab-case]]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.migrations.kitkit.book-import :as kitkit]
            ))

(def course-name "onebillion-book")
(defn get-image-dir
  [dir]
  (if (.exists (io/file (str dir  "img/shared_3/"))) (str dir "img/shared_3/") (str dir "img/shared_4/")))

(defn get-image-path
  [dir name]
  (let [img-dir (get-image-dir dir)]
  (str img-dir name (kitkit/find-extension img-dir name))))

(defn get-audio-path
  [name ext]
  (str "local/en_GB/" name ext))

(defn parse-title
  [book]
  (let [title-page (->> book
             :pages
             first
             )
        book (assoc book :pages (drop 1 (:pages book)))
        ]
     (-> book
        (assoc :title (:text title-page))
        (assoc :image (:img title-page))
        (assoc :orientation "landscape")
        (assoc :audio (:audio title-page))
        (assoc :anim (:anim title-page))
        )
    )
  )

(defn get-words-info
  [dir page para]
  (try
  (let [
        full-path (str dir (get-audio-path (str "p" page "_" para) ".etpa"))
        xml (-> full-path io/file xml/parse)
        timings (->> xml
                     :content
                     (filter #(= (:tag %) :timings))
                     first
                     :content)]
    (doall (mapv (fn [timing] {
            :start (Float/parseFloat (get-in timing [:attrs :start]))
            :end (Float/parseFloat (get-in timing [:attrs :end]))
            :word (get-in timing [:attrs :text])
            :id (Integer/parseInt (get-in timing [:attrs :id]))
           }) timings))
    )
  (catch java.io.FileNotFoundException e (println (str "File not found " (get-audio-path (str "p" page "_" para) ".etpa"))))
  )
  )

(defn parse-pages
  [book xml dir]
    (assoc book :pages (doall (map
              (fn [page] {
                 :page (Integer/parseInt (get-in page [:attrs :pageno]))
                 :image (str "p" (get-in page [:attrs :pageno]))
                 :paragraphs [{:sentences
                              (doall (vec (map-indexed (fn [idx para] {
                                          :audio (str "p" (get-in page [:attrs :pageno]) "_" (+ idx 1)),
                                          :text  (get-in para [:content 0])
                                          :words (get-words-info dir (get-in page [:attrs :pageno]) (+ idx 1))
                                       })
                               (get-in page [:content]))))
                               }]
                 })
            (get-in xml [:content])))))

(defn prepare-assets
  [book dir public-dir target-dir]
  (as-> book result
    (assoc result :pages (mapv
            (fn [page]
              (let
                [img (get-image-path dir (:image page))
                 img-name (.getName (io/file img))
                 target-path (str target-dir img-name)
                 public-path (str public-dir img-name)
                 ]
                (clojure.java.io/make-parents target-path)
                (kitkit/copy-file img target-path)
                (assoc page :image public-path)
                )
              ) (:pages result)))
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
                                                                   public-path (str public-dir audio-name)
                                                                   ]
                                                               (clojure.java.io/make-parents target-path)
                                                               (kitkit/copy-file audio target-path)
                                                               (assoc sentence :audio public-path)
                                                               )
                                                             )
                                                           (:sentences paragraph)
                                                         )
                                        ))
                                      (:paragraphs page)
                                    )
                        )
                        )
                      (:pages result)))
      )
  )

(defn prepare-book-info-by-sentence [book-info]
  {:pages (vec (flatten (map (fn [page]
                          (map
                            (fn [p]
                              (map (fn [s]
                                     {:text (:text s)                ; (clojure.string/join " " (map (fn [w] (:word w)) (:words s)))
                                      :audio (:audio s)
                                      :img (:image page)
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

                            (:paragraphs page)))
                        (:pages book-info))))})

(defn read-book-info
  [dir public-dir target-dir]
  (let [file (str dir "local/en_GB/book.xml")
        xml (try (->
              file
              io/file
              slurp
              .trim
              (.replaceAll "[^\\x20-\\x7e]", "")
              .getBytes
              java.io.ByteArrayInputStream.
              xml/parse)
                 (catch org.xml.sax.SAXParseException e
                        (println "Trying to fix & symbol")
                   (->
                          file
                          io/file
                          slurp
                          .trim
                          (.replaceAll "[^\\x20-\\x7e]", "")
                          (.replaceAll "&", "")
                          .getBytes
                          java.io.ByteArrayInputStream.
                          xml/parse)

                 ))

        ]
       (-> {}
          (parse-pages xml dir)
          (prepare-assets dir public-dir target-dir)
          (prepare-book-info-by-sentence)
          (parse-title)
        )
    ))

(defn import-book-info-by-sentence
  [book-info owner-id name]
  (let [data {:name (->kebab-case name)
              :lang "En"
              :skills [1]}
        [course] (db/find-courses-by-name {:name course-name})
        course (if course course (second (core/create-course (assoc data :name course-name)  owner-id)))
        metadata horizontal-template/m
        activity (horizontal-template/f book-info)
        course-slug (-> course :slug)
        scene-slug (:name data)]
    (if (core/get-scene-data course-slug scene-slug)
      (second (core/save-scene! course-slug scene-slug activity owner-id))
      (second (core/create-scene! activity metadata course-slug scene-slug (:skills data) owner-id)))
    (println (str "/s/" course-slug "/" scene-slug))))
