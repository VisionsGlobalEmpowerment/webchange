(ns webchange.ui.components.info.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn info
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name {"bbs--info" true
                                                 class-name        (some? class-name)})}
              [system-icon {:icon       "info"
                            :class-name "info-icon"}]])))
