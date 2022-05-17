(ns webchange.admin.components.select-list.views
  (:require
    [webchange.admin.components.list.views :as list]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.list :refer [->>toggle-item]]))

(defn- select-list-item
  [{:keys [data on-click]}]
  (let [{:keys [id selected?]} data
        handle-click #(when (fn? on-click) (on-click id))]
    [list/list-item (merge data
                           {:class-name "component--select-list-item"
                            :on-click   handle-click
                            :actions    [ui/icon {:icon       "check"
                                                  :class-name (ui/get-class-name {"check-icon" true
                                                                                  "selected"   selected?})}]})]))

(defn select-list
  [{:keys [data on-change]}]
  (let [handle-change #(when (fn? on-change)
                         (on-change %))
        handle-item-clicked (fn [data selected-id]
                              (->> data
                                   (filter :selected?)
                                   (map :id)
                                   (->>toggle-item selected-id)
                                   (handle-change)))]
    [list/list {:class-name "component--select-list"}
     (for [{:keys [id] :as item} data]
       ^{:key id}
       [select-list-item {:data      item
                          :on-click  #(handle-item-clicked data %)}])]))
