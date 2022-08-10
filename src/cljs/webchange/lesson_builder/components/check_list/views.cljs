(ns webchange.lesson-builder.components.check-list.views
  (:require
    [webchange.ui.index :as ui]))

(defn- check-list-item
  [{:keys [selected? text on-click value]}]
  (let [handle-click #(when (fn? on-click)
                        (on-click value))]
    [:div {:class-name (ui/get-class-name {"check-list--item"           true
                                           "check-list--item--selected" selected?})
           :on-click   handle-click}
     [:span.check-list--item--name
      text]
     [ui/icon {:icon       "check"
               :class-name "check-list--item--icon"}]]))

(defn check-list
  [{:keys [items on-click value]}]
  [:div.component--check-list
   (for [item-data items]
     ^{:key (:value item-data)}
     [check-list-item (merge item-data
                             {:on-click  on-click
                              :selected? (= value (:value item-data))})])])
