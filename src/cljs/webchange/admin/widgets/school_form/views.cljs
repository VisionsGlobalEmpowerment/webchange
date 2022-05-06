(ns webchange.admin.widgets.school-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.school-form.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- about-control
  []
  (let [id "about"
        value @(re-frame/subscribe [::state/about])
        error @(re-frame/subscribe [::state/about-error])
        handle-change #(re-frame/dispatch [::state/set-about %])]
    [:<>
     [c/label {:for id} "About"]
     [c/text-area {:id        id
                   :value     value
                   :error     error
                   :on-change handle-change}]]))

(defn- location-control
  []
  (let [id "location"
        value @(re-frame/subscribe [::state/location])
        error @(re-frame/subscribe [::state/location-error])
        handle-change #(re-frame/dispatch [::state/set-location %])]
    [:<>
     [c/label {:for id} "Location"]
     [c/input {:id        id
               :value     value
               :error     error
               :on-change handle-change}]]))

(defn- name-control
  []
  (let [id "name"
        value @(re-frame/subscribe [::state/school-name])
        error @(re-frame/subscribe [::state/school-name-error])
        handle-change #(re-frame/dispatch [::state/set-school-name %])]
    [:<>
     [c/label {:for id} "School Name"]
     [c/input {:id        id
               :value     value
               :error     error
               :on-change handle-change}]]))

(defn- submit
  []
  (let [handle-click #(re-frame/dispatch [::state/save])]
    [c/button {:on-click   handle-click
               :class-name "submit"}
     "Save"]))

(defn school-form
  [{:keys [id on-save]}]
  (re-frame/dispatch [::state/init {:id      id
                                    :on-save on-save}])
  (fn []
    [:div.widget--school-form
     [name-control]
     [location-control]
     [about-control]
     [submit]]))
