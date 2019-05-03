(ns webchange.editor.components.scene-items.action-templates.index
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.action-properties.core :refer [action-types]]
    [webchange.editor.events :as events]
    [webchange.subs :as subs]))

(defn list-action-templates-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Action templates"}]
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [action-type (map #(-> % :value name) action-types)]
          ^{:key action-type}
          [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "action" :id (name action-type)}
                                                                                                      clj->js
                                                                                                      js/JSON.stringify)))}

           [sa/ItemContent {}
            [sa/ItemHeader {:as "a"}
             (str action-type)]

            [sa/ItemDescription {}
             ]]]
          )]])))
