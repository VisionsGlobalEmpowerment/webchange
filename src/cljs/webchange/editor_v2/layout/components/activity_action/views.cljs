(ns webchange.editor-v2.layout.components.activity-action.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.editor-v2.layout.components.activity-action.state :as scene-action.events]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]))

(defn action-modal
  [{:keys [course-id]}]
  (let [open? @(re-frame/subscribe [::scene-action.events/modal-state])
        current-action @(re-frame/subscribe [::scene-action.events/current-action])
        scene-id (re-frame/subscribe [::subs/current-scene])
        scene-data @(re-frame/subscribe [::subs/scene @scene-id])
        metadata (get scene-data :metadata)
        action (get-in metadata [:actions current-action])
        data (r/atom {})
        close #(re-frame/dispatch [::scene-action.events/close])
        save #(re-frame/dispatch [::scene-action.events/save @data])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   close
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title (:title action)]
       [ui/dialog-content {:class-name "translation-form"}
        [template {:template action
                   :metadata metadata
                   :data     data}]]
       [ui/dialog-actions
        [ui/button {:on-click close}
         "Cancel"]
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click save}
          "Save"]]]])))

(defn- action-button
  [{:keys [name handle-click]}]
  [ui/form-control {:full-width true
                    :margin     "normal"}
   [ui/button
    {:on-click handle-click}
    name]])

(defn activity-actions
  [{:keys [scene-data course-id]}]
  (let [actions (get-in scene-data [:metadata :actions])]
    [:div
     (for [[name action] actions]
       ^{:key name}
       [action-button {:name         (:title action)
                       :handle-click #(re-frame/dispatch [::scene-action.events/show-actions-form name])}])
     [action-modal {:course-id course-id}]]))
