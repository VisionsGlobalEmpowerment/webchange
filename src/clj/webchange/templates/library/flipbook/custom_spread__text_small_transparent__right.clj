(ns webchange.templates.library.flipbook.custom-spread--text-small-transparent--right
  (:require
    [webchange.templates.library.flipbook.utils :as utils]
    [webchange.utils.text :as text-utils]
    [clojure.tools.logging :as log]))

(def template
  {:page       {:type       "group"
                :transition "page"
                :children   ["page-image" "page-text"]}
   :page-image {:type        "image"
                :image-size  "cover"
                :mask-align  "right-of-center"
                :x           0
                :y           0
                :width       "---"
                :height      "---"
                :mask-width  "---"
                :mask-height "---"
                :src         "---"}
   :page-text  {:type           "text"
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
  (-> page-data
      (assoc-in [:page-image :width] (* width 2))
      (assoc-in [:page-image :height] height)
      (assoc-in [:page-image :mask-width] width)
      (assoc-in [:page-image :mask-height] height)

      (assoc-in [:page-text :x] padding)
      (assoc-in [:page-text :y] padding)
      (assoc-in [:page-text :width] (- width (* padding 2)))
      (assoc-in [:page-text :height] (- height (* padding 2)))))

(defn- set-colors
  [page-data {:keys [text-color]}]
  (-> page-data
      (assoc-in [:page-text :fill] text-color)))

(defn- set-content
  [page-data {:keys [image-src text]
              :or   {text ""}}]
  (-> page-data
      (assoc-in [:page-image :src] image-src)
      (assoc-in [:page-text :text] text)
      (assoc-in [:page-text :chunks] (text-utils/text->chunks text))))

(defn create
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
