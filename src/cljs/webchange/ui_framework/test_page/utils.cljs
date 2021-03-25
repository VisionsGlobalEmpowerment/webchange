(ns webchange.ui-framework.test-page.utils
  (:require
    [reagent.core :as r]))

(defn group
  []
  (let [this (r/current-component)]
    (into [:div]
          (r/children this))))

(defn group-body
  []
  (let [this (r/current-component)]
    (into [:div]
          (r/children this))))

(defn group-header
  []
  (let [this (r/current-component)]
    (into [:h1]
          (r/children this))))

(defn table
  [{:keys [columns rows data]}]
  [:table
   (when (some? columns)
     [:thead
      [:tr
       (when (some? rows) [:th])
       (for [[column-index column-title] (map-indexed vector columns)]
         ^{:key column-index}
         [:th column-title])]])
   [:tbody
    (for [[row-index row] (map-indexed vector data)]
      ^{:key row-index}
      [:tr
       (when (some? rows)
         [:td (str (nth rows row-index))])
       (for [[cell-index cell] (map-indexed vector row)]
         ^{:key cell-index}
         [:td cell])])]])
