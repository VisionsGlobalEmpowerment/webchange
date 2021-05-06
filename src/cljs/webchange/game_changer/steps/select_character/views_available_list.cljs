(ns webchange.game-changer.steps.select-character.views-available-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select-character.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- groups-list-item
  [{:keys [name value img on-click] :as group}]
  [:div {:class-name "character-item-card"
         :on-click   #(on-click group)}
   (if (some? img)
     [:img {:src        img
            :class-name "preview"}]
     [:div.placeholder])
   [:div.title name]])

(defn- groups-list
  [{:keys [on-click]}]
  (let [groups @(re-frame/subscribe [::state/available-characters-groups])]
    [:div.groups-list
     (for [{:keys [value] :as group} groups]
       ^{:key value}
       [groups-list-item (merge group
                                {:on-click on-click})])]))

(defn- skins-list-item
  [{:keys [name value img]}]
  [:div.card-wrapper
   [:div.character-item-card
    (if (some? img)
      [:img {:src        img
             :class-name "preview"}]
      [:div.placeholder])
    [:div.title name]]])

(defn- skins-list
  [{:keys [group]}]
  (let [skins @(re-frame/subscribe [::state/available-skins group])]
    [:div.skins-list
     (for [{:keys [name] :as skin} skins]
       ^{:key name}
       [skins-list-item skin])]))

(defn available-characters-list
  []
  (r/with-let [_ (re-frame/dispatch [::state/load-characters])
               current-group (r/atom nil)
               handle-group-click (fn [{:keys [value]}]
                                    (reset! current-group value))]
    (print "current-group" @current-group)

    [:div.available-characters-list
     [groups-list {:on-click handle-group-click}]
     (when (some? @current-group)
       [skins-list {:group @current-group}])]))
