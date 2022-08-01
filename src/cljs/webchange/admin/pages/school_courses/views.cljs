(ns webchange.admin.pages.school-courses.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.school-courses.state :as state]
    [webchange.admin.widgets.page.views-dep :as page]
    [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [school-name @(re-frame/subscribe [::state/school-name])
        handle-add-click #(re-frame/dispatch [::state/show-assign-course-modal])]
    [page/_header {:title   school-name
                  :icon    "school"
                  :actions [c/icon-button {:icon     "add"
                                           :on-click handle-add-click}
                            "Assign Course"]}]))

(defn- list-item
  [{:keys [id stats] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-course id])
        handle-remove-click #(re-frame/dispatch [::state/remove-course id])
        handle-copy-click #(re-frame/dispatch [::state/copy-course id])]
    [l/list-item (merge props
                        {:actions [:<>
                                   [c/icon-button {:icon     "copy"
                                                   :title    "Copy"
                                                   :variant  "light"
                                                   :on-click handle-copy-click}]
                                   [c/icon-button {:icon     "remove"
                                                   :title    "Remove"
                                                   :variant  "light"
                                                   :on-click handle-remove-click}]
                                   [c/icon-button {:icon     "edit"
                                                   :title    "Edit"
                                                   :variant  "light"
                                                   :on-click handle-edit-click}]]})]))

(defn- content
  []
  (let [courses-data @(re-frame/subscribe [::state/courses])]
    [page/main-content {:title "Courses"}
     [l/list {:class-name "courses-list"}
      (for [{:keys [id] :as course-data} courses-data]
        ^{:key id}
        [list-item course-data])]]))

(defn assign-course-modal
  []
  (r/with-let [data (r/atom {})]
    (let [open? @(re-frame/subscribe [::state/show-assign-course-modal?])
          _ (js/console.log open?)
          course-options @(re-frame/subscribe [::state/course-options])
          errors @(re-frame/subscribe [::state/errors])

          handle-close #(re-frame/dispatch [::state/close-assign-course-modal])
          handle-save-click #(re-frame/dispatch [::state/assign-course @data])]
      [c/dialog {:open?              open?
                 :on-close           handle-close
                 :title              "Assign Course"
                 :class-name-content "assign-course-modal"}
       [c/select {:placeholder "Select course"
                  :value       (:course-id @data)
                  :type        "int"
                  :options     course-options
                  :on-change   #(swap! data assoc :course-id %)
                  :class-name  "type-input"
                  :variant     "outlined"
                  :error       (when (:course-id errors)
                                 (:course-id errors))}]
       [c/button {:on-click handle-save-click}
        "Save"]])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--school-courses"}
     [header]
     [content]
     [assign-course-modal]]))
