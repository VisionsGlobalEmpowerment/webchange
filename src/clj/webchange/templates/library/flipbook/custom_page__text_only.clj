(ns webchange.templates.library.flipbook.custom-page--text-only
  (:require
    [webchange.templates.library.flipbook.utils :as utils]))

(def template
  {:page                 {:type       "group"
                          :transition "page-1"
                          :children   ["page-text-background" "page-text"]}
   :page-text-background {:type   "rectangle"
                          :x      0
                          :y      0
                          :width  "---"
                          :height "---"
                          :fill   "---"}
   :page-text            {:type           "text"
                          :word-wrap      true
                          :vertical-align "top"
                          :x              "---"
                          :y              "---"
                          :width          "---"
                          :height         "---"
                          :fill           "---"
                          :text           "---"}})

(defn- apply-page-size
  [page-data {:keys [width height padding]}]
  (-> page-data
      (assoc-in [:page-text :x] padding)
      (assoc-in [:page-text :y] padding)
      (assoc-in [:page-text :width] (- width (* padding 2)))
      (assoc-in [:page-text :height] (- height (* padding 2)))

      (assoc-in [:page-text-background :width] width)
      (assoc-in [:page-text-background :height] height)))

(defn- set-colors
  [page-data {:keys [background-color text-color]}]
  (-> page-data
      (assoc-in [:page-text-background :fill] background-color)
      (assoc-in [:page-text :fill] text-color)))

(defn- set-content
  [page-data {:keys [text]}]
  (-> page-data
      (assoc-in [:page-text :text] text)))

(defn create
  "content-params:
     :text - book title"
  [page-params {:keys [image-src] :as content-params}]
  (let [page-name (utils/generate-name "page")]
    {:name      page-name
     :resources [image-src]
     :objects   (-> template
                    (apply-page-size page-params)
                    (set-colors page-params)
                    (set-content content-params)
                    (utils/rename-object "page" page-name))}))
