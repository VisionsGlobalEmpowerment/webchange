(ns webchange.lesson-builder.tools.question-form.menu.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.info.views :refer [info]]
    [webchange.lesson-builder.tools.question-form.menu.state :as state]
    [webchange.lesson-builder.tools.question-form.state :as parent-state]
    [webchange.lesson-builder.tools.question-form.question-options.views :as options]
    [webchange.ui.index :as ui]))

(defn- control-group
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.control-group
              [:h1 title]])))

(defn- form-actions
  []
  (let [action @(re-frame/subscribe [::state/current-action])
        saving? @(re-frame/subscribe [::parent-state/saving?])
        handle-cancel-click #(re-frame/dispatch [::parent-state/cancel])
        handle-save-click #(re-frame/dispatch [::parent-state/save action])]
    [:div.form-actions
     [ui/button {:color     "blue-1"
                 :disabled? saving?
                 :on-click  handle-cancel-click}
      "Cancel"]
     [ui/button {:on-click handle-save-click
                 :loading? saving?}
      "Save"]]))

(defn question-menu
  []
  (let [title @(re-frame/subscribe [::state/menu-title])]
    [:div.question-form--question-menu
     [:div.params-form
      [control-group {:title title}
       [info "Name your question so you can find and drag it into the correct place in the Script editor."]
       [options/question-alias]]
      [control-group {:title "Select Question Type"}
       [options/question-type]]]
     [form-actions]]))