(ns webchange.admin.widgets.school-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.school-form.state :as state]
    [webchange.validation.specs.school-spec :as school-spec]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]))

(defn- login-link
  [{:keys [id label]} _]
  (fn [{:keys [disabled? id label] :as a} {:keys [value error handle-change] :as b}]
    (let [handle-copy-click #(re-frame/dispatch [::state/copy-login-link])
          value @(re-frame/subscribe [::state/login-link])]
      [:<>
       [c/label {:for id} label]
       [c/input {:id        id
                 :value     value
                 :disabled? true
                 :action    [c/icon-button {:icon     "copy"
                                            :variant  "light"
                                            :on-click handle-copy-click}]}]])))

(def edit-school-model {:name       {:label "School Name"
                                     :type  :text}
                        :location   {:label "Location"
                                     :type  :text}
                        :login-link {:label   "Login link"
                                     :type    :custom
                                     :control login-link}
                        :about      {:label "About"
                                     :type  :text-multiline}})

(def add-school-model {:name     {:label "School Name"
                                  :type  :text}
                       :location {:label "Location"
                                  :type  :text}
                       :about    {:label "About"
                                  :type  :text-multiline}})

(defn edit-school-form
  []
  (r/create-class
    {:display-name "Edit School Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [class-name editable? on-cancel on-save school-id]}]
       (let [loading? @(re-frame/subscribe [::state/data-loading?])
             saving? @(re-frame/subscribe [::state/data-saving?])
             school-data @(re-frame/subscribe [::state/form-data])
             model edit-school-model
             handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
         [ui/form {:form-id    (-> (str "school-" school-id)
                                   (keyword))
                   :data       school-data
                   :model      model
                   :spec       ::school-spec/edit-school
                   :on-save    handle-save
                   :on-cancel  on-cancel
                   :disabled?  (not editable?)
                   :loading?   loading?
                   :saving?    saving?
                   :class-name (c/get-class-name {"widget--school-form" true
                                                  class-name            (some? class-name)})}]))}))

(defn add-school-form
  []
  (r/create-class
    {:display-name "Add School Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [class-name on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             school-data @(re-frame/subscribe [::state/form-data])
             model add-school-model
             handle-save #(re-frame/dispatch [::state/create-school % {:on-success on-save}])]
         [ui/form {:form-id    (-> (str "add-school")
                                   (keyword))
                   :data       school-data
                   :model      model
                   :spec       ::school-spec/create-school
                   :on-save    handle-save
                   :saving?    saving?
                   :class-name (c/get-class-name {"widget--school-form" true
                                                  class-name            (some? class-name)})}]))}))
