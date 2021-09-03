(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-skip
  (:require
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn skip-unit
  [{:keys [effect-name class-name]}]
  [:div {:class-name (get-class-name (merge class-name
                                            {"effect-unit" true}))}
   [icon {:icon       "effect"
          :class-name "effect-icon"}]
   effect-name])
