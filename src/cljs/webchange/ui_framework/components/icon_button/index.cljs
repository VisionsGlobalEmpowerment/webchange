(ns webchange.ui-framework.components.icon-button.index
  (:require
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name icon disabled? on-click]
    :or   {disabled? false
           on-click  #()}}]
  [:button {:class-name (get-class-name (-> {"wc-icon-button" true}
                                            (assoc class-name (some? class-name))
                                            (assoc icon true)))
            :disabled   disabled?
            :on-click   on-click}
   [icon/component {:icon icon}]])
