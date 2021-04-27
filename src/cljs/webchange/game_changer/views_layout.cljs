(ns webchange.game-changer.views-layout
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [button timeline]]))

(defn layout
  [{:keys [title title-action timeline-items actions]
    :or   {actions []}}]
  (r/with-let [this (r/current-component)]
    [:div.game-changer-form
     [:div.title
      [:h1 title]
      (when (some? title-action)
        title-action)]
     [:div.timeline
      [timeline {:items timeline-items}]]
     (into [:div.content]
           (r/children this))
     [:div.actions
      (for [{:keys [id text handler props] :or {props {}}} actions]
        ^{:key id}
        [button (merge {:on-click handler
                        :size     "big"}
                       props)
         text])]]))
