(ns webchange.admin.pages.student-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.student-profile.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- level-picker
  []
  (let [current-level @(re-frame/subscribe [::state/current-level])
        level-options @(re-frame/subscribe [::state/level-options])
        on-change #(re-frame/dispatch [::state/select-level %])]
    [:div.level-picker
     [ui/select {:value     current-level
                 :options   level-options
                 :type      "int"
                 :on-change on-change}]]))

(defn- header
  []
  (let [{student-name :name} @(re-frame/subscribe [::state/student])
        {:keys [started-at last-login activity-progress cumulative-time]} @(re-frame/subscribe [::state/course-stats])]
    [page/header {:title      student-name
                  :avatar     ""
                  :class-name "student-profile-header"}
     [page/header-content-group {:title "Program start"} [:span started-at]]
     [:hr]
     [page/header-content-group {:title "Latest login at"} [:span last-login]]
     [:hr]
     [page/header-content-group {:title "Activities completed"} [:span activity-progress]]
     [:hr]
     [page/header-content-group {:title "Total played time"} [:span cumulative-time]]]))

(defn- progress-card
  [{:keys [completed? last-played name total-time]}]
  [:div {:class-name (ui/get-class-name {"progress-card" true
                                         "completed"     completed?})}
   (if completed?
     [:<>
      [:div.data
       [:span.name name]
       [:span.last-played last-played]
       [:span.total-time total-time]]
      [ui/icon {:icon "check"}]]
     [:span.name name])])

(defn- lesson-card
  [{:keys [name]}]
  [:div.lesson-card
   name])

(defn- list-item
  [{:keys [activities length] :as props}]
  [:<>
   [lesson-card props]
   (for [{:keys [id] :as props} activities]
     ^{:key id}
     [progress-card props])
   (for [idx (range (- length (count activities)))]
     ^{:key idx}
     [:div])])

(defn- table-actions
  []
  [:div.table-actions
   [ui/icon-button {:icon       "trophy"
                    :variant    "light"
                    :direction  "revert"
                    :class-name "complete-button"}
    "Complete Class"]])

(defn- progress-table
  []
  (let [{:keys [data max-activities]} @(re-frame/subscribe [::state/lessons-data])]
    [:div {:class-name (ui/get-class-name {"progress-table"                      true
                                           (str "columns-" (inc max-activities)) true})}

     [level-picker]
     [table-actions]
     (for [{:keys [id] :as lesson-data} data]
       ^{:key id}
       [list-item (merge lesson-data
                         {:length max-activities})])]))

(defn- content
  []
  (let [{class-name :name} @(re-frame/subscribe [::state/class])
        {course-name :name} @(re-frame/subscribe [::state/course])]
    [page/main-content {:title class-name}
     #_[:p course-name]
     [progress-table]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--student-profile"}
     [header]
     [content]]))
