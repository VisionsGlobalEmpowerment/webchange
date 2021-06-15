(ns webchange.editor-v2.dialog.dialog-text-form.menu.views-delay
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-action]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-empty-action]]
    [webchange.ui-framework.components.index :refer [input]]))

(defn delay-menu
  [{:keys [action-data]}]
  (let [{:keys [path source delay]} action-data
        handle-change (fn [value]
                        (re-frame/dispatch [::state-action/set-action-delay {:action-path path
                                                                             :action-type source
                                                                             :value       value}]))]
    [:div
     [input {:value     delay
             :type      "int"
             :on-change handle-change}]]))
