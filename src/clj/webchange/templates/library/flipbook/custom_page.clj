(ns webchange.templates.library.flipbook.custom-page
  (:require
    [webchange.templates.library.flipbook.utils :as utils]
    [webchange.utils.scene-data :refer [generate-name rename-object]]
    [webchange.utils.text :as text-utils]))

(def page-template
  {:page {:type       "group"
          :transition "page"
          :children   []}})

(def page-image-template
  {:type        "image"
   :image-size  "contain"
   :x           0
   :y           "---"
   :width       "---"
   :height      "---"
   :src         "---"})

(def page-text-background-template
  {:type   "rectangle"
   :x      0
   :y      "---"
   :width  "---"
   :height "---"
   :fill   "---"})

(def page-text-template
  {:type           "text"
   :word-wrap      true
   :vertical-align "top"
   :align          "center"
   :font-size      38
   :chunks         "---"
   :x              "---"
   :y              "---"
   :width          "---"
   :height         "---"
   :fill           "---"
   :text           "---"})

(defn- add-image
  [page-data {:keys [pos size]} {:keys [width height]} {:keys [image-src]}]
  (let [image-height (-> height (* size) int)
        y (if (= pos :top)
            0
            (- height image-height))
        page-image (assoc page-image-template
                     :y y
                     :width width
                     :height image-height
                     :src image-src)]
    (-> page-data
        (assoc :page-image page-image)
        (update-in [:page :children] conj "page-image"))))

(defn- add-text-background
  [page-data {:keys [pos size]} {:keys [width height background-color]}]
  (let [background-height (-> height (* size) int)
        y (if (= pos :top)
            0
            (- height background-height))
        page-text-background (assoc page-text-background-template
                    :x 0
                    :y y
                    :width width
                    :height background-height
                    :fill background-color)]
    (-> page-data
        (assoc :page-text-background page-text-background)
        (update-in [:page :children] conj "page-text-background"))))

(defn- add-text
  [page-data {:keys [pos size]} {:keys [width height padding text-color]} {:keys [text]}]
  (let [text (or text "")
        text-height (-> height (* size) int)
        y (if (= pos :top)
            0
            (- height text-height))
        page-text (assoc page-text-template
                    :x padding
                    :y (+ y padding)
                    :width (- width (* padding 2))
                    :height (- text-height (* padding 2))
                    :fill text-color
                    :text text
                    :chunks (text-utils/text->chunks text))]
    (-> page-data
        (assoc :page-text page-text)
        (update-in [:page :children] conj "page-text"))))

(defn- create-page
  [objects-data {:keys [image-src]}]
  (let [page-name (generate-name "page")
        objects-data (rename-object objects-data "page" page-name)]
    {:name      page-name
     :text-name (utils/get-text-name objects-data)
     :resources [image-src]
     :objects   objects-data}))

(defn- image-only
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :top :size 1} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-at-top
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :bottom :size 4/5} page-params content-params)
                         (add-text-background {:pos :top :size 1/5} page-params)
                         (add-text {:pos :top :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-small-at-bottom
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :top :size 4/5} page-params content-params)
                         (add-text-background {:pos :bottom :size 1/5} page-params)
                         (add-text {:pos :bottom :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-big-at-bottom
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :top :size 3/4} page-params content-params)
                         (add-text-background {:pos :bottom :size 1/4} page-params)
                         (add-text {:pos :bottom :size 1/4} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-only
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-text-background {:pos :top :size 1} page-params)
                         (add-text {:pos :top :size 1} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-over-image-at-top
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :top :size 1} page-params content-params)
                         (add-text {:pos :top :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-over-image-at-bottom
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image {:pos :top :size 1} page-params content-params)
                         (add-text {:pos :bottom :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn create
  [page-params {:keys [page-type] :as content-params}]
  (case page-type
    :image-only (image-only page-params content-params)
    :text-at-top (text-at-top page-params content-params)
    :text-big-at-bottom (text-big-at-bottom page-params content-params)
    :text-only (text-only page-params content-params)
    :text-small-at-bottom (text-small-at-bottom page-params content-params)
    :text-over-image-at-top (text-over-image-at-top page-params content-params)
    :text-over-image-at-bottom (text-over-image-at-bottom page-params content-params)))
