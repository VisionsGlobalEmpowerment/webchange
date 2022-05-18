(ns webchange.admin.pages.class-students.views
  (:require
    [re-frame.core :as re-frame]
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

(defn- header
  []
  (let [{class-name :name stats :stats} @(re-frame/subscribe [::state/class])
        {course-name :name} @(re-frame/subscribe [::state/course])
        handle-add-click #(re-frame/dispatch [::state/add-student])]
    [page/header {:title   class-name
                  :icon    "school"
                  :actions [ui/icon-button {:icon     "add"
                                            :on-click handle-add-click}
                            "Add Student to Class"]}
     [:p (str (:students stats) " Students")]
     [:p course-name]
     [lesson-picker]
     [level-picker]]))

(defn- activity-card
  [{:keys [name preview] :as props}]
  ;(print "activity-card" props)
  [:div.activity-card
   [:div.preview
    [:img {:src preview}]]
   [:div.name name]])

(defn- progress-card
  [{:keys [completed? last-played total-time] :as props}]
  ;(print "progress-card" props)
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
  [{:keys [id name code] :as props}]
  ;(print "user-card" props)
  [:div.user-card {:on-click #(re-frame/dispatch [::state/open-student id])}
   [ui/avatar {:class-name "user-avatar"}]
   [:div.user-data
    [:div.name name]
    [:div.code "code: " code]]])

(defn- content
  []
  (let [students-data @(re-frame/subscribe [::state/students-data])
        {:keys [activities]} @(re-frame/subscribe [::state/lesson-data])
        activities-number (count activities)]
    [page/main-content {:title "Students"}
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
           [progress-card {:completed? true
                           :last-played "21/04/2022"
                           :total-time "2m : 23s"} #_activity-data])])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--class-students"}
     [header]
     [content]]))
