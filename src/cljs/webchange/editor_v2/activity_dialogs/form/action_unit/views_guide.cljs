(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-guide
  (:require
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn guide-unit
  [{:keys [effect-name class-name]}]
  [:div {:class-name (get-class-name (merge class-name
                                            {"effect-unit" true}))}
   [icon {:icon       "effect"
          :class-name "effect-icon"}]
   effect-name])
