(ns webchange.lesson-builder.tools.script.dialog-item.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(re-frame/reg-sub
  ::action-data
  :<- [::state/activity-data]
  (fn [activity-data [_ action-path]]
    (utils/get-action activity-data action-path)))

(comment
  (let [action-path [:dialog-intro :data 2]]
    @(re-frame/subscribe [::action-data action-path])))

(re-frame/reg-sub
  ::action-selected?
  :<- [::script-state/selected-action]
  (fn [selected-action [_ action-path]]
    (= selected-action action-path)))

(re-frame/reg-sub
  ::action-type
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (let [{:keys [type phrase-text phrase-placeholder] :as inner-action} (action-utils/get-inner-action action-data)]
      (cond
        (= (:type action-data) "parallel")
        :parallel

        (= type "text-animation")
        :text-animation

        (some #{type} ["add-animation" "remove-animation"])
        :character-animation

        (some #{type} ["char-movement"])
        :character-movement

        (action-utils/call-question-action? inner-action)
        :question

        (or (some? phrase-text)
            (some? phrase-placeholder))
        :phrase

        :else :effect-general))))

(re-frame/reg-sub
  ::sequence-items
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (->> (utils/get-action activity-data dialog-action-path)
         (:data)
         (map-indexed (fn [idx]
                        {:id          idx
                         :action-path (-> (concat dialog-action-path [:data idx]))})))))
