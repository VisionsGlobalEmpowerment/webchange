(ns webchange.game-changer.templates-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-templates :as state-templates]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::state-templates/load-templates]}))

(re-frame/reg-sub
  ::templates-list
  (fn []
    [(re-frame/subscribe [::state-templates/templates-list])])
  (fn [[templates]]
    (filter (fn [{:keys [props]
                  :or   {props {}}}]
              (get props :game-changer? false))
            templates)))
