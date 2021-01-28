(ns webchange.templates.library.flipbook.custom-spread--text-small-transparent--left
  (:require
    [webchange.templates.library.flipbook.utils :as utils]))

(def template
  {:page       {:type       "group"
                :transition "page"
                :children   ["page-image"]}
   :page-image {:type        "image"
                :image-size  "cover"
                :mask-align  "left-of-center"
                :x           0
                :y           0
                :width       "---"
                :height      "---"
                :mask-width  "---"
                :mask-height "---"
                :src         "---"}})

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (-> page-data
      (assoc-in [:page-image :width] (* width 2))
      (assoc-in [:page-image :height] height)

      (assoc-in [:page-image :mask-width] width)
      (assoc-in [:page-image :mask-height] height)))

(defn- set-content
  [page-data {:keys [image-src]}]
  (-> page-data
      (assoc-in [:page-image :src] image-src)))

(defn create
  [page-params {:keys [image-src] :as content-params}]
  (let [page-name (utils/generate-name "page")
        objects-data (-> template
                         (apply-page-size page-params)
                         (set-content content-params)
                         (utils/rename-object "page" page-name))]
    {:name      page-name
     :text-name nil
     :resources [image-src]
     :objects   objects-data}))
