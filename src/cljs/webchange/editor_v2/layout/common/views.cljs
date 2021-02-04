(ns webchange.editor-v2.layout.common.views
  (:require
    [webchange.editor-v2.layout.components.activity-action.views :refer [activity-actions]]
    [webchange.editor-v2.layout.components.activity-stage.views :refer [select-stage]]
    [webchange.editor-v2.layout.components.background.views-background :refer [change-background]]
    [webchange.editor-v2.layout.components.change-skin.views :refer [change-skin]]
    [webchange.editor-v2.layout.components.interpreter_stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.layout.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.layout.components.share-button.views :refer [share-button]]
    [webchange.editor-v2.layout.components.workflow-steps.views :as workflow-steps]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]))

(defn- get-styles
  []
  {:main-container  {:height         "100%"
                     :display        "flex"
                     :flex-direction "column"}
   :top-panel       {:display        "flex"
                     :width          "100%"
                     :padding-bottom "30px"}
   :data-container  {:flex-grow 0
                     :width     "300px"}
   :scene-container {:flex-grow       1
                     :display         "flex"
                     :justify-content "center"}})

(defn layout
  [{:keys [fullscreen? course-id scene-data]}]
  (let [styles (get-styles)]
    [:div {:style (:main-container styles)}
     [:div {:style (merge (:top-panel styles)
                          (if fullscreen? {:display "none"} {}))}
      [:div {:style (:data-container styles)}
       [:div.data-selector
        [change-background]
        [share-button]
        [change-skin]
        [object-selector]
        [select-stage]]
       [:div [activity-actions {:course-id  course-id
                                :scene-data scene-data}]]]
      [:div {:style (:scene-container styles)}
       [interpreter-stage]]]
     [:div {:style {:margin-bottom "16px"}}
      [workflow-steps/fill-dialogs]]
     [dialogs-diagram {:scene-data scene-data}]]))
