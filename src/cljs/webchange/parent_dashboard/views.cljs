(ns webchange.parent-dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent-dashboard.state :as state]
    [webchange.ui-framework.components.index :refer [dialog input label button]]))

(defn- student-card
  [{:keys [name user-id]}]
  [:div
   [:p {:on-click #(re-frame/dispatch [::state/login-as user-id])} name]])

(defn- students
  []
  (re-frame/dispatch [::state/load-students])
  (fn []
    (let [items @(re-frame/subscribe [::state/students])]
      (for [item items]
        [student-card item]))))

(defn- add-student-form
  []
  (r/with-let [data (r/atom {})]
    [:div
     [:div
      [label "Name"]
      [input {:value (:name @data)
              :on-changle #(swap! data assoc :name %)}]]
     [:div
      [label "Age"]
      [input {:value (:age @data)
              :on-changle #(swap! data assoc :age %)}]]
     [:div
      [label "Device"]
      [input {:value (:device @data)
              :on-changle #(swap! data assoc :device %)}]]
     [button {:on-click #(re-frame/dispatch [::state/add-student @data])
              :variant  "outlined"
              :size     "big"}
      "Submit Student"]
     ]))

(defn- add-student-button
  []
  [button {:on-click #(re-frame/dispatch [::state/open-add-student-page])
           :variant  "outlined"
           :size     "big"}
   "Add student"])

(defn dashboard-page
  []
  [:div
   [students]
   [add-student-button]])

(defn add-student-page
  []
  [add-student-form])

(defn help-page
  [])
