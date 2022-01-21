(ns webchange.parent-dashboard.students-list.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent-dashboard.layout.views :refer [layout]]
    [webchange.parent-dashboard.students-list.state :as state]))

(defn- student-card
  [{:keys [name level lesson id img]
    :or   {img "/images/parent_dashboard/user_placeholder.png"}}]
  (let [handle-play-click (fn []
                            (print "Play" id))
        handle-delete-click (fn []
                              (print "Delete" id))]
    [:div.student-card
     [:div.top-side
      [:img {:src img}]
      [:button {:class-name "delete-button"
                :title      "Delete student"
                :on-click   handle-delete-click}]]
     [:div.bottom-side
      [:div.name name]
      [:div.progress
       (str "Progress: Level " level " - Lesson " lesson)]
      [:button {:class-name "play-button"
                :on-click   handle-play-click}
       "Play"]]]))

(defn- students-list
  []
  (let [students @(re-frame/subscribe [::state/students-list])]
    [:div.students-list
     (for [{:keys [id] :as student} students]
       ^{:key id}
       [student-card student])]))

(defn students-list-page
  []
  [layout {:title   "Current students"
           :actions [[:button "Add a student"]]}
   [students-list]])
