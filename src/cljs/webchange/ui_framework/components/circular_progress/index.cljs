(ns webchange.ui-framework.components.circular-progress.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color]
    :or   {color "primary"}}]
  [:progress {:class-name (get-class-name {"wc-circular-progress" true
                                           (str "color-" color)   true
                                           class-name             (some? class-name)})}])
