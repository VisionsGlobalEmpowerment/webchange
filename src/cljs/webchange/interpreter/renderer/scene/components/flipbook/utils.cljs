(ns webchange.interpreter.renderer.scene.components.flipbook.utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as executor]
    [webchange.interpreter.renderer.scene.components.flipbook.state-flip-actions :as flipbook-state]
    [webchange.interpreter.renderer.state.scene :as scene-state]))

(defn execute-action
  [action]
  (re-frame/dispatch [::executor/execute-action action]))

(defn flip-page
  [props]
  (re-frame/dispatch [::flipbook-state/execute-flip props]))

(defn set-position
  [object-name position]
  (re-frame/dispatch [::scene-state/change-scene-object object-name [[:set-position position]]]))

(defn set-visibility
  [object-name visible?]
  (re-frame/dispatch [::scene-state/change-scene-object object-name [[:set-visibility {:visible visible?}]]]))
