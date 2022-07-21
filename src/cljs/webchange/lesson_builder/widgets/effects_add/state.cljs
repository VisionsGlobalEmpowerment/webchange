(ns webchange.lesson-builder.widgets.effects-add.state
  (:require
    [re-frame.core :as re-frame]))

(def available-effects
  [{:title     "Template effects"
    :component "template-effects"}
   {:title     "Image effects"
    :component "image-effects"}
   {:title     "Character movements"
    :component "character-movements"}
   {:title   "Guide effect"
    :effects [{:text        "Hide guide"
               :action-type "hide-guide"}
              {:text        "Show guide"
               :action-type "show-guide"}
              {:text        "Highlight guide"
               :action-type "highlight-guide"}]}
   {:title   "Skip activity"
    :effects [{:text        "Start skip"
               :action-type "start-skip-region"}
              {:text        "End skip"
               :action-type "end-skip-region"}]}
   {:title   "Sound effect"
    :effects [{:text        "Mute background music"
               :action-type "mute-background-music"}
              {:text        "Unute background music"
               :action-type "unmute-background-music"}]}])

(re-frame/reg-sub
  ::available-effects
  (constantly available-effects))
