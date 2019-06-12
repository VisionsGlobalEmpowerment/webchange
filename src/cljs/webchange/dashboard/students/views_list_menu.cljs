(ns webchange.dashboard.students.views-list-menu
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(defn- students-list-menu-item
  [{:keys [user]}]
  (let [{:keys [first-name last-name]} user]
    [ui/list-item {}
     [ui/avatar (get first-name 0)]
     [ui/list-item-text {}  (str first-name " " last-name)]]))

(defn students-list-menu
  []
  (let [modal-state (r/atom {:open false})]
    (fn []
      (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
            _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
            students @(re-frame/subscribe [::students-subs/class-students class-id])
            open-modal #(swap! modal-state assoc :open true)
            close-modal #(swap! modal-state assoc :open false)
            current-student (atom {})
            add-student (fn [student-data] (re-frame/dispatch [::students-events/add-student class-id student-data]))]

        [ui/list
         [ui/list-item {:disabled     true}
          [ui/list-item-text {} "Students"]]
         (for [student students]
           ^{:key (:id student)}
           [students-list-menu-item student])
         (if class-id
           [ui/list-item {:on-click     (fn [] (do (reset! current-student {})
                                                   (swap! modal-state assoc :title :add)
                                                   (open-modal)))}
            [ui/list-item-icon {} [ic/add]]
            [ui/list-item-text {} "Add"]]
           [ui/list-item {:disabled true} [ui/chip {:label "Select Class"}]])
         [student-modal current-student {:title        :add
                                         :modal-open   (:open @modal-state)
                                         :handle-save  add-student
                                         :handle-close close-modal}]]))))
