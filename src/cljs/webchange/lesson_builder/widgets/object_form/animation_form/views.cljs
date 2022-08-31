(ns webchange.lesson-builder.widgets.object-form.animation-form.views
  (:require
    [webchange.lesson-builder.widgets.choose-character.views :refer [choose-character]]
    [webchange.lesson-builder.widgets.object-form.common.views :refer [flip-component scale-component]]
    [webchange.ui.index :as ui]))

(defn animation-form
  [{:keys [class-name data on-change] :as props}]
  (let [{:keys [editable?]} data
        options (get editable? :edit-form {:scale true
                                           :flip  true})
        handle-change #(when (fn? on-change) (on-change %))
        component-props (merge props
                               {:on-change handle-change})]
    [:div {:class-name (ui/get-class-name {"animation-form-fields" true
                                           class-name              (some? class-name)})}
     [choose-character {:on-change     handle-change
                        :default-value data}]
     [:div.additional-fields
      (when (:flip options) [flip-component component-props])
      (when (:scale options) [scale-component component-props])]]))
