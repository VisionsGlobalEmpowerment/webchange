(ns webchange.ui-framework.components.icon-button.index
  (:require
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color icon disabled? on-click size]
    :or   {disabled? false
           color     "default"
           on-click  #()
           size      "default"}}]
  [:button {:class-name (get-class-name (-> {"wc-icon-button" true}
                                            (assoc class-name (some? class-name))
                                            (assoc color true)
                                            (assoc icon true)
                                            (assoc size true)))
            :disabled   disabled?
            :on-click   on-click}
   [icon/component {:icon icon}]])
