(ns webchange.templates.library.flipbook.custom-spread
  (:require
    [webchange.templates.library.flipbook.utils :as utils]
    [webchange.utils.scene-data :refer [generate-name rename-object]]
    [webchange.utils.text :as text-utils]))

(def page-template
  {:page       {:type       "group"
                :transition "page"
                :children   []}})

(def page-image-template
  {:type        "image"
   :image-size  "cover"
   :mask-align  "right-of-center"
   :x           0
   :y           0
   :width       "---"
   :height      "---"
   :mask-width  "---"
   :mask-height "---"
   :src         "---"})

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
  [page-data mask-align {:keys [width height]} {:keys [image-src]}]
  (let [page-image (assoc page-image-template
                     :mask-align (name mask-align)
                     :width (* width 2)
                     :height height
                     :mask-width width
                     :mask-height height
                     :src image-src)]
    (-> page-data
        (assoc :page-image page-image)
        (update-in [:page :children] conj "page-image"))))

(defn- add-text
  [page-data position {:keys [width height padding text-color]} {:keys [text] :or {text ""}}]
  (let [text-y-position (if (= position :top)
                          padding
                          (-> height (/ 4) (* 3)))
        page-text (assoc page-text-template
                    :x padding
                    :y text-y-position
                    :width (- width (* padding 2))
                    :height (- height (* padding 2))
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

(defn left-without-text
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :left-of-center page-params content-params))]
    (create-page objects-data content-params)))

(defn left-text-top
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :left-of-center page-params content-params)
                         (add-text :top page-params content-params))]
    (create-page objects-data content-params)))

(defn left-text-bottom
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :left-of-center page-params content-params)
                         (add-text :bottom page-params content-params))]
    (create-page objects-data content-params)))

(defn right-without-text
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :right-of-center page-params content-params))]
    (create-page objects-data content-params)))

(defn right-text-top
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :right-of-center page-params content-params)
                         (add-text :top page-params content-params))]
    (create-page objects-data content-params)))

(defn right-text-bottom
  [page-params content-params]
  (let [objects-data (-> page-template
                         (add-image :right-of-center page-params content-params)
                         (add-text :bottom page-params content-params))]
    (create-page objects-data content-params)))

(defn- get-spread-constructors
  [layout]
  (case layout
    :text-right-top [left-without-text right-text-top]
    :text-right-bottom [left-without-text right-text-bottom]
    :text-left-top [left-text-top right-without-text]
    :text-left-bottom [left-text-bottom right-without-text]))

(defn create
  [page-params {:keys [page-type] :as content-params}]
  (let [[left-page-constructor right-page-constructor] (get-spread-constructors page-type)
        left-page-data (left-page-constructor page-params content-params)
        right-page-data (right-page-constructor page-params content-params)]
    [left-page-data right-page-data]))
