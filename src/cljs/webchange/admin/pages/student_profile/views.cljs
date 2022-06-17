(ns webchange.admin.pages.student-profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.student-profile.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.student-form.views :refer [edit-student-form]]
    [webchange.admin.widgets.student-progress-complete.views :refer [student-progress-complete]]
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
    [page/_header {:title      student-name
                  :avatar     ""
                  :class-name "student-profile-header"}
     [page/_header-content-group {:title "Program start"} [:span started-at]]
     [:hr]
     [page/_header-content-group {:title "Latest login at"} [:span last-login]]
     [:hr]
     [page/_header-content-group {:title "Activities completed"} [:span activity-progress]]
     [:hr]
     [page/_header-content-group {:title "Total played time"} [:span cumulative-time]]]))

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
  (let [handle-complete-click #(re-frame/dispatch [::state/open-complete-class])]
    [:div.table-actions
     [ui/icon-button {:icon       "trophy"
                      :variant    "light"
                      :direction  "revert"
                      :class-name "complete-button"
                      :on-click   handle-complete-click}
      "Complete Class"]]))

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

(defn- course-name
  []
  (let [{:keys [name] :as course} @(re-frame/subscribe [::state/course])]
    [:div.course-name
     [ui/icon {:icon "presentation"}]
     [:span "Course: "]
     [:span (if (some? course) name "Not Assigned")]]))

(defn- view-switcher
  []
  [:div.view-switcher
   [ui/button {:class-name "active"} "Course View"]
   [:hr]
   [ui/button "Student History"]])

(defn- content
  []
  (let [{class-name :name} @(re-frame/subscribe [::state/class])]
    [page/main-content {:title   class-name
                        :icon    "classes"
                        :actions [:div.main-content-actions
                                  [course-name]
                                  [view-switcher]]}
     [progress-table]]))

(defn- side-bar-complete-class
  [{:keys [student-id]}]
  (let [handle-close-click #(re-frame/dispatch [::state/open-student-profile])
        handle-save #(re-frame/dispatch [::state/open-student-profile])]
    [page/side-bar {:title   "Complete Class"
                    :actions [ui/icon-button {:icon     "close"
                                              :variant  "light"
                                              :on-click handle-close-click}]}
     [student-progress-complete {:student-id student-id
                                 :on-save    handle-save}]]))

(defn- side-bar-student-profile
  [{:keys [school-id student-id]}]
  (r/with-let [form-editable? (r/atom false)
               handle-edit-click #(swap! form-editable? not)
               handle-save-click #(do (reset! form-editable? false)
                                      (re-frame/dispatch [::state/update-student-data student-id]))]
    [page/side-bar {:title   "Student Account"
                    :actions [ui/icon-button {:icon     (if @form-editable? "close" "edit")
                                              :variant  "light"
                                              :on-click handle-edit-click}]}
     [edit-student-form {:student-id student-id
                         :school-id  school-id
                         :editable?  @form-editable?
                         :on-save    handle-save-click}]]))

(defn- side-bar
  [props]
  (let [side-bar-content @(re-frame/subscribe [::state/side-bar])]
    (case side-bar-content
      :complete-class [side-bar-complete-class props]
      :student-profile [side-bar-student-profile props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--student-profile"}
     [header]
     [content]
     [side-bar props]]))
