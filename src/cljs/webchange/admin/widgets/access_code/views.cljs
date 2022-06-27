(ns webchange.admin.widgets.access-code.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.access-code.state :as state]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn access-code
  [{:keys [class-name school-id disabled? on-change] :as props}]
  (let [handle-change #(when (fn? on-change) (on-change %))
        handle-generate-click #(re-frame/dispatch [::state/generate-access-code school-id {:on-success handle-change}])]
    [ui/input (merge props
                     {:on-change  handle-change
                      :class-name (get-class-name {"widget--access-code" true
                                                   class-name            (some? class-name)})
                      :action     (when-not disabled?
                                    {:text       "Generate Code"
                                     :class-name "widget--access-code--generate-button "
                                     :on-click   handle-generate-click})})]))
