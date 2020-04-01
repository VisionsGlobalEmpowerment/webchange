(ns webchange.student-dashboard.history.views-history-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.student-dashboard.history.views-history-list-item :refer [history-list-item]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn prepare-data
  [{:keys [image] :as data}]
  (-> data
      (assoc :image {:png image})))

(defn- get-styles
  []
  {:empty-list-item {:padding "16px 0"}
   :list            {:background-color (get-in-theme [:palette :background :default])}
   :list-item       {:border-bottom-style "solid"
                     :border-bottom-width "1px"
                     :border-bottom-color (get-in-theme [:palette :text :secondary])}})

(defn- last?
  [item list]
  (->> (last list)
       (= item)))

(defn- empty-list-placeholder
  []
  (let [styles (get-styles)]
    [ui/list {:style (:list styles)}
     [ui/list-item {:style (:empty-list-item styles)}
      [ui/list-item-text {:primary "No activities in history"}]]]))

(defn history-list
  [{:keys [data max-count on-click]}]
  (let [styles (get-styles)
        history-list (if-not (nil? max-count)
                       (take-last max-count data)
                       data)]
    (if (< 0 (count history-list))
      [ui/list {:style (:list styles)}
       (for [{:keys [level lesson activity] :as item} history-list]
         (let [style (when-not (last? item data)
                       (:list-item styles))]
           ^{:key (str level "-" lesson "-" activity)}
           [history-list-item (merge (prepare-data item)
                                     {:on-click on-click
                                      :style    style})]))]
      [empty-list-placeholder])))
