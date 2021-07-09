(ns webchange.game-changer.steps.select_template.state
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

(re-frame/reg-sub
 ::tutorial-activity
 (fn []
   [(re-frame/subscribe [::templates-list])])
 (fn [[templates]]
   (first
    (filter (fn [{:keys [id]}]
              (= 4 id))
            templates))))

(re-frame/reg-sub
 ::templates-list-without-tutorial-activity
 (fn []
   [(re-frame/subscribe [::templates-list])])
 (fn [[templates]]
   (filterv (fn [{:keys [id]}]
              (not= 4 id))
            templates)))