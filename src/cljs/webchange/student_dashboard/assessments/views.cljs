(ns webchange.student-dashboard.assessments.views
  (:require
    [webchange.student-dashboard.assessments.views-assessments-list :refer [assessments-list]]
    [webchange.student-dashboard.common.block-header.views :refer [block-header]]
    [webchange.student-dashboard.common.icons.views :as icons]))

(defn assessments-block
  [params]
  [:div
   [block-header {:icon icons/mark-done
                  :text "Assessment"}]
   [assessments-list params]])
