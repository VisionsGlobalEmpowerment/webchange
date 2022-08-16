(ns webchange.lesson-builder.tools.question-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.check-list.views :refer [check-list]]
    [webchange.lesson-builder.components.info.views :refer [info]]
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.ui.index :as ui]))

(defn control-group
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.control-group
              [:h1 title]])))

(defn- question-alias
  []
  (let [field-name :alias
        value @(re-frame/subscribe [::state/form-field field-name])
        handle-change #(re-frame/dispatch [::state/set-form-field field-name %])]
    [ui/input {:label       "Question Name"
               :placeholder "Name"
               :value       value
               :on-change   handle-change}]))

(defn- question-type
  []
  (let [value @(re-frame/subscribe [::state/question-type])
        options @(re-frame/subscribe [::state/question-type-options])
        handle-change #(re-frame/dispatch [::state/set-question-type %])]
    [check-list {:items    options
                 :value    value
                 :on-click handle-change}]))

(defn- form-actions
  []
  (let [action @(re-frame/subscribe [::state/current-action])
        saving? @(re-frame/subscribe [::state/saving?])
        handle-cancel-click #(re-frame/dispatch [::state/cancel])
        handle-save-click #(re-frame/dispatch [::state/save action])]
    [:div.form-actions
     [ui/button {:color     "blue-1"
                 :disabled? saving?
                 :on-click  handle-cancel-click}
      "Cancel"]
     [ui/button {:on-click handle-save-click
                 :loading? saving?}
      "Save"]]))

(defn question-params
  []
  (let [action @(re-frame/subscribe [::state/current-action])
        title @(re-frame/subscribe [::state/menu-title])]
    [:div.question-form--question-params
     [:div.params-form
      [control-group {:title title}
       (when (= action :add)
         [info "Name your question so you can find and drag it into the correct place in the Script editor."])
       [question-alias]]
      [control-group {:title "Select Question Type"}
       [question-type]]]
     [form-actions]]))
