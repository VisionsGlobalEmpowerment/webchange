(ns webchange.ui-framework.components.avatar.index
  (:require
    [webchange.ui-framework.components.icon.index :as ic]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"wc-avatar" true
                                      class-name  (some? class-name)})}
   [ic/component {:icon       "user"
                  :class-name "default-icon"}]])
