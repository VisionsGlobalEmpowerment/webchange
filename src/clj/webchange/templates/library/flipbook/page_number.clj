(ns webchange.templates.library.flipbook.page-number
  (:require
    [webchange.templates.library.flipbook.utils :as utils]))

(def template
  {:type           "text"
   :vertical-align "top"
   :font-size      38
   :x              "---"
   :y              "---"
   :fill           "---"
   :text           "---"})

(defn- get-page-position
  [page-number starts-from]
  (let [name->number (fn [name] (if (= name "left") 0 1))
        number->name (fn [number] (if (= number 0) "left" "right"))]
    (if (even? page-number)
      (->> starts-from name->number (- 1) number->name)
      starts-from)))

(defn- set-position
  [object-data {:keys [width height padding page-side]}]
  (let [[x y] (case page-side
                "left" [35 (- height padding 5)]
                "right" [(- width padding 10) (- height padding 5)])]
    (-> object-data
        (assoc :x x)
        (assoc :y y))))

(defn- set-color
  [object-data {:keys [text-color]}]
  (-> object-data
      (assoc :fill text-color)))

(defn- add-child
  [children child-name]
  (-> children
      (vec)
      (conj child-name)))

(defn add-page-number
  [activity-data page-group-name page-data]
  (let [current-page-number (-> activity-data (get-in [:metadata :flipbook-pages :total] 0) (inc))
        pages-start-from (-> activity-data (get-in [:metadata :flipbook-pages :start-from] "left"))
        page-side (get-page-position current-page-number pages-start-from)

        object-name (utils/generate-name "page-number")
        object-data (-> template
                        (assoc :text current-page-number)
                        (set-color page-data)
                        (set-position (merge page-data
                                             {:page-side page-side})))]
    (-> activity-data
        (update-in [:objects (keyword page-group-name) :children] add-child object-name)
        (assoc-in [:objects (keyword object-name)] object-data)
        (update-in [:metadata :flipbook-pages :total] inc))))
