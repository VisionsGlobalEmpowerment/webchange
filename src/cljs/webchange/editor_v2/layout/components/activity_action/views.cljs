(ns webchange.editor-v2.layout.components.activity-action.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.editor-v2.layout.components.activity-action.state :as scene-action.events]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]
    [webchange.editor-v2.wizard.validator :as validator]
    [webchange.ui-framework.components.index :refer [dialog button]]))

(defn- get-action-default-data
  [scene-data action-data]
  (if (contains? action-data :default-props)
    (let [props-key (-> action-data (get :default-props) (keyword))
          saved-props (get-in scene-data [:metadata :saved-props] {})]
      (->> (get saved-props props-key {})
           (map (fn [[key value]] [(clojure.core/name key) value]))
           (into {})))
    {}))

(defn action-modal
  []
  (r/with-let [scene-id (re-frame/subscribe [::subs/current-scene])
               scene-data @(re-frame/subscribe [::subs/scene @scene-id])
               metadata (get scene-data :metadata)
               current-action-name @(re-frame/subscribe [::scene-action.events/current-action])
               current-action-data (get-in metadata [:actions current-action-name])
               data (r/atom (get-action-default-data scene-data current-action-data))
               {:keys [valid?] :as validator} (validator/init data)
               close #(re-frame/dispatch [::scene-action.events/close])
               save #(do
                       (if (valid?) (re-frame/dispatch [::scene-action.events/save @data])))]
    [dialog
     {:title    (:title current-action-data)
      :on-close close}
     [template {:template  current-action-data
                :metadata  metadata
                :data      data
                :validator validator}]
     [:div.actions
      [button {:on-click save
               :size     "big"}
       "Save"]
      [button {:on-click close
               :variant  "outlined"
               :size     "big"}
       "Cancel"]]]))

(defn action-modal-container
  []
  (let [open? @(re-frame/subscribe [::scene-action.events/modal-state])
        current-action-name @(re-frame/subscribe [::scene-action.events/current-action])]
    (when open?
      ^{:key current-action-name}
      [action-modal])))

(defn- action-button
  [{:keys [name handle-click]}]
  [ui/form-control {:full-width true
                    :margin     "normal"}
   [ui/button
    {:on-click handle-click}
    name]])

(defn activity-actions
  [{:keys [scene-data]}]
  (let [actions (get-in scene-data [:metadata :actions])]
    [:div
     (for [[name action] actions]
       ^{:key name}
       [action-button {:name         (:title action)
                       :handle-click #(re-frame/dispatch [::scene-action.events/show-actions-form name])}])
     [action-modal-container]]))
