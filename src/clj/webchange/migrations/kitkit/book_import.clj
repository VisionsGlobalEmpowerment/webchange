(ns webchange.migrations.kitkit.book-import
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.course.core :as core]
            [camel-snake-kebab.core :refer [->kebab-case]]
            [webchange.migrations.kitkit.book-horizontal-page :as horizontal-template]
            [webchange.migrations.kitkit.book-vertical :as vertical-template]
            [webchange.migrations.kitkit.book-vertical-page :as book-vertical-page]
            [webchange.migrations.kitkit.book-horizontal-page :as book-horizontal-page]
  ))

(def course-name "kitkit-book")

(defn read-title [book row]
  (-> book
      (assoc :title (get row 1))
      (assoc :audio (get row 2))
      (assoc :image (get row 3))
      (assoc :orientation (get row 4))))

(defn read-page [book row]
  (-> book
      (assoc :pages (conj (:pages book) {
                                         :page (get row 1)
                                         :image (get row 2)
                                         :paragraphs []
                                         }))
      (assoc :pages-last (inc (:pages-last book)))
      (assoc :paragraphs-last -1)))

(defn read-paragraph [book row]
  (-> book
      (assoc-in [:pages (:pages-last book) :paragraphs]
                (conj (get-in book [:pages (:pages-last book) :paragraphs]) {
                                                                             :sentences []
                                                                             }))
      (assoc :paragraphs-last (inc (:paragraphs-last book)))
      (assoc :sentences-last -1)))

(defn read-sentence [book row]
  (-> book
      (assoc-in [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences]
                (conj (get-in book [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences])
                      {
                       :audio (get row 1)
                       :start (edn/read-string (get row 2))
                       :words []}))
      (assoc :sentences-last (inc (:sentences-last book)))
      (assoc :word-last -1)))

(defn read-word [book row]
  (-> book
      (assoc-in [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences (:sentences-last book) :words]
                (conj (get-in book [:pages (:pages-last book) :paragraphs (:paragraphs-last book) :sentences (:sentences-last book) :words])
                      {:start (edn/read-string (get row 1))
                       :end (edn/read-string (get row 2))
                       :word (get row 3)
                       :audio (get row 4)
                       :length (edn/read-string (get row 5))}))))

(defn read-book-info
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
                      book)
                    ) {:pages [] :paragraphs-last -1 :sentences-last -1 :pages-last -1} rows)))))

(defn prepare-book-info-by-sentence [book-info]
  {:pages (flatten (map (fn [page]
                 (map
                   (fn [p]
                     (map (fn [s]
                            {:text (clojure.string/join " " (map (fn [w] (:word w)) (:words s)))
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
                (:pages book-info)))
   :title (:title book-info)
   :audio (:audio book-info)
   :image (:image book-info)
   :orientation (:orientation book-info)
   })

(defn prepare-book-info [book-info]
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
   :orientation (:orientation book-info)
   }
  )

(defn endWithFun
  [arg1 arg2]
  (if (< (count arg1) (count arg2)) false
  (= (subs arg1 (- (count arg1) (count arg2)) (count arg1)) arg2)))

(defn prepare-filename
  [file extension]
  (if (endWithFun file extension) file (str file extension))
  )

(defn is-file-exists-with-extension
  [dir file extension]
  (let [name (prepare-filename file extension)]
    (.exists (io/file (str dir name)))))

(defn find-extension
  [dir file]
  (cond
    (is-file-exists-with-extension dir file ".png") ".png"
    (is-file-exists-with-extension dir file ".jpg") ".jpg"
    )
  )

(defn copy-file
  [source target]
  (with-open [in (io/input-stream source)
              out (io/output-stream target)]
    (io/copy in out)))

(defn import-assets-book-info
  [book-info source-dir target-dir public-dir]
  (clojure.java.io/make-parents (str target-dir "1.txt"))
  (doall (map (fn [page]
                (doall (map (fn [paragraph]
                       (doall (map (fn [sentence]
                             (copy-file (str source-dir "page/" (:audio sentence))
                                        (str target-dir (:audio sentence)))
                        ) (:sentences paragraph)))
                ) (:paragraphs page)))
          ) (:pages book-info))
         )
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
                                              (doall (map (fn [paragraph]
                                                            (assoc paragraph :sentences
                                                                             (doall (map (fn [sentence]
                                                                                           (assoc sentence :audio (str public-dir (:audio sentence)))
                                                                                           ) (:sentences paragraph))))
                                                            ) (:paragraphs page))))
                                       (assoc :image (str public-dir (get image-map (:image page)))))
                                   ) (:pages book-info)))
        (assoc :image ((fn [] (let [file (:image book-info)
                               extension (find-extension (str source-dir "page/") file)
                               file (prepare-filename file extension)
                               ]
                           (copy-file (str source-dir "page/" file)
                                      (str target-dir file))
                           (str public-dir file)
                           )))
               )
        (assoc :audio ((fn []
            (copy-file (str source-dir "page/" (:audio book-info))
                       (str target-dir (:audio book-info)))
                       (str public-dir (:audio book-info))
            )))
        )))


(defn import-book-info
  [book-info owner-id name]
  (let [data {:name (->kebab-case name)
              :lang "En"
              :skills [1]}
        [course] (db/find-courses-by-name {:name course-name})
        course (if course course (second (core/create-course (assoc data :name course-name)  owner-id)))
        metadata (if (= (:orientation book-info) "portrait") book-vertical-page/m book-horizontal-page/m)
        activity (if (= (:orientation book-info) "portrait")  (book-vertical-page/f book-info) (book-horizontal-page/f book-info))
        scene (if (core/get-scene-data (-> course :slug) (:name data))
                (second (core/save-scene! (-> course :slug) (:name data) activity owner-id))
                (second (core/create-scene! activity metadata  (-> course :slug) (:name data) (:skills data) owner-id)))
        ]
    (println (str "/s/" (:course-slug scene) "/" (:name scene)))))
