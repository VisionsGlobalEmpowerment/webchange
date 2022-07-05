(ns webchange.editor.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor.state :as state]
    [webchange.subs :as subs]))

(defn lesson-builder
  [{:keys [scene-id]}]
  (re-frame/dispatch [::state/init scene-id])
  (fn []
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])]
      [layout {:scene-data scene-data}])))
