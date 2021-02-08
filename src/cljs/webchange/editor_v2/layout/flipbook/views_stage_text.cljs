(ns webchange.editor-v2.layout.flipbook.views-stage-text
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.utils :refer [scene-data->objects-list]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.editor-v2.translator.text.views-chunks-editor-form :refer [chunks-editor-form]]
    [webchange.state.state :as state]))

(defn stage-text
  []
  (let [scene-data @(re-frame/subscribe [::state/scene-data])
        stage-idx @(re-frame/subscribe [::stage/current-stage])
        text-objects (scene-data->objects-list scene-data stage-idx)
        handle-change (fn [text-name text-data-patch]
                        (re-frame/dispatch [::state/update-scene-object {:object-name       text-name
                                                                         :object-data-patch text-data-patch}]))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     (for [[text-name text-data] text-objects]
       ^{:key text-name}
       [ui/grid {:item true :xs 6}
        [chunks-editor-form (merge (select-keys text-data [:text :chunks])
                                   {:on-change (fn [data] (handle-change text-name data))})]])]))
