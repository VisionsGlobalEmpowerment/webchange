(ns webchange.templates.library.flipbook.front-cover
  (:require
    [clojure.string :refer [join]]))

(def cover-name "page-cover")

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
                           :max-width  600
                           :max-height 600
                           :scale-x    10
                           :scale-y    10
                           :origin     {:type "center-center"}
                           :src        "---"}
   :page-cover-title      {:type     "group"
                           :x        "---"
                           :y        "---"
                           :children ["page-cover-title-text" "page-cover-authors"]}
   :page-cover-title-text {:type           "text"
                           :x              0
                           :y              0
                           :vertical-align "top"
                           :fill           "---"
                           :font-size      60
                           :font-family    "Lexend Deca"
                           :align          "center"
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
  [template {:keys [width height]}]
  (let [page-center (/ width 2)]
    (-> template
        (assoc-in [:page-cover-background :width] width)
        (assoc-in [:page-cover-background :height] height)
        (assoc-in [:page-cover-title :x] page-center)
        (assoc-in [:page-cover-image :x] page-center))))

(defn- set-layout
  [template {:keys [layout]}]
  (let [title-top-y 150
        title-bottom-y 750
        image-top-y 350
        image-bottom-y 650]
    (case layout
      :title-top (-> template
                     (assoc-in [:page-cover-title :y] title-top-y)
                     (assoc-in [:page-cover-image :y] image-bottom-y))
      :title-bottom (-> template
                        (assoc-in [:page-cover-title :y] title-bottom-y)
                        (assoc-in [:page-cover-image :y] image-top-y)))))

(defn- set-content
  [template {:keys [image-src title authors]}]
  (let [authors-text (join "     " authors)]
    (-> template
        (assoc-in [:page-cover-image :src] image-src)
        (assoc-in [:page-cover-title-text :text] title)
        (assoc-in [:page-cover-authors :text] authors-text))))

(defn- set-colors
  [template {:keys [background-color text-color]
             :or   {background-color 0xff9800
                    text-color       0x000000}}]
  (-> template
      (assoc-in [:page-cover-title-text :fill] text-color)
      (assoc-in [:page-cover-authors :fill] text-color)
      (assoc-in [:page-cover-background :fill] background-color)))

(defn create
  "props:
     :layout - `:title-top` or `:title-bottom`
     :image-src - cover image url
     :title - book title
     :authors - array of authors
     :background-color (default=0xff9800)
     :text-color (default=0x000000)"
  [props]
  (let [page-size {:width  880
                   :height 1080}]
    {:name    cover-name
     :objects (-> template
                  (apply-page-size page-size)
                  (set-layout props)
                  (set-content props)
                  (set-colors props))}))
