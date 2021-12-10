(ns webchange.templates.library.flipbook.cover-front
  (:require
    [clojure.string :refer [join]]
    [webchange.utils.text :as text-utils]))

(def page-name "page-cover")

(def resources [])

(def template
  {:page-cover              {:type       "group"
                             :transition "page-cover"
                             :children   ["page-cover-background" "page-cover-image" "page-cover-title"]}
   :page-cover-background   {:type   "rectangle"
                             :x      0
                             :y      0
                             :width  "---"
                             :height "---"
                             :fill   "---"}
   :page-cover-image        {:type       "image"
                             :x          "---"
                             :y          "---"
                             :width      832
                             :height     744
                             :image-size "contain"
                             :origin     {:type "center-center"}
                             :src        "---"
                             :editable?  {:select true
                                          :drag   true
                                          :edit-form {:select-image true
                                                      :upload-image true
                                                      :scale true
                                                      :flip true
                                                      :visible true}}}
   :page-cover-title        {:type     "group"
                             :x        "---"
                             :y        "---"
                             :children ["page-cover-title-text" "page-cover-authors" "page-cover-illustrators"]}
   :page-cover-title-text   {:type           "text"
                             :y              0
                             :font-size      48
                             :font-family    "Lexend Deca"
                             :align          "left"
                             :vertical-align "top"
                             :editable?      {:select true}
                             :x              "---"
                             :width          "---"
                             :chunks         "---"
                             :fill           "---"
                             :text           "---"}
   :page-cover-authors      {:type           "text"
                             :x              0
                             :y              85
                             :vertical-align "top"
                             :editable?      {:select true}
                             :fill           "---"
                             :align          "left"
                             :font-size      24
                             :font-family    "Lexend Deca"
                             :text           "---"}
   :page-cover-illustrators {:type           "text"
                             :x              0
                             :y              125
                             :vertical-align "top"
                             :editable?      {:select true}
                             :fill           "---"
                             :align          "left"
                             :font-size      24
                             :font-family    "Lexend Deca"
                             :text           "---"}
   })

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (let [page-center (/ width 2)]
    (-> page-data
        (assoc-in [:page-cover-background :width] width)
        (assoc-in [:page-cover-background :height] height)
        (assoc-in [:page-cover-title :x] 64)
        (assoc-in [:page-cover-image :x] page-center)
        (assoc-in [:page-cover-title-text :x] 0)
        (assoc-in [:page-cover-title-text :width] (* width 0.8))
        (assoc-in [:page-cover-authors :width] (* width 0.8))
        (assoc-in [:page-cover-illustrators :width] (* width 0.8)))))

(defn- set-layout
  [page-data {:keys [layout]}]
  (let [title-top-y 150
        title-bottom-y 880
        image-top-y 436
        image-bottom-y 850]
    (case layout
      :title-top (-> page-data
                     (assoc-in [:page-cover-title :y] title-top-y)
                     (assoc-in [:page-cover-image :y] image-bottom-y))
      :title-bottom (-> page-data
                        (assoc-in [:page-cover-title :y] title-bottom-y)
                        (assoc-in [:page-cover-image :y] image-top-y)))))

(defn- set-content
  [page-data
   {:keys [image-src title authors illustrators]
    :or   {authors      ["___"]
           illustrators ["___"]}}]
  (let [authors-text (->> (join ", " authors)
                          (str "By "))
        illustrators-text (->> (join ", " illustrators)
                               (str "Illustrated by "))]
    (-> page-data
        (assoc-in [:page-cover-image :src] image-src)
        (assoc-in [:page-cover-title-text :text] title)
        (assoc-in [:page-cover-title-text :chunks] (text-utils/text->chunks title))
        (assoc-in [:page-cover-authors :text] authors-text)
        (assoc-in [:page-cover-illustrators :text] illustrators-text))))

(defn- set-colors
  [page-data {:keys [background-color text-color]}]
  (-> page-data
      (assoc-in [:page-cover-title-text :fill] text-color)
      (assoc-in [:page-cover-authors :fill] text-color)
      (assoc-in [:page-cover-illustrators :fill] text-color)
      (assoc-in [:page-cover-background :fill] background-color)))

(defn create
  "content-params:
     :layout - `:title-top` or `:title-bottom`
     :image-src - cover image url
     :title - book title
     :authors - array of authors
     :background-color (default=0xff9800)
     :text-color (default=0x000000)"
  [page-params content-params]
  {:name      page-name
   :text-name "page-cover-title-text"
   :resources resources
   :objects   (-> template
                  (apply-page-size page-params)
                  (set-layout content-params)
                  (set-content content-params)
                  (set-colors page-params))})
