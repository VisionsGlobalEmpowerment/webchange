(ns webchange.templates.library.flipbook.custom-spread
  (:require
    [webchange.templates.library.flipbook.utils :as utils]
    [webchange.utils.scene-data :refer [generate-name rename-object]]
    [webchange.utils.text :as text-utils]))

(def page-template
  {:type     "group"
   :children []})

(def page-background-template
  {:type   "rectangle"
   :x      0
   :y      0
   :width  "---"
   :height "---"
   :fill   "---"})

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
   :editable?      {:select     true
                    :drag       true}
   :metadata       {:removable? true}
   :text           "---"})

(defn- generate-page-id
  [suffix]
  (str "page-" suffix))

(defn- generate-image-id [id]
  (str "page-image-" id))

(defn- generate-background-id [id]
  (str "page-background-" id))

(defn- generate-text-background-id [id]
  (str "page-text-background-" id))

(defn- generate-text-id [id]
  (str "page-text-" id))

(defn- add-background
  [page-data {:keys [width height background-color]} {:keys [next-page-id]}]
  (let [page-background (assoc page-background-template
                          :width width
                          :height height
                          :fill background-color)
        page-id (generate-page-id next-page-id)
        object-id (generate-background-id next-page-id)]
    (-> page-data
        (assoc (keyword object-id) page-background)
        (update-in [(keyword page-id) :children] conj object-id))))

(defn- add-image
  [page-data mask-align {:keys [width height]} {:keys [image-src next-page-id]}]
  (let [page-image (assoc page-image-template
                     :mask-align (name mask-align)
                     :width (* width 2)
                     :height height
                     :mask-width width
                     :mask-height height
                     :src image-src)
        page-id (generate-page-id next-page-id)
        object-id (generate-image-id next-page-id)]
    (-> page-data
        (assoc (keyword object-id) page-image)
        (update-in [(keyword page-id) :children] conj object-id))))

(defn- add-text
  [page-data position {:keys [width height padding text-color]} {:keys [text next-page-id] :or {text ""}}]
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
                    :chunks (text-utils/text->chunks text))
        page-id (generate-page-id next-page-id)
        object-id (generate-text-id next-page-id)]
    (-> page-data
        (assoc (keyword object-id) page-text)
        (update-in [(keyword page-id) :children] conj object-id))))

(defn- create-page
  [objects-data {:keys [image-src next-page-id]}]
  (let [page-name (generate-page-id next-page-id)]
    {:name      page-name
     :text-name (utils/get-text-name objects-data)
     :resources [image-src]
     :objects   objects-data}))

(defn- get-page-template
  [{next-page-id :next-page-id}]
  (let [page-id (generate-page-id next-page-id)]
    {(keyword page-id) page-template}))

(defn left-without-text
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :left-of-center page-params content-params))]
    (create-page objects-data content-params)))

(defn left-text-top
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :left-of-center page-params content-params)
                         (add-text :top page-params content-params))]
    (create-page objects-data content-params)))

(defn left-text-bottom
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :left-of-center page-params content-params)
                         (add-text :bottom page-params content-params))]
    (create-page objects-data content-params)))

(defn right-without-text
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :right-of-center page-params content-params))]
    (create-page objects-data content-params)))

(defn right-text-top
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :right-of-center page-params content-params)
                         (add-text :top page-params content-params))]
    (create-page objects-data content-params)))

(defn right-text-bottom
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image :right-of-center page-params content-params)
                         (add-text :bottom page-params content-params))]
    (create-page objects-data content-params)))

(defn constructors
  [page-type]
  (case page-type
    :text-right-top [left-without-text right-text-top]
    :text-right-bottom [left-without-text right-text-bottom]
    :text-left-top [left-text-top right-without-text]
    :text-left-bottom [left-text-bottom right-without-text]
    :image-only [left-without-text right-without-text]))
