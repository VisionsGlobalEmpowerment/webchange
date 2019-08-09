(ns webchange.dashboard.classes.class-profile.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page content-page-section score-table]]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.classes.class-profile.stubs :refer [scores-stub profile-stub]]
    [webchange.dashboard.classes.class-profile.views-profile-table :refer [profile-table]]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.students.common.map-students :refer [map-students-list]]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.routes :refer [redirect-to]]))

(defn- translate
  [path]
  (get-in {:header     "Class Profile"
           :class      {:header "Class profile"}
           :actions    {:edit     "Edit"
                        :remove   "Remove"
                        :students "Students"}
           :card       {:course "Course"}
           :assessment {:header            "Assessment / Test"
                        :table-title       "Assessment #"
                        :table-items-title "Stud Name"
                        :legend            {:green  "Scored 95+"
                                            :yellow "Scored between 80-95%"
                                            :red    "Scored 79% or below"}}}
          path))

(defn data-item
  [{:keys [text value]}]
  [ui/typography
   {:variant "body1"
    :style   {:padding "12px 0"}}
   [:strong (str text ": ")] value])

(defn- class-short-card
  [{:keys [class]}]
  [ui/grid {:container true
            :justify   "space-between"}
   [ui/grid {:item true :xs 3}
    [data-item {:text  (translate [:card :course])
                :value (:course class)}]]])

(defn- actions
  [{:keys [class on-edit-click on-remove-click on-students-click]}]
  [:div
   [ui/tooltip
    {:title (translate [:actions :students])}
    [ui/icon-button {:on-click #(on-students-click class)} [ic/people]]]
   [ui/tooltip
    {:title (translate [:actions :edit])}
    [ui/icon-button {:on-click #(on-edit-click class)} [ic/create]]]
   [ui/tooltip
    {:title (translate [:actions :remove])}
    [ui/icon-button {:on-click #(on-remove-click class)} [ic/delete]]]])

(defn- class-profile
  [{:keys [class students]}]
  (let [assessment-data (scores-stub students)
        assessment-levels [80 95]
        course-name "Vera La Vaquita"]
    [content-page
     {:title         (translate [:header])
      :current-title (:name class)
      :actions       (actions {:class             class
                               :on-edit-click     (fn [{:keys [id]}] (re-frame/dispatch [::classes-events/show-edit-class-form id]))
                               :on-remove-click   (fn [{:keys [id]}] (re-frame/dispatch [::dashboard-events/show-delete-class-form id]))
                               :on-students-click (fn [{:keys [id]}] (redirect-to :dashboard-students :class-id id))})}
     [class-short-card {:class (merge class {:course course-name})}]
     [content-page-section
      {:title (translate [:class :header])}
      [profile-table]]
     #_[content-page-section
        {:title (translate [:assessment :header])}
        [score-table
         {:title       (translate [:assessment :table-title])
          :items-title (translate [:assessment :table-items-title])
          :levels      assessment-levels
          :legend      (translate [:assessment :legend])}
         assessment-data]
        ]]))

(defn class-profile-page
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        class @(re-frame/subscribe [::classes-subs/current-class])
        students @(re-frame/subscribe [::students-subs/class-students class-id])
        is-loading? @(re-frame/subscribe [::students-subs/students-loading class-id])
        class-removed? (and (nil? class) (not is-loading?))]
    (cond
      class-removed? (do (redirect-to :dashboard-classes)
                         [:span "Redirect.."])
      is-loading? [ui/linear-progress]
      :else [class-profile
             {:class    class
              :students (map-students-list students)}])))
