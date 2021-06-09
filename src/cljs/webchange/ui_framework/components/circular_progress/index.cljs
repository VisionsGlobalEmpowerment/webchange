(ns webchange.ui-framework.components.circular-progress.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color]
    :or   {color "primary"}}]
  [:progress {:class-name (get-class-name (cond-> (-> {"wc-circular-progress" true}
                                                      (assoc (str "color-" color) true))
                                                  (some? class-name) (assoc class-name true)))}])
