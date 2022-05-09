(ns webchange.admin.widgets.school-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.school-form.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- about-control
  [{:keys [disabled?]}]
  (let [id "about"
        value @(re-frame/subscribe [::state/about])
        error @(re-frame/subscribe [::state/about-error])
        handle-change #(re-frame/dispatch [::state/set-about %])]
    [:<>
     [c/label {:for id} "About"]
     [c/text-area {:id        id
                   :value     value
                   :error     error
                   :disabled? disabled?
                   :on-change handle-change}]]))

(defn- location-control
  [{:keys [disabled?]}]
  (let [id "location"
        value @(re-frame/subscribe [::state/location])
        error @(re-frame/subscribe [::state/location-error])
        handle-change #(re-frame/dispatch [::state/set-location %])]
    [:<>
     [c/label {:for id} "Location"]
     [c/input {:id        id
               :value     value
               :error     error
               :disabled? disabled?
               :on-change handle-change}]]))

(defn- name-control
  [{:keys [disabled?]}]
  (let [id "name"
        value @(re-frame/subscribe [::state/school-name])
        error @(re-frame/subscribe [::state/school-name-error])
        handle-change #(re-frame/dispatch [::state/set-school-name %])]
    [:<>
     [c/label {:for id} "School Name"]
     [c/input {:id        id
               :value     value
               :error     error
               :disabled? disabled?
               :on-change handle-change}]]))

(defn- submit
  [{:keys [disabled?]}]
  (let [loading? @(re-frame/subscribe [::state/data-saving?])
        handle-click #(re-frame/dispatch [::state/save])]
    [c/button {:on-click   handle-click
               :disabled?  (or disabled? loading?)
               :class-name "submit"}
     (if-not loading?
       "Save"
       [c/circular-progress])]))

(defn- data-loading-indicator
  []
  [:div.data-loading-indicator
   [c/circular-progress]])

(defn school-form
  [{:keys [id on-save]}]
  (re-frame/dispatch [::state/init {:id      id
                                    :on-save on-save}])
  (fn [{:keys [editable?]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])]
      [:div.widget--school-form
       (if-not loading?
         [:div.controls
          [name-control {:disabled? (not editable?)}]
          [location-control {:disabled? (not editable?)}]
          [about-control {:disabled? (not editable?)}]]
         [data-loading-indicator])
       (when editable?
         [submit {:disabled? loading?}])])))
