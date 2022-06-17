(ns webchange.admin.pages.class-students.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.class-students.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))


(defn- lesson-picker
  []
  (let [current-lesson @(re-frame/subscribe [::state/current-lesson])
        lesson-options @(re-frame/subscribe [::state/lesson-options])
        on-change #(re-frame/dispatch [::state/select-lesson %])]
    [ui/select {:value     current-lesson
                :options   lesson-options
                :type      "int"
                :on-change on-change}]))

(defn- level-picker
  []
  (let [current-level @(re-frame/subscribe [::state/current-level])
        level-options @(re-frame/subscribe [::state/level-options])
        on-change #(re-frame/dispatch [::state/select-level %])]
    [ui/select {:value     current-level
                :options   level-options
                :type      "int"
                :on-change on-change}]))

(defn- header-group
  [{:keys [class-name title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (ui/get-class-name {"header-group" true
                                                    class-name     (some? class-name)})}
              (when (some? title)
                [:label title])])))

(defn- header
  []
  (let [{class-name :name stats :stats} @(re-frame/subscribe [::state/class])
        {course-name :name} @(re-frame/subscribe [::state/course])
        handle-add-click #(re-frame/dispatch [::state/add-student])]
    [page/_header {:title      class-name
                  :icon       "classes"
                  :class-name "class-students-header"
                  :actions    [ui/icon-button {:icon     "add"
                                               :on-click handle-add-click}
                               "Add Student to Class"]}
     [page/_header-content-group {:class-name "students-stats"}
      [ui/icon {:icon "students"}]
      [:span (:students stats) " Students"]]
     [:hr]
     [page/_header-content-group {:title "Course Name"} [:span course-name]]
     [:hr]
     [page/_header-content-group {:title "Lesson"} [lesson-picker]]
     [:hr]
     [page/_header-content-group {:title "Level"} [level-picker]]]))

(defn- activity-card
  [{:keys [name preview]}]
  [:div.activity-card
   [ui/image {:src        preview
              :class-name "preview"}]
   [:div.name name]])

(defn- progress-card
  [{:keys [completed? last-played total-time]}]
  [:div {:class-name (ui/get-class-name {"progress-card" true
                                         "completed"     completed?})}
   (if completed?
     [:<>
      [:div.data
       [:span.last-played last-played]
       [:span.total-time total-time]]
      [ui/icon {:icon "check"}]]
     [:span "Not Started"])])

(defn- user-card
  [{:keys [id name code]}]
  [:div {:class-name "user-card"
         :on-click   #(re-frame/dispatch [::state/open-student id])}
   [ui/avatar {:class-name "user-avatar"}]
   [:div.user-data
    [:div.name name]
    [:div.code "code: " code]]])

(defn- content
  []
  (let [students-data @(re-frame/subscribe [::state/students-data])
        {:keys [activities]} @(re-frame/subscribe [::state/lesson-data])
        activities-number (count activities)]
    [page/main-content
     [:div {:class-name (ui/get-class-name {"main-table"                             true
                                            (str "columns-" (inc activities-number)) true})}
      [:div]
      (for [{:keys [id] :as activity-data} activities]
        ^{:key (str "activity-" id)}
        [activity-card activity-data])
      (for [{:keys [id activities] :as student-data} students-data]
        ^{:key (str "student-" id)}
        [:<>
         {:key (str "student-card-" id)}
         [user-card student-data]
         (for [activity-data activities]
           ^{:key (str "student-progress-" (:id student-data) "-" (:id activity-data))}
           [progress-card activity-data])])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--class-students"}
     [header]
     [content]]))
