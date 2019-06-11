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

(defn- add-new-student-dashboard-item
  [{:keys [on-click]}]
  [:div.students-dashboard-item.add-new {:on-click on-click}
   [:div.students-dashboard-item_body "+ New"]])

;      (for [{id :id {first-name :first-name last-name :last-name} :user} students]
; {:id 4, :user-id 5, :class-id 1, :user {:first-name we, :last-name nil, :email nil}}

(defn- students-dashboard-item
  [{{:keys [first-name last-name email]} :user :as student}
   {:keys [on-edit on-remove]}]
  [:div.students-dashboard-item
   [ui/card
    [ui/card-header {:title    (str first-name " " last-name)
                     :subtitle email
                     :avatar   (r/as-element [ui/avatar (get first-name 0)])}]
    [ui/card-actions {:style {:text-align "right"}}
     [ui/icon-menu {:icon-button-element (r/as-element [ui/icon-button (ic/navigation-more-horiz)])
                    :anchor-origin       {:horizontal "left" :vertical "top"}
                    :target-origin       {:horizontal "left" :vertical "top"}}
      [ui/menu-item {:primary-text "Edit"
                     :on-click     #(on-edit student)}]
      [ui/menu-item {:primary-text "Remove"
                     :on-click     #(on-remove student)}]]]]])

(defn students-dashboard
  []
  (let [current-student (r/atom {})
        modal-state (r/atom {:open        false
                             :title       ""
                             :handle-save #()})]
    (fn []
      (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
            _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
            students @(re-frame/subscribe [::students-subs/class-students class-id])
            open-modal #(swap! modal-state assoc :open true)
            close-modal #(swap! modal-state assoc :open false)
            add-student (fn [student-data] (re-frame/dispatch [::students-events/add-student class-id student-data]))
            edit-student (fn [student-data] (re-frame/dispatch [::students-events/edit-student (:class-id student-data) (:id student-data) student-data]))
            remove-student (fn [student-data] (re-frame/dispatch [::students-events/delete-student (:class-id student-data) (:id student-data)]))
            handle-add-click (fn [] (do (reset! current-student {})
                                        (swap! modal-state assoc :title :add)
                                        (swap! modal-state assoc :handle-save add-student)
                                        (open-modal)))
            handle-edit-click (fn [student-data] (do (reset! current-student (flatten-student student-data))
                                                     (swap! modal-state assoc :title :edit)
                                                     (swap! modal-state assoc :handle-save edit-student)
                                                     (open-modal)))]
        [ui/card
         [ui/card-header {:title "Students"}]
         [ui/card-media
          [:div.students-dashboard
           (for [student students]
             ^{:key (:id student)}
             [students-dashboard-item student {:on-edit   handle-edit-click
                                               :on-remove remove-student}])
           [add-new-student-dashboard-item {:on-click handle-add-click}]]
          [student-modal current-student {:title        (:title @modal-state)
                                          :modal-open   (:open @modal-state)
                                          :handle-save  (:handle-save @modal-state)
                                          :handle-close close-modal}]]]))))
