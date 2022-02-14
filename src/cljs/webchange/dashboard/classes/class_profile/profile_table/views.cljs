(ns webchange.dashboard.classes.class-profile.profile-table.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.class-profile.profile-table.state :as state]
    [webchange.routes :refer [redirect-to]]))

(def columns {:first-name        "FirstName"
              :last-name         "Last Name"
              :started-at        "Program start date"
              :latest-activity   "Latest activity"
              :last-login        "Last log-in date"
              :cumulative-score  "Cumulative Activity Score"
              :activity-progress "Activity Progress"
              :cumulative-time   "Progress towards Time Goal"})

(defn profile-table
  [{:keys [class-id course-slug]}]
  (let [handle-row-click #(re-frame/dispatch [::state/open-student-profile %])]
    (if (and (some? class-id)
             (some? course-slug))
      (do (re-frame/dispatch [::state/init class-id course-slug])
          (let [students @(re-frame/subscribe [::state/students])]
            [ui/table {:class-name "profile-table"}
             [ui/table-head
              [ui/table-row
               (for [column-key (keys columns)]
                 ^{:key column-key}
                 [ui/table-cell
                  (get columns column-key)])]]
             [ui/table-body
              (for [student students]
                ^{:key (:id student)}
                [ui/table-row {:hover    true
                               :on-click #(handle-row-click (:id student))}
                 (for [column-key (keys columns)]
                   ^{:key (str (:id student) "_" column-key)}
                   [ui/table-cell
                    (get student column-key)])])]])))))
