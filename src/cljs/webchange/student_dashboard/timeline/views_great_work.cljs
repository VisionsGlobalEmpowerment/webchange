(ns webchange.student-dashboard.timeline.views-great-work
  (:require
    [webchange.ui-framework.components.utils :as ui]))

(defn great-work-button
  [{:keys [lang]}]
  [:div {:class-name (ui/get-class-name {"great-work-button" true
                                         (str "lang-" lang)  (some? lang)})}])
