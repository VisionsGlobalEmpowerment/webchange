(ns webchange.student-dashboard.next-activity.views
  (:require
    [webchange.student-dashboard.common.block-header.views :refer [block-header]]
    [webchange.student-dashboard.common.icons.views :as icons]
    [webchange.student-dashboard.next-activity.views-activity-placeholder :refer [activity-placeholder]]))

(defn prepare-data
  [{:keys [image] :as data}]
  (-> data
      (assoc :image {:png image})
      ;; ToDo: remove when source data fixed
      (assoc :description "Cornell University Library promotes a culture of broad inquiry and supports the University’s mission to discover, preserve, and disseminate knowledge and creative expression.")))

(defn next-activity-block
  [activity]
  [:div
   [block-header {:icon icons/play
                  :text "Next Scene"}]
   [activity-placeholder (prepare-data activity)]])
