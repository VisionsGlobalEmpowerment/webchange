(ns webchange.student.pages.select-school.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student.pages.select-school.state :as state]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- select-school-form
  []
  (let [schools @(re-frame/subscribe [::state/schools])
        select-school #(re-frame/dispatch [::state/select-school %])]
    [:div {:class-name "select-school-form"}
     [:h1.header "Select School"]
     (for [{:keys [id name]} schools]
       ^{:key id}
       [:div {:class-name "school-form-item"
              :on-click #(select-school id)}
        (str name)])]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [:div {:class-name "student--select-school-page"}
     [select-school-form]]))
