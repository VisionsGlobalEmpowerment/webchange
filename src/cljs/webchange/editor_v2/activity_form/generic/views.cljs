(ns webchange.editor-v2.activity-form.generic.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.interpreter-stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.activity-form.generic.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.activity-form.generic.components.select-stage.views :refer [select-stage]]
    [webchange.editor-v2.activity-form.get-activity-type :refer [get-activity-type]]
    [webchange.editor-v2.components.activity-tracks.views :refer [activity-tracks]]
    [webchange.editor-v2.creation-progress.views :refer [progress-panel]]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- asset-block
  [{:keys [activity-type]}]
  (when (= activity-type "book")
    [:div.asset-block
     [select-stage]
     [object-selector]]))

(defn activity-form
  [{:keys [scene-data]}]
  (r/with-let [;_ (re-frame/dispatch [::progress-state/show-translation-progress])
               show-tracks? (r/atom false)]
    (let [activity-type (get-activity-type scene-data)]
      [:div.generic-editor
       [:div.interpreter-wrapper
        [interpreter-stage {:class-name "generic-interpreter"}]]
       [asset-block {:activity-type activity-type}]
       [icon-button {:icon       "dashboard"
                     :class-name (get-class-name {"switch-activity-tracks" true
                                                  "active"                 @show-tracks?})
                     :on-click   #(swap! show-tracks? not)}
        "Activity Tracks"]
       (when @show-tracks?
         [activity-tracks {:class-name "generic-diagram"}])]
      ;[progress-panel]
      )))
