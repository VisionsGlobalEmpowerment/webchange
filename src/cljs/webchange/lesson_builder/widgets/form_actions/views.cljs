(ns webchange.lesson-builder.widgets.form-actions.views
  (:require
    [webchange.ui.index :as ui]))

(defn form-actions
  [{:keys [class-name on-cancel on-save save-props]
    :or   {save-props {}}}]
  [:div {:class-name (ui/get-class-name {"widget--form-actions" true
                                         class-name             (some? class-name)})}
   (when (fn? on-cancel)
     [ui/button {:color    "blue-1"
                 :on-click on-cancel}
      "Cancel"])
   (when (fn? on-save)
     [ui/button (merge save-props
                       {:on-click on-save})
      "Save"])])
