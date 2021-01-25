(ns webchange.templates.library.flipbook.generic-front)

(def page-name "generic-front-page")

(def resources {:logo "/raw/img/flipbook/logo.png"})

(def template
  {:generic-front-page                   {:type       "group"
                                          :transition "generic-front-page"
                                          :children   ["generic-front-page-background"]}
   :generic-front-page-background        {:type     "group"
                                          :x        0
                                          :y        0
                                          :children ["generic-front-page-background-border"
                                                     "generic-front-page-background-back"
                                                     "generic-front-page-logo"]}
   :generic-front-page-background-border {:type   "rectangle"
                                          :x      0
                                          :y      0
                                          :width  "---"
                                          :height "---"
                                          :fill   "---"}
   :generic-front-page-background-back   {:type   "rectangle"
                                          :x      "---"
                                          :y      "---"
                                          :width  "---"
                                          :height "---"
                                          :fill   "---"}
   :generic-front-page-logo              {:type       "image"
                                          :x          "---"
                                          :y          350
                                          :width      600
                                          :height     600
                                          :image-size "contain"
                                          :origin     {:type "center-center"}
                                          :src        "---"}})

(defn- apply-page-size
  [page-data {:keys [width height padding]}]
  (let [page-center (/ width 2)]
    (-> page-data
        (assoc-in [:generic-front-page-logo :x] page-center)
        (assoc-in [:generic-front-page-background-border :width] width)
        (assoc-in [:generic-front-page-background-border :height] height)
        (assoc-in [:generic-front-page-background-back :x] padding)
        (assoc-in [:generic-front-page-background-back :y] padding)
        (assoc-in [:generic-front-page-background-back :width] (- width (* 2 padding)))
        (assoc-in [:generic-front-page-background-back :height] (- height (* 2 padding))))))

(defn- set-content
  [page-data]
  (let [image-src (:logo resources)]
    (-> page-data
        (assoc-in [:generic-front-page-logo :src] image-src))))

(defn- set-colors
  [page-data {:keys [background-color border-color]}]
  (-> page-data
      (assoc-in [:generic-front-page-background-back :fill] background-color)
      (assoc-in [:generic-front-page-background-border :fill] border-color)))

(defn create
  [page-params _]
  {:name      page-name
   :resources (vals resources)
   :objects   (-> template
                  (apply-page-size page-params)
                  (set-content)
                  (set-colors page-params))})