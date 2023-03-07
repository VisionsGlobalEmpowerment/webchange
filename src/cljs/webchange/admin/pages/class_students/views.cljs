(ns webchange.admin.pages.class-students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-students.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn- lesson-picker
  []
  (let [current-lesson @(re-frame/subscribe [::state/current-lesson])
        lesson-options @(re-frame/subscribe [::state/lesson-options])
        on-change #(re-frame/dispatch [::state/select-lesson %])]
    [ui/select {:label     "Lesson"
                :value     current-lesson
                :options   lesson-options
                :type      "int"
                :on-change on-change}]))

(defn- level-picker
  []
  (let [current-level @(re-frame/subscribe [::state/current-level])
        level-options @(re-frame/subscribe [::state/level-options])
        on-change #(re-frame/dispatch [::state/select-level %])]
    [ui/select {:label     "Level"
                :value     current-level
                :options   level-options
                :type      "int"
                :on-change on-change}]))

(defn- progress-list-item
  [{:keys [id access-code name progress]}]
  (let [handle-click #(re-frame/dispatch [::state/open-student-profile-page id])]
    [ui/list-item {:avatar           nil
                   :name             name
                   :description      access-code
                   :on-click         handle-click
                   :class-name--name "student-name"}
     (for [{:keys [last-played score time-spent unique-id]} progress]
       ^{:key unique-id}
       [ui/complete-progress {:value   score
                              :caption last-played
                              :text    time-spent}])]))

(defn- progress-list
  []
  (let [{:keys [students]} @(re-frame/subscribe [::state/progress-data])]
    [ui/list {:class-name "progress-list"}
     (for [{:keys [user-id] :as student-data} students]
       ^{:key user-id}
       [progress-list-item student-data])]))

;; header

(defn- activities-list-item
  [{:keys [name preview category]}]
  [:div.activity-card
   [ui/image {:src        preview
              :class-name "preview"}]
   [:div.name name
    [:div.tag category]]])

(defn- activities-list
  []
  (let [{:keys [activities]} @(re-frame/subscribe [::state/progress-data])]
    [ui/list {}
     [ui/list-item {:avatar           nil
                    :name             ""
                    :class-name       (get-class-name {"activities-row"       true
                                                       "activities-row-short" (-> (count activities) (< 4))})
                    :class-name--name "student-name"}
      (for [{:keys [unique-id] :as activity-data} activities]
        ^{:key unique-id}
        [activities-list-item activity-data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [{class-name :name stats :stats} @(re-frame/subscribe [::state/class])
          {course-name :name} @(re-frame/subscribe [::state/course-data])
          readonly? @(re-frame/subscribe [::state/readonly?])
          handle-add-click #(re-frame/dispatch [::state/open-add-students-page])]
      [page/single-page {:class-name "page--class-students"
                         :header     {:title    class-name
                                      :icon     "classes"
                                      :stats    [{:icon    "students"
                                                  :counter (:students stats)
                                                  :label   "Students"}]
                                      :info     [{:key   "Course Name"
                                                  :value (or course-name "")}]
                                      :controls [[level-picker]
                                                 [lesson-picker]]
                                      :actions  (when-not readonly?
                                                  [{:text     "Add Student"
                                                    :icon     "plus"
                                                    :on-click handle-add-click}])}}
       [activities-list]
       [progress-list]])))
