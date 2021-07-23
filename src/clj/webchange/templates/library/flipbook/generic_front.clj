(ns webchange.templates.library.flipbook.generic-front)

(def page-name "generic-front-page")

(def resources {:logo "/raw/img/flipbook/logo_2.png"})

(def template
  {:generic-front-page                 {:type       "group"
                                        :transition "generic-front-page"
                                        :children   ["generic-front-page-background-back"
                                                     "generic-front-page-logo"]
                                        :generated? true}
   :generic-front-page-background-back {:type   "rectangle"
                                        :x      0
                                        :y      0
                                        :width  "---"
                                        :height "---"
                                        :fill   "---"}
   :generic-front-page-logo            {:type       "image"
                                        :x          "---"
                                        :y          450
                                        :origin     {:type "center-center"}
                                        :src        "---"
                                        :editable?  {:select true}}})

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (let [page-center (/ width 2)]
    (-> page-data
        (assoc-in [:generic-front-page-logo :x] page-center)
        (assoc-in [:generic-front-page-background-back :width] width)
        (assoc-in [:generic-front-page-background-back :height] height))))

(defn- set-content
  [page-data]
  (let [image-src (:logo resources)]
    (-> page-data
        (assoc-in [:generic-front-page-logo :src] image-src))))

(defn- set-colors
  [page-data {:keys [background-color]}]
  (-> page-data
      (assoc-in [:generic-front-page-background-back :fill] background-color)))

(defn create
  [page-params _]
  {:name      page-name
   :resources (vals resources)
   :objects   (-> template
                  (apply-page-size page-params)
                  (set-content)
                  (set-colors page-params))})
