(ns webchange.templates.library.flipbook.page-number
  (:require
    [webchange.utils.list :refer [without-item]]
    [webchange.utils.scene-data :refer [generate-name]]))

(def number-object-prefix "page-number")

(def template
  {:type           "text"
   :vertical-align "top"
   :font-size      38
   :x              "---"
   :y              "---"
   :fill           "---"
   :text           "---"})

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

(defn- add-page-number-object
  ([activity-data page-group-name page-data page-number page-side]
   (let [object-name (generate-name number-object-prefix)
         object-data (-> template
                         (assoc :text page-number)
                         (set-color page-data)
                         (set-position (merge page-data
                                              {:page-side page-side})))]
     (-> activity-data
         (update-in [:objects (keyword page-group-name) :children] add-child object-name)
         (assoc-in [:objects (keyword object-name)] object-data)))))

(defn add-page-number
  [activity-data page-group-name page-data page-number page-side]
  (if-not (= page-number 0)
    (add-page-number-object activity-data page-group-name page-data page-number page-side)
    activity-data))

(defn- remove-page-number
  [activity-data page-object-name]
  (let [page-children (get-in activity-data [:objects page-object-name :children])
        page-number-names (->> page-children
                               (filter (fn [child-name]
                                         (clojure.string/starts-with? child-name number-object-prefix)))
                               (map keyword))]
    (reduce (fn [activity-data page-number-name]
              (-> activity-data
                  (update :objects dissoc page-number-name)
                  (update-in [:objects page-object-name :children] without-item (clojure.core/name page-number-name))))
            activity-data
            page-number-names)))

(defn update-pages-numbers
  [activity-data page-data]
  (let [{:keys [flipbook-name]} (get activity-data :metadata)
        pages (->> (get-in activity-data [:objects (keyword flipbook-name) :pages])
                   (map :object)
                   (map keyword))]

    (->> pages
         (reduce (fn [{:keys [data page-number page-side]} page-object-name]
                   {:page-side   (if (= page-side "right") "left" "right")
                    :page-number (inc page-number)
                    :data        (-> data
                                     (remove-page-number page-object-name)
                                     (add-page-number page-object-name page-data page-number page-side))})
                 {:data        activity-data
                  :page-number 0
                  :page-side   "right"})
         (:data))))
