(ns webchange.editor-v2.activity-form.generic.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.interpreter-stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.activity-form.generic.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.activity-form.generic.components.select-stage.views :refer [select-stage]]
    [webchange.editor-v2.activity-form.get-activity-type :refer [get-activity-type]]
    [webchange.editor-v2.components.activity-tracks.views :refer [activity-tracks]]
    [webchange.editor-v2.creation-progress.views :refer [progress-panel]]
    [webchange.editor-v2.layout.views :refer [layout]]))

(defn- asset-block
  [{:keys [activity-type]}]
  (when (= activity-type "book")
    [:div.asset-block
     [select-stage]
     [object-selector]]))

(defn activity-form
  [{:keys [scene-data]}]
  (r/with-let [;_ (re-frame/dispatch [::progress-state/show-translation-progress])
               ]
    (let [activity-type (get-activity-type scene-data)]
      [layout {:scene-data scene-data}
       [:div.generic-editor
        [:div.interpreter-wrapper
         [interpreter-stage {:class-name "generic-interpreter"}]]
        [asset-block {:activity-type activity-type}]
        [activity-tracks {:class-name "generic-diagram"}]]
       ;[progress-panel]
       ])))
