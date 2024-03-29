(ns webchange.student.pages.sign-in.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student.pages.sign-in.state :as state]
    [webchange.ui.index :as ui]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]
    [webchange.subs :as subs]))

(defn- current-code-item
  [{:keys [value]}]
  [:div {:class-name (get-class-name {"current-code-item"        true
                                      "current-code-item--empty" (nil? value)})}
   (when (some? value)
     value)])

(defn- current-code
  []
  (let [code @(re-frame/subscribe [::state/code])
        handle-remove-click #(re-frame/dispatch [::state/remove-value])]
    [:div {:class-name "current-code"}
     (for [[idx value] (map-indexed vector code)]
       ^{:key idx}
       [current-code-item {:value value}])
     [:button {:class-name "remove-button"
               :on-click   handle-remove-click}]]))

(defn- num-pad-key
  [{:keys [value]}]
  (let [handle-click #(re-frame/dispatch [::state/enter-value value])]
    [:button {:on-click   handle-click
              :class-name (get-class-name {"num-pad-key"                     true
                                           (str "num-pad-key--value-" value) true})}
     value]))

(defn- num-pad
  []
  [:div.num-pad
   (for [value [1 2 3 4 5 6 7 8 9 0]]
     ^{:key value}
     [num-pad-key {:value value}])])

(defn- sign-in-form
  []
  (let [change-school #(re-frame/dispatch [::state/change-school])
        loading? @(re-frame/subscribe [::subs/data-loading?])]
    (if loading?
      [ui/loading-overlay]
      [:div {:class-name "sign-in-form"}
       [:h1.header "Student Access"]
       [current-code]
       [num-pad]
       [:div {:on-click change-school} "change school"]])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (re-frame/dispatch [::state/set-school-id school-id])
    [:div {:class-name "student--sign-in-page"}
     [sign-in-form]]))
