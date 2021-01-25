(ns webchange.templates.library.flipbook.custom-page--text-small-at-bottom
  (:require
    [webchange.templates.library.flipbook.utils :as utils]
    [webchange.utils.text :as text-utils]))

(def template
  {:page                 {:type       "group"
                          :transition "page-1"
                          :children   ["page-image" "page-text-background" "page-text"]}
   :page-image           {:type       "image"
                          :x          0
                          :y          0
                          :width      "---"
                          :height     "---"
                          :image-size "cover"
                          :src        "---"}
   :page-text-background {:type   "rectangle"
                          :x      0
                          :y      "---"
                          :width  "---"
                          :height "---"
                          :fill   "---"}
   :page-text            {:type           "text"
                          :word-wrap      true
                          :vertical-align "top"
                          :font-size      38
                          :chunks         "---"
                          :x              "---"
                          :y              "---"
                          :width          "---"
                          :height         "---"
                          :fill           "---"
                          :text           "---"}})

(defn- apply-page-size
  [page-data {:keys [width height padding]}]
  (let [text-area-y (-> height (/ 4) (* 3))]
    (-> page-data
        (assoc-in [:page-image :width] width)
        (assoc-in [:page-image :height] text-area-y)

        (assoc-in [:page-text :x] padding)
        (assoc-in [:page-text :y] (+ text-area-y padding))
        (assoc-in [:page-text :width] (- width (* padding 2)))
        (assoc-in [:page-text :height] (- height text-area-y (* padding 2)))

        (assoc-in [:page-text-background :y] text-area-y)
        (assoc-in [:page-text-background :width] width)
        (assoc-in [:page-text-background :height] (- height text-area-y)))))

(defn- set-colors
  [page-data {:keys [background-color text-color]}]
  (-> page-data
      (assoc-in [:page-text-background :fill] background-color)
      (assoc-in [:page-text :fill] text-color)))

(defn- set-content
  [page-data {:keys [image-src text]}]
  (-> page-data
      (assoc-in [:page-image :src] image-src)
      (assoc-in [:page-text :text] text)
      (assoc-in [:page-text :chunks] (text-utils/text->chunks text))))

(defn create
  "content-params:
     :image-src - cover image url
     :text - book title"
  [page-params {:keys [image-src] :as content-params}]
  (let [page-name (utils/generate-name "page")
        objects-data (-> template
                         (apply-page-size page-params)
                         (set-colors page-params)
                         (set-content content-params)
                         (utils/rename-object "page" page-name))]
    {:name      page-name
     :text-name (utils/get-text-name objects-data)
     :resources [image-src]
     :objects   objects-data}))
