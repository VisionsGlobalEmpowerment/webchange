(ns webchange.student.pages.sign-in.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student.pages.sign-in.state :as state]))

(defn- current-code-item
  [{:keys [value]}]
  (if (some? value)
    value "*"))

(defn- current-code
  []
  (let [code @(re-frame/subscribe [::state/code])
        handle-remove-click #(re-frame/dispatch [::state/remove-value])]
    [:div
     (for [value code]
       ^{:key value}
       [current-code-item {:value value}])
     [:button {:on-click handle-remove-click}
      "<<"]]))

(defn- num-pad-key
  [{:keys [value]}]
  (let [handle-click #(re-frame/dispatch [::state/enter-value value])]
    [:button {:on-click handle-click}
     value]))

(defn- num-pad
  []
  [:div
   (for [value [1 2 3 4 5 6 7 8 9 0]]
     ^{:key value}
     [num-pad-key {:value value}])])

(defn- sign-in-form
  []
  [:div
   [:h1 "Student Access"]
   [current-code]
   [num-pad]])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (re-frame/dispatch [::state/set-school-id school-id])
    [:div {:class-name "student--sign-in-page"}
     [sign-in-form]]))
