(ns webchange.editor-v2.activity-form.generic.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.generic.views-skeleton :refer [skeleton]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.activity-form.get-activity-type :refer [get-activity-type]]

    [webchange.editor-v2.activity-form.common.interpreter-stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.activity-form.generic.components.activity-action.views :refer [activity-actions]]
    [webchange.editor-v2.activity-form.generic.components.change-background.views-background :refer [change-background]]
    [webchange.editor-v2.activity-form.generic.components.change-skin.views :refer [change-skin]]
    [webchange.editor-v2.activity-form.generic.components.common-actions.views :refer [actions]]
    [webchange.editor-v2.activity-form.generic.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.activity-form.generic.components.select-stage.views :refer [select-stage]]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]

    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.creation-progress.views :refer [progress-panel]]))

(defn activity-form
  [{:keys [scene-data]}]
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
    (let [activity-type (get-activity-type scene-data)]
      [layout
       [skeleton {:top-left-component  (into [:div]
                                             (cond-> [[activity-actions {:scene-data scene-data}]]
                                                     (= activity-type "book") (concat [[select-stage]
                                                                                       [object-selector]])
                                                     (not= activity-type "book") (concat [[change-background]
                                                                                          [actions]
                                                                                          [change-skin]])))
                  :top-right-component [interpreter-stage]
                  :bottom-component    [dialogs-diagram {:scene-data scene-data}]
                  :modals              [progress-panel]}]])))
