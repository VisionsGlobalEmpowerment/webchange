(ns webchange.lesson-builder.tools.script.dialog-item.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::action-data
  :<- [::state/activity-data]
  (fn [activity-data [_ action-path]]
    (utils/get-action activity-data action-path)))

(re-frame/reg-sub
  ::action-type
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (let [{:keys [id type phrase-text]} (action-utils/get-inner-action action-data)]
      (cond
        (= (:type action-data) "parallel")
        :parallel

        ;(some #{id} available-effects-ids)
        ;:effect

        (= type "text-animation")
        :text-animation

        (some #{type} ["add-animation" "remove-animation"])
        :character-animation

        (some #{type} ["char-movement"])
        :character-movement

        (some? phrase-text)
        :phrase

        (some #{type} ["start-skip-region" "end-skip-region"])
        :skip

        (some #{type} ["mute-background-music" "unmute-background-music"])
        :background-music

        (some #{type} ["show-guide" "hide-guide" "highlight-guide"])
        :guide

        :else :unknown))))

(re-frame/reg-sub
  ::sequence-items
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (->> (utils/get-action activity-data dialog-action-path)
         (:data)
         (map-indexed (fn [idx]
                        {:id          idx
                         :action-path (-> (concat dialog-action-path [:data idx]))})))))
