(ns webchange.editor-v2.layout.common.views
  (:require
    [webchange.editor-v2.layout.components.activity-action.views :refer [activity-actions]]
    [webchange.editor-v2.layout.components.background.views-background :refer [change-background]]
    [webchange.editor-v2.layout.components.change-skin.views :refer [change-skin]]
    [webchange.editor-v2.layout.components.common-actions.views :refer [actions]]
    [webchange.editor-v2.layout.components.interpreter_stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.layout.components.share-button.views :refer [share-button]]
    [webchange.editor-v2.layout.components.history-button.views :refer [history-button]]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]
    [webchange.editor-v2.layout.views-skeleton :refer [skeleton]]))

(defn layout
  [{:keys [course-id scene-data]}]
  [skeleton {:top-left-component  [:div
                                   [change-background]
                                   [actions]
                                   [change-skin]
                                   [activity-actions {:course-id  course-id
                                                      :scene-data scene-data}]
                                   [share-button]
                                   [history-button]]
             :top-right-component [interpreter-stage]
             :bottom-component    [dialogs-diagram {:scene-data scene-data}]}])
