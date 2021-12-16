(ns webchange.editor-v2.activity-form.generic.components.activity-action.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.editor-v2.activity-form.generic.components.activity-action.state :as scene-action.events]
    [webchange.state.state-activity :as state-activity]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]
    [webchange.editor-v2.wizard.validator :as validator]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui-framework.components.index :refer [dialog button]]
    [webchange.utils.scene-data :as utils]
    [webchange.logger.index :as logger]))

(defn- get-action-default-data
  [scene-data action-data]
  (if (contains? action-data :default-props)
    (let [props-key (-> action-data (get :default-props) (keyword))
          saved-props (get-in scene-data [:metadata :saved-props] {})]
      (get saved-props props-key {}))
    {}))

(defn- action-modal-view
  []
  (r/with-let [scene-id (re-frame/subscribe [::subs/current-scene])
               scene-data @(re-frame/subscribe [::subs/scene @scene-id])
               metadata (get scene-data :metadata)
               current-action-name @(re-frame/subscribe [::state-activity/current-action])
               current-action-data (get-in metadata [:actions current-action-name])
               data (r/atom (get-action-default-data scene-data current-action-data))
               {:keys [valid?] :as validator} (validator/init data)
               close #(re-frame/dispatch [::scene-action.events/close])
               save #(do
                       (if (valid?) (re-frame/dispatch [::scene-action.events/save {:data @data}])))]
    [dialog
     {:title    (:title current-action-data)
      :on-close close
      :actions  [[button {:on-click save
                          :size     "big"}
                  "Save"]
                 [button {:on-click close
                          :variant  "outlined"
                          :color    "default"
                          :size     "big"}
                  "Cancel"]]}
     [template {:template  current-action-data
                :metadata  metadata
                :data      data
                :validator validator}]]))

(defn activity-action-modal
  []
  (let [open? @(re-frame/subscribe [::scene-action.events/modal-state])
        current-action-name @(re-frame/subscribe [::state-activity/current-action])]
    (when open?
      ^{:key current-action-name}
      [action-modal-view])))

(defn get-activity-actions-list
  [scene-data]
  (let [page-number @(re-frame/subscribe [::state-flipbook/current-page-number])]
    (->> (utils/get-metadata-untracked-actions scene-data)
         (map (fn [[name {:keys [title options]}]]
                {:text     title
                 :on-click (if-not (empty? options)
                             #(re-frame/dispatch [::scene-action.events/show-actions-form name])
                             #(re-frame/dispatch [::scene-action.events/save {:action name
                                                                              ;; ToDo: check is it flipbook?
                                                                              :data   {:page-number page-number}}]))})))))
