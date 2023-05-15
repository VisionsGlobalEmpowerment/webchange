(ns webchange.lesson-builder.tools.script.dialog-item.text-animation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as lesson-builder]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as activity-utils]
    [webchange.utils.text :refer [text->chunks]]))

;; target

(re-frame/reg-sub
  ::target
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (let [inner-action (action-utils/get-inner-action action-data)]
      (get inner-action :target))))

(re-frame/reg-event-fx
  ::set-target
  (fn [_ [_ action-path target]]
    {:dispatch [::stage-actions/set-action-target {:action-path action-path
                                                   :target      target}]}))

;; phrase text

(re-frame/reg-sub
  ::text
  (fn [[_ action-path]]
    [(re-frame/subscribe [::lesson-builder/activity-data])
     (re-frame/subscribe [::state/action-data action-path])])
  (fn [[activity-data action-data]]
    (let [{:keys [target]} (action-utils/get-inner-action action-data)]
      (-> (activity-utils/get-scene-object activity-data target)
          (get :text)))))

(re-frame/reg-event-fx
  ::set-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ action-path text]]
    (let [{:keys [target]} (-> (activity-utils/get-action activity-data action-path)
                               (action-utils/get-inner-action))]
      {:dispatch-n [[::stage-actions/set-object-text {:object-name target
                                                      :text        text
                                                      :chunks      (text->chunks text)}]
                    [::state-renderer/set-scene-object-state (keyword target) {:text text}]]})))

(re-frame/reg-sub
  ::has-issue?
  (fn [[_ action-path]]
    [(re-frame/subscribe [::lesson-builder/activity-data])
     (re-frame/subscribe [::state/action-data action-path])])
  (fn [[activity-data action-data]]
    (let [{:keys [target] :as inner-action-data} (action-utils/get-inner-action action-data)
          text-chunks (-> (activity-utils/get-scene-object activity-data target)
                          (get :chunks))
          action-chunks-numbers (->> inner-action-data
                                     :data
                                     (map :chunk))]
      (->> (range 0 (count text-chunks))
           (map (fn [idx]
                  (->> action-chunks-numbers
                       (some #{idx})
                       (boolean))))
           (reduce #(and %1 %2))
           (not)))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this text animation action?"
                                                     :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))

