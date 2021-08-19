(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-animation
  (:require
    [webchange.ui-framework.components.index :refer [icon menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn animation-unit
  [{:keys [animation-name animation-object class-name]}]
  [:div {:class-name (get-class-name (merge class-name
                                            {"effect-unit" true}))}
   [icon {:icon       (if (some? animation-name)
                        "animation-add"
                        "animation-remove")
          :class-name "effect-icon"}]
   (str animation-object ": " (or animation-name "Reset animation"))])
