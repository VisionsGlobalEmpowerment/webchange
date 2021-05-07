(ns webchange.game-changer.steps.select-character.views-selected-list
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.select-character.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- characters-list-item
  [{:keys [name preview on-remove] :as character}]
  [:div {:class-name "characters-list-item"}
   (if (some? preview)
     [:img {:src        preview
            :class-name "preview"}]
     [:div.placeholder])
   [:div.title name]
   [icon-button {:icon       "remove"
                 :class-name "remove"
                 :on-click   #(on-remove character)}]])

(defn- add-button
  [{:keys [on-click]
    :or   {on-click #()}}]
  [icon-button {:icon       "add"
                :size       "big"
                :class-name "add"
                :on-click   on-click}])

(defn selected-characters-list
  [{:keys [data on-add-click on-remove-click]}]
  (let [characters @(re-frame/subscribe [::state/characters-skins data])]
    [:div.selected-characters
     [:div.characters-list
      (for [[idx character] (map-indexed vector characters)]
        ^{:key idx}
        [characters-list-item (merge character
                                     {:idx       idx
                                      :on-remove on-remove-click})])
      [add-button {:on-click on-add-click}]]]))