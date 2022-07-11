(ns webchange.ui.components.link.views
  (:require
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn link
  [{:keys [class-name href target]
    :or   {target "_blank"}}]
  (->> (r/current-component)
       (r/children)
       (into [:a {:href       href
                  :target     target
                  :class-name (get-class-name {"bbs--link" true
                                               class-name  (some? class-name)})}])))
