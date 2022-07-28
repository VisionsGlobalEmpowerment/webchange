(ns webchange.lesson-builder.tools.script.dialog-item.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.character-animation.views :refer [character-animation]]
    [webchange.lesson-builder.tools.script.dialog-item.character-movement.views :refer [character-movement]]
    [webchange.lesson-builder.tools.script.dialog-item.effect-general.views :refer [effect-general]]
    [webchange.lesson-builder.tools.script.dialog-item.phrase.views :refer [phrase]]
    [webchange.lesson-builder.tools.script.dialog-item.question.views :refer [question]]
    [webchange.lesson-builder.tools.script.dialog-item.text-animation.views :refer [text-animation]]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.views :refer [item-wrapper]]))

(declare dialog-item)

(defn actions-sequence
  [{:keys [action-path]}]
  (let [dialog-items @(re-frame/subscribe [::state/sequence-items action-path])]
    [item-wrapper {:parallel?   true
                   :action-path action-path}
     (for [{:keys [id] :as dialog-item-data} dialog-items]
       ^{:key id}
       [dialog-item dialog-item-data])]))

(def available-items {:character-animation character-animation
                      :character-movement  character-movement
                      :effect-general      effect-general
                      :parallel            actions-sequence
                      :phrase              phrase
                      :text-animation      text-animation
                      :question            question})

(defn dialog-item
  [{:keys [action-path] :as props}]
  (let [action-type @(re-frame/subscribe [::state/action-type action-path])
        item-component (get available-items action-type)]
    (when (some? item-component)
      [item-component props])))
