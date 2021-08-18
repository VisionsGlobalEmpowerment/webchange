(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-animation
  (:require
    [webchange.ui-framework.components.index :refer [icon menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn animation-unit
  [{:keys [animation-name animation-object class-name] :as props}]
  (print "animation-unit" props)
  [:div {:class-name (get-class-name (merge class-name
                                            {"effect-unit" true}))}
   [icon {:icon       "animation-add"
          :class-name "effect-icon"}]
   (str animation-object ": " animation-name)])
