(ns webchange.migrations.kitkit.book-vertical
  (:require
    [webchange.templates.core :as core]
    [clojure.string :refer [index-of]]))

(def m {:id          5
        :name        "book"
        :description "Simple book"
        :options     {:title {:label "Title"
                              :type  "string"}
                      :pages {:label "Pages"
                              :type  "pages"
                              :max   10}}})

(def t {:assets        [{:url "/raw/clipart/book/books_02_single-background.png", :size 10, :type "image"}
                        {:url "/raw/img/ui/back_button_01.png" :size 1 :type "image"}]
        :objects       {:background      {:type "background"
                                          :src  "/raw/clipart/book/books_02_single-background.png"}
                        :next-page-arrow {:type    "image",
                                          :x       1770
                                          :y       500
                                          :width   97,
                                          :height  99,
                                          :actions {:click {:id "next-page", :on "click", :type "action"}},
                                          :scale-x -1,
                                          :src     "/raw/img/ui/back_button_01.png"}
                        :group-0         {:type "group" :children ["title" "title-image"] :x 370 :y 130}
                        :title           {:type "text" :width 1180 :align "center" :vertical-align "top" :font-family "Lexend Deca" :font-size 120
                                          :text nil :chunks nil :x 0 :y 640}
                        :title-image {:type "image" :src "" :x 590 :y 320 :origin {:type "center-center"} :max-width 1180 :max-height 640}
                        }
        :scene-objects [["background" "next-page-arrow" "group-0"]],
        :actions       {:dialog-1-title {:type               "sequence-data",
                                         :editor-type        "dialog",
                                         :concept-var        "current-word",
                                         :data               [{:type "sequence-data"
                                                               :data [{:type "empty" :duration 0}
                                                                      {:data        [],
                                                                       :type        "text-animation",
                                                                       :audio       nil,
                                                                       :target      "title",
                                                                       :animation   "bounce",
                                                                       :phrase-text nil}]}],
                                         :phrase             "dialog-title",
                                         :phrase-description "Title",
                                         :dialog-track       "1 Title"}

                        :next-page      {:type "sequence-data"
                                         :data [{:type     "set-attribute" :attr-name "visible", :attr-value false
                                                 :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                                {:type "counter", :counter-id "current-page", :counter-action "increase"}
                                                {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                 :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                                {:type     "action"
                                                 :from-var [{:template "dialog-%-page", :var-name "current-page", :action-property "id"}]}]}
                        :intro
                                        {:type "sequence-data",
                                         :data [{:type "start-activity"}
                                                {:type "set-variable", :var-name "current-page", :var-value 0}
                                                {:type "set-variable", :var-name "max-page", :var-value 4}
                                                {:type "empty" :duration 1000}
                                                {:type "action" :id "dialog-1-title"}]}}
        :triggers
                       {:start {:on "start", :action "intro"}}
        :metadata      {:stages [{:name    "Title"
                                  :objects ["background" "next-page-arrow" "group-0"]}]}})

(defn- group-name
  [idx]
  (str "group-" idx))

(defn- part->chunk
  [phrase part start]
  (let [start (index-of phrase part start)
        end (+ start (count part))]
    {:start start :end end}))

(defn- parts->chunks
  [phrase parts]
  (loop [idx 0
         tail parts
         chunks []]
    (if (empty? tail)
      chunks
      (let [chunk (part->chunk phrase (first tail) idx)]
        (recur (:end chunk)
               (rest tail)
               (conj chunks chunk))))))

(defn- text->chunks
  [text]
  (parts->chunks text (clojure.string/split text #" ")))

(defn- create-page
  [idx {:keys [img text]}]
  (let [group-name (group-name idx)
        image-name (str "image-" idx)
        text-name (str "text-" idx)]
    {(keyword group-name) {:type "group" :children [image-name text-name] :visible false :x 60 :y 130}
     (keyword image-name) {:type "image" :src img :x 590 :y 350 :origin {:type "center-center"} :max-width 500 :max-height 800}
     (keyword text-name)  {:type "text" :x 950 :y 0 :width 550 :vertical-align "top" :font-family "Lexend Deca" :font-size 80
                           :text text :chunks (text->chunks text)}}))

(defn- create-page-dialog
  [idx {:keys [text audio anim]}]
  (let [anim (if anim anim [])
        dialog {:type               "sequence-data",
                :concept-var        "current-word",
                :data               [{:type        "sequence-data"
                                      :editor-type "dialog",
                                      :data        [{:type "empty" :duration 0}
                                                    {:data        anim,
                                                     :type        "text-animation",
                                                     :audio       audio,
                                                     :target      (str "text-" idx),
                                                     :animation   "bounce",
                                                     :phrase-text text}]}],
                :phrase             (str "dialog-page-" idx),
                :phrase-description (str "Page " idx),
                :dialog-track       "2 Pages"}
        name (str "dialog-" idx "-page")]
    {(keyword name) dialog}))

(defn- create-page-stage
  [idx]
  (let [group-name (group-name idx)]
    {:name    (str "Page " idx)
     :objects ["background" "next-page-arrow" group-name]}))

(defn- create-asset
  [{img :img}]
  {:url img :size 1 :type "image"})

(defn- create-audio
  [{audio :audio}]
  {:url audio :size 1 :type "audio"})

(defn- add-pages
  [t pages]
  (let [page-container (->> pages
                            (map-indexed (fn [idx p] (create-page (inc idx) p)))
                            (reduce merge))
        dialog-container (->> pages
                              (map-indexed (fn [idx p] (create-page-dialog (inc idx) p)))
                              (reduce merge))
        assets (map create-asset pages)
        audios (map create-audio (filter (fn [{audio :audio}] audio) pages))
        group-names (->> (count pages) (range) (map inc) (map group-name) (into []))
        stages (->> (count pages) (range) (map inc) (map create-page-stage) (into []))]
    (-> t
        (update :assets concat (concat assets audios))
        (update :assets vec)
        (update :objects merge page-container)
        (update :scene-objects conj group-names)
        (update :actions merge dialog-container)
        (update-in [:metadata :stages] concat stages)
        (update-in [:metadata :stages] vec))))

(defn- add-title
  [t title]
  (-> t
      (assoc-in [:objects :title :text] title)
      (assoc-in [:objects :title :chunks] (text->chunks title))
      (assoc-in [:actions :dialog-1-title :data 0 :data 1 :phrase-text] title)))

(defn- add-title-audio
  [t audio]
  (-> t
      (assoc-in [:actions :dialog-1-title :data 0 :data 1 :audio] audio)
      (assoc :assets (conj (:assets t) (create-audio {:audio audio})))))

(defn- add-title-image
  [t image]
  (-> t
      (assoc-in [:objects :title-image :src] image)
      (assoc :assets (conj (:assets t) (create-asset {:img image})))))

(defn f
  [args]
  (-> t
      (add-title (:title args))
      (add-title-audio (:audio args))
      (add-pages (:pages args))
      (add-title-audio (:audio args))
      (add-title-image (:image args))
      ))

(core/register-template
  (:id m) m f)
