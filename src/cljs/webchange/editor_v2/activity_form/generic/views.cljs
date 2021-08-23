(ns webchange.editor-v2.activity-form.generic.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.interpreter-stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.activity-form.common.object-form.views :refer [object-form]]
    [webchange.editor-v2.activity-form.common.objects-tree.views :refer [objects-tree]]
    [webchange.editor-v2.activity-form.generic.views-actions :refer [actions]]
    [webchange.editor-v2.activity-form.get-activity-type :refer [get-activity-type]]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.creation-progress.views :refer [progress-panel]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]

    [webchange.editor-v2.activity-form.generic.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.activity-form.generic.components.select-stage.views :refer [select-stage]]
    [webchange.editor-v2.components.activity-tracks.views :refer [activity-tracks]]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- asset-block
  [{:keys [activity-type]}]
  (into [:div.asset-block]
        (cond-> [[objects-tree]
                 [object-form]]
                (= activity-type "book") (concat [[select-stage]
                                                  [object-selector]]))))

(defn activity-form
  [{:keys [scene-data]}]
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])
               show-new-diagram? (r/atom true)]
    (let [activity-type (get-activity-type scene-data)]
      [layout {:scene-data scene-data}
       [:div.generic-editor
        [:div.interpreter-wrapper
         [interpreter-stage {:class-name "generic-interpreter"}]
         [actions {:scene-data scene-data}]]
        [asset-block {:activity-type activity-type}]
        [:div.swap-diagram-mode
         [icon-button {:icon     "swap"
                       :title    "Change Diagram Mode"
                       :on-click #(swap! show-new-diagram? not)}]]
        (if @show-new-diagram?
          [activity-tracks {:class-name "generic-diagram"}]
          [dialogs-diagram {:class-name "generic-diagram"
                            :scene-data scene-data}])]
       [progress-panel]])))
