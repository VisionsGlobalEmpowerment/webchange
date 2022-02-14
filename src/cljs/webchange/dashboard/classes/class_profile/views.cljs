(ns webchange.dashboard.classes.class-profile.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page content-page-section score-table]]
    [webchange.dashboard.classes.class-profile.state :as state]
    [webchange.dashboard.classes.class-profile.stubs :refer [scores-stub]]
    [webchange.dashboard.classes.class-profile.profile-table.views :refer [profile-table]]))

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
  []
  (let [course-name @(re-frame/subscribe [::state/course-name])]
    [ui/grid {:container  true
              :justify    "space-between"
              :class-name "class-short-card"}
     [ui/grid {:item true :xs 3}
      [data-item {:text  (translate [:card :course])
                  :value course-name}]]]))

(defn- actions
  []
  (let [handle-students-click #(re-frame/dispatch [::state/open-students-page])
        handle-edit-click #(re-frame/dispatch [::state/open-edit-class-form])
        handle-remove-click #(re-frame/dispatch [::state/open-remove-class-form])]
    [:div
     [ui/tooltip
      {:title (translate [:actions :students])}
      [ui/icon-button {:on-click handle-students-click} [ic/people]]]
     [ui/tooltip
      {:title (translate [:actions :edit])}
      [ui/icon-button {:on-click handle-edit-click} [ic/create]]]
     [ui/tooltip
      {:title (translate [:actions :remove])}
      [ui/icon-button {:on-click handle-remove-click} [ic/delete]]]]))

(defn- class-profile
  [{:keys [class-id]}]
  (let [course-slug @(re-frame/subscribe [::state/course-slug])
        class-name @(re-frame/subscribe [::state/class-name])]
    [content-page
     {:title         (translate [:header])
      :current-title class-name
      :actions       [actions]
      :class-name    "class-profile"}
     [class-short-card]
     [content-page-section
      {:title (translate [:class :header])}
      [profile-table {:class-id    class-id
                      :course-slug course-slug}]]
     #_[content-page-section
        {:title (translate [:assessment :header])}
        [score-table
         {:title       (translate [:assessment :table-title])
          :items-title (translate [:assessment :table-items-title])
          :levels      [80 95]
          :legend      (translate [:assessment :legend])}
         (scores-stub students)]]]))

(defn class-profile-page
  [{:keys [class-id]}]
  (re-frame/dispatch [::state/init {:class-id class-id}])
  [class-profile
   {:class-id class-id}])
