(ns webchange.student-dashboard.history.views
  (:require
    [webchange.student-dashboard.common.block-header.views :refer [block-header]]
    [webchange.student-dashboard.common.icons.views :as icons]
    [webchange.student-dashboard.history.views-history-list :refer [history-list]]))

(defn history
  [{:keys [style] :as params}]
  [:div {:style (or style {})}
   [block-header {:icon icons/history
                  :text "History"}]
   [history-list (-> params
                     (dissoc :style))]])
