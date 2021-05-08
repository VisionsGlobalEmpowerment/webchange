(ns webchange.game-changer.steps.select-character.views-selected-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select-character.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button text-input]]))

(defn- character-name
  [{:keys [name on-change]}]
  (r/with-let [edit-mode? (r/atom false)
               handle-title-click #(reset! edit-mode? true)
               handle-enter-press (fn [new-value]
                                    (reset! edit-mode? false)
                                    (on-change new-value))
               handle-esc-press #(reset! edit-mode? false)]
    (if @edit-mode?
      [text-input {:default-value  name
                   :on-enter-press handle-enter-press
                   :on-esc-press   handle-esc-press
                   :class-name     "edit-name"}]
      [:div {:class-name "title"
             :on-click   handle-title-click}
       name])))

(defn- characters-list-item
  [{:keys [name preview on-remove on-change] :as character}]
  (let [handle-name-changed (fn [name]
                              (on-change (merge character
                                                {:name name})))]
    [:div {:class-name "characters-list-item"}
     (if (some? preview)
       [:img {:src        preview
              :class-name "preview"}]
       [:div.placeholder])
     [character-name {:name      name
                      :on-change handle-name-changed}]
     [icon-button {:icon       "remove"
                   :class-name "remove"
                   :on-click   #(on-remove character)}]]))

(defn- start-message
  []
  [:div.start-message
   "Click \"+\" to add character"])

(defn- add-button
  [{:keys [on-click]
    :or   {on-click #()}}]
  [icon-button {:icon       "add"
                :size       "big"
                :class-name "add"
                :on-click   on-click}])

(defn selected-characters-list
  [{:keys [add-disabled? data on-add-click on-remove-click on-change]}]
  (let [characters @(re-frame/subscribe [::state/characters-skins data])]
    [:div.selected-characters
     [:div.characters-list
      (when (empty? characters)
        [start-message])
      (for [[idx character] (map-indexed vector characters)]
        ^{:key idx}
        [characters-list-item (merge character
                                     {:idx       idx
                                      :on-remove on-remove-click
                                      :on-change on-change})])
      (when-not add-disabled?
        [add-button {:on-click on-add-click}])]]))
