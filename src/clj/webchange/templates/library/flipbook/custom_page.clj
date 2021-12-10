(ns webchange.templates.library.flipbook.custom-page
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
  {:type       "image"
   :image-size "contain"
   :x          0
   :y          "---"
   :width      "---"
   :height     "---"
   :src        "---"
   :editable?  {:select true}})

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
   :align          "left"
   :font-size      38
   :font-family    "Lexend Deca"
   :chunks         "---"
   :x              "---"
   :y              "---"
   :width          "---"
   :height         "---"
   :fill           "---"
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

(defn- add-image
  [page-data {:keys [pos size]} {:keys [width height]} {:keys [image-src next-page-id]}]
  (let [image-height (-> height (* size) int)
        y (if (= pos :top)
            0
            (- height image-height))
        page-image (assoc page-image-template
                     :editable? {:drag true
                                 :select true}
                     :y y
                     :width width
                     :height image-height
                     :src image-src)
        page-id (generate-page-id next-page-id)
        object-id (generate-image-id next-page-id)]
    (-> page-data
        (assoc (keyword object-id) page-image)
        (update-in [(keyword page-id) :children] conj object-id))))

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

(defn- add-text-background
  [page-data {:keys [pos size]} {:keys [width height background-color]} {:keys [next-page-id]}]
  (let [background-height (-> height (* size) int)
        y (if (= pos :top)
            0
            (- height background-height))
        page-text-background (assoc page-text-background-template
                               :x 0
                               :y y
                               :width width
                               :height background-height
                               :fill background-color)
        page-id (generate-page-id next-page-id)
        object-id (generate-text-background-id next-page-id)]
    (-> page-data
        (assoc (keyword object-id) page-text-background)
        (update-in [(keyword page-id) :children] conj object-id))))

(defn- add-text
  [page-data {:keys [pos size]} {:keys [width height padding text-color]} {:keys [text next-page-id]}]
  (let [text (or text "")
        text-height (-> height (* size) int)
        y (if (= pos :top)
            padding
            (- height text-height))
        page-text (assoc page-text-template
                    :x padding
                    :y y
                    :width (- width (* padding 2))
                    :height (- text-height (* padding 2))
                    :fill text-color
                    :text text
                    :editable? {:select true}
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

(defn- image-only
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :top :size 1} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-at-top
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :bottom :size 4/5} page-params content-params)
                         (add-text-background {:pos :top :size 1/5} page-params content-params)
                         (add-text {:pos :top :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-small-at-bottom
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :top :size 4/5} page-params content-params)
                         (add-text-background {:pos :bottom :size 1/5} page-params content-params)
                         (add-text {:pos :bottom :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-big-at-bottom
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :top :size 3/4} page-params content-params)
                         (add-text-background {:pos :bottom :size 1/4} page-params content-params)
                         (add-text {:pos :bottom :size 1/4} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-only
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-text-background {:pos :top :size 1} page-params content-params)
                         (add-text {:pos :top :size 1} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-over-image-at-top
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :top :size 1} page-params content-params)
                         (add-text {:pos :top :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn- text-over-image-at-bottom
  [page-params content-params]
  (let [objects-data (-> (get-page-template content-params)
                         (add-background page-params content-params)
                         (add-image {:pos :top :size 1} page-params content-params)
                         (add-text {:pos :bottom :size 1/5} page-params content-params))]
    (create-page objects-data content-params)))

(defn constructors
  [page-type]
  [(case page-type
     :image-only image-only
     :text-at-top text-at-top
     :text-big-at-bottom text-big-at-bottom
     :text-only text-only
     :text-small-at-bottom text-small-at-bottom
     :text-over-image-at-top text-over-image-at-top
     :text-over-image-at-bottom text-over-image-at-bottom)])
