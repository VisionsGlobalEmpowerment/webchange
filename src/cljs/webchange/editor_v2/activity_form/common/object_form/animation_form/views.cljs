(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.components.scale.views :refer [scale-component]]
    [webchange.editor-v2.components.character-form.views :as animation-form]))

(defn- character
  [{:keys [class-name id]}]
  (let [{:keys [character skin head clothes]} @(re-frame/subscribe [::state/current-character-data id])
        handle-change (fn [{:keys [name skin-params change-skeleton?]}]
                        (if change-skeleton?
                          (re-frame/dispatch [::state/set-skeleton id name skin-params])
                          (re-frame/dispatch [::state/set-current-skin-names id skin-params])))]
    [animation-form/form {:character  character
                          :skin       skin
                          :head       head
                          :clothes    clothes
                          :on-change  handle-change
                          :class-name class-name}]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div.animation-form
     [character {:id id}]
     [scale-component {:id         id
                       :class-name "scale-group"}]]))
