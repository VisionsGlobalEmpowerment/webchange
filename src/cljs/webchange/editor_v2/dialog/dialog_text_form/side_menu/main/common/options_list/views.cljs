(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- options-list-item
  [{:keys [text value selected? on-click]}]
  (let [handle-click #(on-click value)]
    [:li {:class-name (get-class-name {"options-list-item" true
                                       "selected"          selected?})
          :on-click   handle-click}
     text]))

(defn options-list
  [{:keys [options on-click]}]
  [:ul.options-list
   (for [{:keys [value] :as option} options]
     ^{:key value}
     [options-list-item (merge option
                               {:on-click on-click})])])
