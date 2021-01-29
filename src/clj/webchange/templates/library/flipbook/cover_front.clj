(ns webchange.templates.library.flipbook.cover-front
  (:require
    [clojure.string :refer [join]]
    [clojure.tools.logging :as log]
    [webchange.utils.text :as text-utils]))

(def page-name "page-cover")

(def resources [])

(def template
  {:page-cover            {:type       "group"
                           :transition "page-cover"
                           :children   ["page-cover-background" "page-cover-image" "page-cover-title"]}
   :page-cover-background {:type   "rectangle"
                           :x      0
                           :y      0
                           :width  "---"
                           :height "---"
                           :fill   "---"}
   :page-cover-image      {:type       "image"
                           :x          "---"
                           :y          "---"
                           :width      600
                           :height     600
                           :image-size "contain"
                           :origin     {:type "center-center"}
                           :src        "---"}
   :page-cover-title      {:type     "group"
                           :x        "---"
                           :y        "---"
                           :children ["page-cover-title-text" "page-cover-authors"]}
   :page-cover-title-text {:type           "text"
                           :y              0
                           :font-size      60
                           :font-family    "Lexend Deca"
                           :align          "center"
                           :vertical-align "top"
                           :x              "---"
                           :width          "---"
                           :chunks         "---"
                           :fill           "---"
                           :text           "---"}
   :page-cover-authors    {:type           "text"
                           :x              0
                           :y              150
                           :vertical-align "top"
                           :fill           "---"
                           :align          "center"
                           :font-size      28
                           :text           "---"}})

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (let [page-center (/ width 2)]
    (-> page-data
        (assoc-in [:page-cover-background :width] width)
        (assoc-in [:page-cover-background :height] height)
        (assoc-in [:page-cover-title :x] page-center)
        (assoc-in [:page-cover-image :x] page-center)
        (assoc-in [:page-cover-title-text :x] (- (/ width 4)))
        (assoc-in [:page-cover-title-text :width] (/ width 2)))))

(defn- set-layout
  [page-data {:keys [layout]}]
  (let [title-top-y 150
        title-bottom-y 750
        image-top-y 350
        image-bottom-y 650]
    (case layout
      :title-top (-> page-data
                     (assoc-in [:page-cover-title :y] title-top-y)
                     (assoc-in [:page-cover-image :y] image-bottom-y))
      :title-bottom (-> page-data
                        (assoc-in [:page-cover-title :y] title-bottom-y)
                        (assoc-in [:page-cover-image :y] image-top-y)))))

(defn- set-content
  [page-data {:keys [image-src title authors]}]
  (let [authors-text (join "     " authors)]
    (-> page-data
        (assoc-in [:page-cover-image :src] image-src)
        (assoc-in [:page-cover-title-text :text] title)
        (assoc-in [:page-cover-title-text :chunks] (text-utils/text->chunks title))
        (assoc-in [:page-cover-authors :text] authors-text))))

(defn- set-colors
  [page-data {:keys [background-color text-color]}]
  (-> page-data
      (assoc-in [:page-cover-title-text :fill] text-color)
      (assoc-in [:page-cover-authors :fill] text-color)
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
