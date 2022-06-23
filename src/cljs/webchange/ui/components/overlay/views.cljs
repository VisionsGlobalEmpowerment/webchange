(ns webchange.ui.components.overlay.views
  (:require
    [webchange.ui.components.progress.views :refer [circular-progress]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn focus-overlay
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"bbs--focus-overlay" true
                                      class-name           (some? class-name)})}])

(defn loading-overlay
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"bbs--loading-overlay" true
                                      class-name             (some? class-name)})}
   [circular-progress {:class-name "bbs--loading-overlay--progress"}]])
