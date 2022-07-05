(ns webchange.admin.pages.student-profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.student-profile.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.student-form.views :refer [edit-student-form]]
    [webchange.admin.widgets.student-progress-complete.views :refer [student-progress-complete]]
    [webchange.ui.index :as ui]
    [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [{student-name :name} @(re-frame/subscribe [::state/student-data])
        {:keys [started-at last-login activity-progress cumulative-time]} @(re-frame/subscribe [::state/course-statistics])]
    [page/header {:avatar ""
                  :title  student-name
                  :info   [{:key   "Program Start Date"
                            :value (or started-at "")
                            :icon  "school"}
                           {:key   "Last Login Date"
                            :value (or last-login "")
                            :icon  "students"}
                           {:key        "Activities Completed"
                            :value      (or activity-progress "")
                            :icon       "games"
                            :icon-color "blue-2"}
                           {:key        "Total Played Time"
                            :value      (or cumulative-time "")
                            :icon       "play"
                            :icon-color "blue-2"}]}]))

(defn- table-actions
  []
  (let [handle-complete-click #(re-frame/dispatch [::state/open-complete-class])]
    [:div.table-actions
     [c/icon-button {:icon       "trophy"
                     :variant    "light"
                     :direction  "revert"
                     :class-name "complete-button"
                     :on-click   handle-complete-click}
      "Complete Class"]]))

(defn- progress-list-item
  [{:keys [name progress]}]
  [ui/list-item {:name name}
   (for [{:keys [name score unique-id]} progress]
     ^{:key unique-id}
     [ui/complete-progress {:value   score
                            :caption name}])])

(defn- progress-list
  [{:keys [class-name]}]
  (let [{:keys [lessons]} @(re-frame/subscribe [::state/progress-data])]
    [ui/list {:class-name class-name}
     (for [{:keys [user-id] :as lesson-data} lessons]
       ^{:key user-id}
       [progress-list-item lesson-data])]))

(defn- progress-table
  []
  (let [current-level @(re-frame/subscribe [::state/current-level])
        level-options @(re-frame/subscribe [::state/level-options])
        on-change #(re-frame/dispatch [::state/select-level %])
        handle-complete-click #(re-frame/dispatch [::state/open-complete-class])]
    [:div.progress-table
     [:div.progress-table--header
      [ui/select {:value      current-level
                  :options    level-options
                  :type       "int"
                  :on-change  on-change
                  :class-name "level-picker"}]
      [:div.title "Activities"]
      [ui/button {:icon     "cup"
                  :shape    "rounded"
                  :color    "transparent"
                  :on-click handle-complete-click}
       "Complete Class"]]
     [progress-list {:class-name "progress-table--list"}]]))

(defn- content
  []
  (let [{class-name :name} @(re-frame/subscribe [::state/class-data])
        {course-name :name} @(re-frame/subscribe [::state/course-data])]
    [page/content {:title    class-name
                   :subtitle "Started one day"
                   :icon     "classes"
                   :header   [:div.course-name
                              [ui/navigation-icon {:icon "courses"}]
                              [:span.prefix "Course: "]
                              [:span (or course-name "Not Assigned")]]
                   :tabs     [{:title     "Course View"
                               :component [progress-table]}
                              {:title     "Student History"
                               :component [:div "Student History"]}]}]))

(defn- side-bar-complete-class
  [{:keys [student-id]}]
  (let [handle-close-click #(re-frame/dispatch [::state/open-student-profile])
        handle-save #(re-frame/dispatch [::state/open-student-profile])]
    [page/side-bar {:title   "Complete Class"
                    :actions [c/icon-button {:icon     "close"
                                             :variant  "light"
                                             :on-click handle-close-click}]}
     [student-progress-complete {:student-id student-id
                                 :on-save    handle-save}]]))

(defn- side-bar-student-profile
  [{:keys [school-id student-id]}]
  (r/with-let [form-editable? (r/atom false)
               handle-edit-click #(reset! form-editable? true)
               handle-cancel-click #(reset! form-editable? false)
               handle-remove-from-class #(re-frame/dispatch [::state/open-class-profile-page])
               handle-save-click #(do (reset! form-editable? false)
                                      (re-frame/dispatch [::state/update-student-data student-id]))]
    [page/side-bar {:title    "Student Account"
                    :icon     "teachers"
                    :focused? @form-editable?
                    :actions  (cond-> []
                                      (not @form-editable?) (conj {:icon     "edit"
                                                                   :variant  "light"
                                                                   :on-click handle-edit-click}))}
     [edit-student-form {:student-id           student-id
                         :school-id            school-id
                         :editable?            @form-editable?
                         :on-save              handle-save-click
                         :on-cancel            handle-cancel-click
                         :on-remove-from-class handle-remove-from-class
                         :actions              {:remove-account    false
                                                :remove-from-class true}}]]))

(defn- side-bar
  [props]
  (let [side-bar-content @(re-frame/subscribe [::state/side-bar])]
    (case side-bar-content
      ;:complete-class [side-bar-complete-class props]
      :student-profile [side-bar-student-profile props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    ;[page/page {:class-name "page--student-profile"}
    ; [header]
    ;
    ; [side-bar props]]
    [page/page {:class-name "page--student-profile"}
     [header]
     [content]
     [side-bar props]]))
