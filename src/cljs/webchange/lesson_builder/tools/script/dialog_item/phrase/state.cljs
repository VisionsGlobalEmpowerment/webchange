(ns webchange.lesson-builder.tools.script.dialog-item.phrase.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-action-validator :refer [validate-action]]))

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
  ::phrase-text
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (let [inner-action (action-utils/get-inner-action action-data)]
      (get inner-action :phrase-text))))

(re-frame/reg-event-fx
  ::set-phrase-text
  (fn [_ [_ action-path phrase-text]]
    {:dispatch [::stage-actions/set-action-phrase-text {:action-path action-path
                                                        :phrase-text phrase-text}]}))

(re-frame/reg-sub
  ::has-issue?
  (fn [[_ action-path]]
    (re-frame/subscribe [::state/action-data action-path]))
  (fn [action-data]
    (let [inner-action (action-utils/get-inner-action action-data)]
      (-> (validate-action "animation-sequence" inner-action) :valid? not))))

;; remove

(re-frame/reg-event-fx
  ::remove
  (fn [_ [_ action-path]]
    {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this phrase action?"
                                                     :on-confirm [::stage-actions/remove-action {:action-path action-path}]}]}))
