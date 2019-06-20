(ns webchange.dashboard.students.views-dashboard
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.utils :refer [flatten-student]]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(defn- add-new-student-dashboard-item []
  [:div.students-dashboard-item.add-new
   {:on-click #(re-frame/dispatch [::students-events/show-add-student-form])}
   [:div.students-dashboard-item_body "+ New"]])

(defn- students-dashboard-item
  [{{:keys [first-name last-name email]} :user :as student}]
  (let [menu-anchor (r/atom nil)
        menu-open? (r/atom false)]
    (fn []
      [:div.students-dashboard-item
       [ui/card
        [ui/card-header {:title    (str first-name " " last-name)
                         :subtitle email
                         :avatar   (r/as-element [ui/avatar (get first-name 0)])}]
        [ui/card-actions {:style {:float "right"}}
         [ui/icon-button
          {:on-click #(do (reset! menu-open? true)
                          (reset! menu-anchor (.-currentTarget %)))}
          [ic/more-horiz]]
         [ui/menu
          {:open @menu-open?
           :on-close #(reset! menu-open? false)
           :anchor-El @menu-anchor}
          [ui/menu-item
           {:on-click #(do (re-frame/dispatch [::students-events/show-edit-student-form (:id student)])
                           (reset! menu-open? false))}
           "Edit"]
          [ui/menu-item
           {:on-click #(do (re-frame/dispatch [::students-events/delete-student (:class-id student) (:id student)])
                           (reset! menu-open? false))}
           "Remove"]]]]])))

(defn students-dashboard
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        students @(re-frame/subscribe [::students-subs/class-students class-id])]
    [ui/card
     [ui/card-header {:title "Students"}]
     [ui/card-content
      [:div.students-dashboard
       (for [student students]
         ^{:key (:id student)}
         [students-dashboard-item student])
       [add-new-student-dashboard-item]]
      [student-modal]
      ]]))
