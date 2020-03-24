(ns webchange.student-dashboard.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.events :as sde]
    [webchange.student-dashboard.subs :as sds]
    [webchange.student-dashboard.assessments.views :refer [assessments-block]]
    [webchange.student-dashboard.history.views :refer [history]]
    [webchange.student-dashboard.next-activity.views :refer [next-activity-block]]
    [webchange.student-dashboard.toolbar.views :refer [toolbar]]))

(defn- get-styles
  []
  {:page-wrapper {:height     "100%"
                  :text-align "center"}
   :main-content {:padding "54px 72px"}
   :left-side    {:padding-right "60px"}
   :right-side   {:padding-left "60px"}})

(defn- loading-bar
  []
  [ui/circular-progress])

(defn- dashboard-view
  [{:keys [next-activity finished-activities assessments handle-activity-click]}]
  (let [styles (get-styles)]
    [ui/grid {:container true
              :justify   "space-between"
              :style     (:main-content styles)}
     [ui/grid {:item  true
               :xs    8
               :style (:left-side styles)}
      [next-activity-block (merge next-activity
                                  {:on-click handle-activity-click})]
      [assessments-block {:data      assessments
                          :max-count 1
                          :on-click  handle-activity-click}]]
     [ui/grid {:item  true
               :xs    4
               :style (:right-side styles)}
      [history {:data      finished-activities
                :max-count 5
                :on-click  handle-activity-click}]]]))

(defn student-dashboard-page
  []
  (let [loading? @(re-frame/subscribe [::sds/progress-loading])
        next-activity @(re-frame/subscribe [::sds/next-activity])
        finished-activities @(re-frame/subscribe [::sds/finished-activities])
        assessments @(re-frame/subscribe [::sds/assessments])
        handle-activity-click (fn [activity] (re-frame/dispatch [::sde/open-activity activity]))]
    (let [styles (get-styles)]
      [ui/grid {:container true
                :style     (:page-wrapper styles)}
       [ui/grid {:item true
                 :xs   12}
        [toolbar]]
       [ui/grid {:item true
                 :xs   12}
        (if loading?
          [loading-bar]
          [dashboard-view {:next-activity         next-activity
                           :finished-activities   finished-activities
                           :assessments           assessments
                           :handle-activity-click handle-activity-click}])]])))

(defn student-dashboard-finished-page
  []
  (let [loading? @(re-frame/subscribe [::sds/progress-loading])
        finished-activities @(re-frame/subscribe [::sds/finished-activities])
        handle-activity-click (fn [activity] (re-frame/dispatch [::sde/open-activity activity]))]
    (let [styles (get-styles)]
      [ui/grid {:container true
                :style     (:page-wrapper styles)}
       [ui/grid {:item true
                 :xs   12}
        [toolbar]]
       [ui/grid {:item true
                 :xs   12
                 :style {:height "100%"}}
        (if loading?
          [loading-bar]
          [history {:data      finished-activities
                    :max-count 3
                    :on-click  handle-activity-click
                    :style     (:main-content styles)}])]])))
