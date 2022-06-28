(ns webchange.admin.components.select-list.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.utils.list :refer [->>toggle-item]]))

(defn- select-list-item
  [{:keys [data on-click]}]
  (let [{:keys [id selected?]} data
        handle-click #(when (fn? on-click) (on-click id))]
    [ui/list-item (merge data
                         {:dense?  true
                          :actions [(cond-> {:icon     "check"
                                             :on-click handle-click}
                                            selected? (assoc :color "yellow-1"))]})]))

(defn- empty-item
  []
  [:div.empty-item "No items to select"])

(defn select-list
  [{:keys [class-name data on-change]}]
  (let [handle-change #(when (fn? on-change)
                         (on-change %))
        handle-item-clicked (fn [data selected-id]
                              (->> data
                                   (filter :selected?)
                                   (map :id)
                                   (->>toggle-item selected-id)
                                   (handle-change)))]
    [ui/list {:class-name (ui/get-class-name {"component--select-list" true
                                              class-name               (some? class-name)})}
     (if-not (empty? data)
       (for [{:keys [id] :as item} data]
         ^{:key id}
         [select-list-item {:data     item
                            :on-click #(handle-item-clicked data %)}])
       [empty-item])]))
