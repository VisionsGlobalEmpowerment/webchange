(ns webchange.parent-dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent-dashboard.add-student.views :as add-student]
    [webchange.parent-dashboard.help.views :as help]
    [webchange.parent-dashboard.state :as state]
    [webchange.parent-dashboard.students-list.views :as students-list]
    [webchange.parent-dashboard.ui.index :refer [circular-progress]]))

(defn parent-page
  []
  (r/create-class
    {:display-name           "parent-page"
     :component-did-mount    (fn []
                               (re-frame/dispatch [::state/init]))
     :component-will-unmount (fn []
                               (re-frame/dispatch [::state/reset]))
     :reagent-render         (fn []
                               (let [ready? @(re-frame/subscribe [::state/ready?])]
                                 (into [:div.parent-page]
                                       (if ready?
                                         (-> (r/current-component) (r/children))
                                         [[circular-progress {:class-name "parent-page-loader"}]]))))}))

(defn dashboard
  []
  [parent-page
   [students-list/students-list-page]])

(defn add-student
  []
  [parent-page
   [add-student/add-student-page]])

(defn help
  []
  [parent-page
   [help/help-page]])
