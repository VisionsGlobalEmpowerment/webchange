(ns webchange.templates.library.flipbook.back-filler)

(def page-name "page-back-filler")

(def resources [])

(def template
  {:page-back-filler            {:type       "group"
                                 :transition "page-cover-back"
                                 :children   ["page-back-filler-background"]
                                 :generated? true}
   :page-back-filler-background {:type   "rectangle"
                                 :x      0
                                 :y      0
                                 :width  "---"
                                 :height "---"
                                 :fill   0x00B2FF}})

(defn- apply-page-size
  [page-data {:keys [width height]}]
  (-> page-data
      (assoc-in [:page-back-filler-background :width] width)
      (assoc-in [:page-back-filler-background :height] height)))

(defn- set-colors
  [page-data {:keys [background-color]}]
  (-> page-data
      (assoc-in [:page-back-filler-background :fill] background-color)))

(defn create
  [page-params _]
  {:name      page-name
   :resources resources
   :objects   (-> template
                  (apply-page-size page-params)
                  (set-colors page-params))})
