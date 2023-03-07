(ns webchange.admin.pages.school-profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.counter.views :refer [counter]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.school-form.views :refer [edit-school-form]]
    [webchange.admin.pages.school-profile.state :as state]))

(defn- school-counter
  []
  (let [{:keys [stats]} @(re-frame/subscribe [::state/school-data])
        readonly? @(re-frame/subscribe [::state/readonly?])
        add-button-props {:color      "blue-1"
                          :chip       "plus"
                          :chip-color "yellow-1"}]
    [counter {:data [{:text    "Classes"
                      :icon    "classes"
                      :counter (:classes stats)
                      :actions [{:text     "Manage Classes"
                                 :on-click #(re-frame/dispatch [::state/open-classes])}
                                (when-not readonly?
                                  (merge add-button-props
                                         {:text     "Add Class"
                                          :on-click #(re-frame/dispatch [::state/open-add-class])}))]}
                     {:text    "Teachers"
                      :icon    "teachers"
                      :counter (:teachers stats)
                      :actions [{:text     "Manage Teachers"
                                 :on-click #(re-frame/dispatch [::state/open-teachers])}
                                (when-not readonly?
                                  (merge add-button-props
                                         {:text     "Add Teacher"
                                          :on-click #(re-frame/dispatch [::state/open-add-teacher])}))]}
                     {:text    "Students"
                      :icon    "students"
                      :counter (:students stats)
                      :actions [{:text     "Manage Students"
                                 :on-click #(re-frame/dispatch [::state/open-students])}
                                (when-not readonly?
                                  (merge add-button-props
                                         {:text     "Add Student"
                                          :on-click #(re-frame/dispatch [::state/open-add-student])}))]}
                     {:text       "Courses"
                      :icon       "courses"
                      :counter    (:courses stats)
                      :background "green-2"
                      :actions    [{:text     "Manage Courses"
                                    :on-click #(re-frame/dispatch [::state/open-courses])}]}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"
               :icon  "statistics"}
   [no-data]])

(defn- side-bar
  [{:keys [school-id]}]
  (let [school-form-editable? @(re-frame/subscribe [::state/school-form-editable?])
        readonly? @(re-frame/subscribe [::state/readonly?])
        handle-edit-click #(re-frame/dispatch [::state/set-school-form-editable (not school-form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/handle-save %])
        handle-archive #(re-frame/dispatch [::state/open-schools-list])
        handle-cancel-click #(re-frame/dispatch [::state/handle-cancel])]
    [page/side-bar {:title    "School Info"
                    :icon     "info"
                    :focused? school-form-editable?
                    :actions  (when-not readonly?
                                (cond-> []
                                        school-form-editable? (conj {:icon     "close"
                                                                     :on-click handle-cancel-click})
                                        (not school-form-editable?) (conj {:icon     "edit"
                                                                           :on-click handle-edit-click})))}
     [edit-school-form {:school-id  school-id
                        :editable?  school-form-editable?
                        :on-save    handle-data-save
                        :on-archive handle-archive
                        :on-cancel  handle-cancel-click}]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (r/with-let []
      (let [school-name @(re-frame/subscribe [::state/school-name])]
        [page/page
         [page/content {:title school-name
                        :icon  "school"}
          [school-counter]
          [statistics]]
         [side-bar {:school-id school-id}]])
      (finally
        (re-frame/dispatch [::state/reset props])))))
