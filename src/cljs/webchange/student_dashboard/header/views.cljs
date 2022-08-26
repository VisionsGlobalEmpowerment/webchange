(ns webchange.student-dashboard.header.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn header
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"header-wrapper" true
                                      class-name       (some? class-name)})}
   [:div.header
    [:div.header-background]
    (into [:div.header-content]
          (-> (r/current-component)
              (r/children)))]])
