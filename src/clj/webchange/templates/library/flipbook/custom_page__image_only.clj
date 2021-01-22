(ns webchange.templates.library.flipbook.custom-page--image-only
  (:require
    [webchange.templates.library.flipbook.utils :as utils]))

(def template
  {:page       {:type       "group"
                :transition "page-1"
                :children   ["page-image"]}
   :page-image {:type   "image"
                :x      0
                :y      0
                :width  "---"
                :height "---"
                :src    "---"}})

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (-> page-data
      (assoc-in [:page-image :width] width)
      (assoc-in [:page-image :height] height)))

(defn- set-content
  [page-data {:keys [image-src]}]
  (-> page-data
      (assoc-in [:page-image :src] image-src)))

(defn create
  "content-params:
     :image-src - cover image url"
  [page-params {:keys [image-src] :as content-params}]
  (let [page-name (utils/generate-name "page")]
    {:name      page-name
     :resources [image-src]
     :objects   (-> template
                    (apply-page-size page-params)
                    (set-content content-params)
                    (utils/rename-object "page" page-name))}))
