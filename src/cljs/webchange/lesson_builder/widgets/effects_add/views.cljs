(ns webchange.lesson-builder.widgets.effects-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.fold.views :refer [fold]]
    [webchange.lesson-builder.widgets.effects-add.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn- effects-list-item
  [{:keys [action-type text]}]
  [draggable {:text   text
              :action "add-effect"
              :data   {:action-type action-type}}])

(defn- effects-list
  [{:keys [effects]}]
  [draggable-list
   (for [{:keys [action-type] :as effect-data} effects]
     ^{:key action-type}
     [effects-list-item effect-data])])

(defn- effects-group
  [{:keys [component title effects]}]
  [fold {:title     title
         :expanded? true}
   (cond
     (some? effects) [effects-list {:effects effects}])])

(defn effects-add
  []
  (let [available-effects @(re-frame/subscribe [::state/available-effects])]
    [:div.widget--effects-add
     (for [{:keys [text] :as effects-group-data} available-effects]
       ^{:key text}
       [effects-group effects-group-data])]))
