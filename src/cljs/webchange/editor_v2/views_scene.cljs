(ns webchange.editor-v2.views-scene
  (:require
    [webchange.editor.index :as e]
    [webchange.subs :as subs]
    [re-frame.core :as re-frame]))

(defn scene
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])]
    [:div.scene-container [e/with-stage [e/scene] {:width 768 :height 432 :scale-x 0.4 :scale-y 0.4} scene-id]]))