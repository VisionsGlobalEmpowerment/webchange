(ns webchange.dashboard.classes.class-profile.views-profile-table
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.common.core :as common]
    [webchange.routes :refer [redirect-to]]))

(def columns {:first-name "FirstName"
              :last-name "Last Name"
              :started-at "Program start date"
              :latest-activity "Latest activity"
              :last-login "Last log-in date"
              :cumulative-score "Cumulative Activity Score"
              :activity-progress "Activity Progress"
              :cumulative-time "Progress towards Time Goal"})

(def defaults {:activities 15
               :time-goal 3600})

(defn latest-activity
  [data]
  (str (:lesson data) " - " (:id data)))

(defn float->percentage
  [float]
  (-> float (* 100) js/Math.round (str "%")))

(defn cumulative-score
  [data]
  (if data
    (let [correct (:correct data)
          incorrect (:incorrect data)
          overall (+ correct incorrect)]
      (-> correct (/ overall) float->percentage))
    "-"))

(defn activity-progress
  [data options]
  (if data
    (let [overall (:activities options)]
      (-> data (/ overall) float->percentage))
    "-"))

(defn cumulative-time
  [data options]
  (if data
    (let [overall (:time-goal options)
          elapsed (-> data (/ 1000) js/Math.round)]
      (if (> elapsed overall)
        (-> elapsed (/ overall) float->percentage)
        "100%"))
    "-"))

(defn prepare-student
  [options]
  (fn [student]
    {:id (-> student :student :id)
     :first-name (-> student :user :first-name)
     :last-name (-> student :user :last-name)
     :started-at (-> student :data :started-at common/format-date-string)
     :latest-activity (-> student :data :latest-activity latest-activity)
     :last-login (-> student :data :last-login common/format-date-string)
     :cumulative-score (-> student :data :cumulative-score cumulative-score)
     :activity-progress (-> student :data :activity-progress (activity-progress options))
     :cumulative-time (-> student :data :cumulative-time (cumulative-time options))}))

(defn profile-table
  []
  (let [is-loading? @(re-frame/subscribe [::classes-subs/class-profile-loading])
        {stats :stats class-id :class-id} @(re-frame/subscribe [::classes-subs/class-profile])
        students (->> stats (map (prepare-student defaults)))]
    (if is-loading?
      [ui/linear-progress]
      [ui/table
       [ui/table-head
        [ui/table-row
         (for [column-key (keys columns)]
           ^{:key column-key}
           [ui/table-cell
            (get columns column-key)])]]
       [ui/table-body
        (for [student students]
          ^{:key (:id student)}
          [ui/table-row {:hover true}
           (for [column-key (keys columns)]
             ^{:key (str (:id student) "_" column-key)}
             [ui/table-cell {:on-click #(redirect-to :dashboard-student-profile :class-id class-id :student-id (:id student)) }
              (get student column-key)])])]]
      )))
