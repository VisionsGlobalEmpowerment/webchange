(ns webchange.ui.components.progress.views
  (:require
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn circular-progress
  [{:keys [class-name color]}]
  [:progress {:class-name (get-class-name {"bbs--circular-progress"                     true
                                           (str "bbs--circular-progress--color-" color) (some? color)
                                           class-name                                   (some? class-name)})}])
