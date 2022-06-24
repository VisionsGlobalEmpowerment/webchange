(ns webchange.ui.components.avatar.views
  (:require
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn avatar
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"bbs--avatar" true
                                      class-name  (some? class-name)})}
   [system-icon {:icon       "account"
                 :class-name "bbs--avatar--default-icon"}]])
