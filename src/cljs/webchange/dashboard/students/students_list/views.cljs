(ns webchange.dashboard.students.students-list.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.common.views :refer [content-page]]
    [webchange.dashboard.students.common.check-icon :refer [check-icon]]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.students-list.state :as state]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.common.map-students :refer [map-students-list]]
    [webchange.dashboard.students.students-list.utils :refer [filter-students-list]]
    [webchange.dashboard.students.students-list.views_list_filter :refer [students-list-filter]]
    [webchange.routes :refer [redirect-to]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(def no-defined-color "#c1c1c1")
(def avatar-male-color "#70adff")
(def avatar-female-color "#ff7070")
(def avatar-unknown-color no-defined-color)
(def styles
  {:content    {:title {:display     "inline-block"
                        :font-weight "bold"
                        :overflow    "hidden"}
                :text  {:display       "inline-block"
                        :overflow      "hidden"
                        :text-overflow "ellipsis"
                        :white-space   "nowrap"}}
   :table-cell {:padding     "4px 12px 4px 12px"
                :white-space "nowrap"}})

(defn translate
  [path]
  (get-in {:title       "Students"
           :add-student {:text "Add Student"}
           :items       {:name   {:title "Name"}
                         :age    {:title       "Age"
                                  :not-defined "---"}
                         :class  {:title "Class"}
                         :course {:title       "Course"
                                  :not-defined "Course is not defined"}
                         :tablet {:title "Tablet"}}
           :actions     {:edit    "Edit"
                         :profile "Profile"
                         :remove  "Remove"
                         :class   "Class"}}
          path))

(defn list-item-avatar
  [{:keys [first-name last-name gender]}]
  [ui/list-item-avatar
   [ui/avatar
    {:style {:background-color (case gender
                                 1 avatar-male-color
                                 2 avatar-female-color
                                 avatar-unknown-color)}}
    (get first-name 0)
    (get last-name 0)]])

(defn list-item
  [{:keys [on-edit-click on-profile-click on-remove-click]}
   {:keys [first-name last-name age gender class course tablet?] :as student}]
  [ui/table-row {:hover true}
   [ui/table-cell {:style (get-in styles [:table-cell])}
    [list-item-avatar
     {:first-name first-name
      :last-name  last-name
      :gender     gender}]]
   [ui/table-cell {:style (get-in styles [:table-cell])}
    [:span
     {:style (get-in styles [:content :text])}
     (str first-name " " last-name)]]
   [ui/table-cell {:style (get-in styles [:table-cell])}
    [:span
     {:style (merge (get-in styles [:content :text])
                    (if-not age {:color no-defined-color} {}))}
     (or age (translate [:items :age :not-defined]))]]
   [ui/table-cell {:style (get-in styles [:table-cell])}
    [:span
     {:style (get-in styles [:content :text])}
     class]]
   [ui/table-cell {:style (get-in styles [:table-cell])}
    [:span
     {:style (merge (get-in styles [:content :text])
                    (if-not course {:color no-defined-color} {}))}
     (or course (translate [:items :course :not-defined]))]]
   [ui/table-cell {:style (get-in styles [:table-cell])} [check-icon {:value tablet?}]]
   [ui/table-cell {:align "right"
                   :style (get-in styles [:table-cell])}
    [ui/tooltip
     {:title (translate [:actions :profile])}
     [ui/icon-button {:on-click #(on-profile-click student)} [ic/data-usage]]]
    [ui/tooltip
     {:title (translate [:actions :edit])}
     [ui/icon-button {:on-click #(on-edit-click student)} [ic/create]]]
    [ui/tooltip
     {:title (translate [:actions :remove])}
     [ui/icon-button {:on-click #(on-remove-click student)} [ic/delete]]]]])

(defn students-list
  [props students]
  [ui/table
   [ui/table-head
    [ui/table-row
     [ui/table-cell {:style (get-in styles [:table-cell])}]
     [ui/table-cell {:style (get-in styles [:table-cell])}
      (translate [:items :name :title])]
     [ui/table-cell {:style (get-in styles [:table-cell])}
      (translate [:items :age :title])]
     [ui/table-cell {:style (get-in styles [:table-cell])}
      (translate [:items :class :title])]
     [ui/table-cell {:style (get-in styles [:table-cell])}
      (translate [:items :course :title])]
     [ui/table-cell {:style (get-in styles [:table-cell])}
      (translate [:items :tablet :title])]
     [ui/table-cell {:style (get-in styles [:table-cell])}]]]
   [ui/table-body
    (for [student students]
      ^{:key (:id student)}
      [list-item props student])]])

(defn- actions
  [{:keys [student on-class-click]}]
  [:div
   [ui/tooltip
    {:title (translate [:actions :class])}
    [ui/icon-button {:on-click #(on-class-click student)} [ic/assignment]]]])

(defn students-list-page
  []
  (let [filter (r/atom {:class-id nil})]
    (fn []
      (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
            class @(re-frame/subscribe [::classes-subs/current-class])
            classes @(re-frame/subscribe [::classes-subs/classes-list])
            students @(re-frame/subscribe [::students-subs/class-students class-id])
            is-loading? (or @(re-frame/subscribe [::students-subs/students-loading class-id])
                            @(re-frame/subscribe [::classes-subs/classes-loading]))

            handle-add-student-click #(re-frame/dispatch [::state/add-student class-id])
            handle-edit-student-click #(re-frame/dispatch [::state/edit-student (:id %) class-id])]
        (if is-loading?
          [ui/linear-progress]
          [content-page
           {:title         (translate [:title])
            :current-title (:name class)
            :actions (actions {:on-class-click  (fn [] (redirect-to :dashboard-class-profile :class-id class-id))})}
           [:div
            [students-list-filter
             {:classes classes
              :style   {:flex "0 0 auto"}}
             filter]
            [students-list
             {:on-profile-click (fn [{:keys [id class-id]}] (redirect-to :dashboard-student-profile :class-id class-id :student-id id))
              :on-edit-click    handle-edit-student-click
              :on-remove-click  (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-remove-from-class-form id]))}
             (->> students
                  (map-students-list)
                  (filter-students-list @filter))]
            [fab
             {:on-click   handle-add-student-click
              :color      "primary"
              :variant    "extended"
              :style      {:margin   16
                           :width    150
                           :height   40
                           :position "fixed"
                           :bottom   20
                           :right    20}
              :aria-label (translate [:add-student :text])}
             [ic/add]
             (translate [:add-student :text])]]])))))
