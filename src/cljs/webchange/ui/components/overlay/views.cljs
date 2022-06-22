(ns webchange.ui.components.overlay.views
  (:require
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn focus-overlay
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"bbs--focus-overlay" true
                                      class-name           (some? class-name)})}])
