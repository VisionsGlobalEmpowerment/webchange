(ns webchange.game-changer.steps.select-character.views-available-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select-character.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- groups-list-item
  [{:keys [name img on-click] :as group}]
  [:div {:class-name "groups-list-item"
         :on-click   #(on-click group)}
   (if (some? img)
     [:img {:src        img
            :class-name "preview"}]
     [:div.placeholder])
   [:div.title name]])

(defn- groups-list
  [{:keys [on-click]}]
  (let [groups @(re-frame/subscribe [::state/available-characters-groups])]
    [:div
     [:div.list-title
      "Select animation group:"]
     [:div.groups-list
      (for [{:keys [value] :as group} groups]
        ^{:key value}
        [groups-list-item (merge group
                                 {:on-click on-click})])]]))

(defn- skins-list-item
  [{:keys [name img on-click] :as skin}]
  [:div {:class-name "skins-list-item"
         :on-click   #(on-click skin)}
   (if (some? img)
     [:img {:src        img
            :class-name "preview"}]
     [:div.placeholder])
   [:div.title name]])

(defn- skins-list
  [{:keys [group on-click]}]
  (let [skins @(re-frame/subscribe [::state/available-characters-skins group])]
    [:div
     [:div.list-title
      "Select skin:"]
     [:div.skins-list
      (for [{:keys [name] :as skin} skins]
        ^{:key name}
        [skins-list-item (merge skin
                                {:on-click on-click})])]]))

(defonce counter (atom 0))

(defn- generate-name
  [{:keys [group-title skin-title]}]
  (swap! counter inc)
  (cond-> (str group-title " " skin-title)
          (> @counter 1) (str " " @counter)))

(defn available-characters-list
  [{:keys [on-click]
    :or   {on-click #()}}]
  (r/with-let [_ (re-frame/dispatch [::state/load-characters])
               current-group (r/atom nil)
               handle-group-click (fn [group]
                                    (reset! current-group group))
               handle-skin-click (fn [skin]
                                   (on-click {:name     (generate-name {:group-title (:name @current-group)
                                                                        :skin-title  (:name skin)})
                                              :skeleton (:value @current-group)
                                              :skin     (:value skin)}))]
    [:div.available-characters-list
     (when-not (some? @current-group)
       [groups-list {:on-click handle-group-click}])
     (when (some? @current-group)
       [skins-list {:group    (:value @current-group)
                    :on-click handle-skin-click}])]))
