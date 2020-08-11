(ns webchange.editor.components.scene-items.animations.index
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.events :as events]
    [webchange.subs :as subs]))

(defn list-animations-panel
  []
  ;(let [scene-id @(re-frame/subscribe [::subs/current-scene])]
  ;  (fn []
  ;    [na/segment {}
  ;     [na/header {:as "h4" :floated "left" :content "Animations"}]
  ;     [:div {:style {:float "right"}}
  ;      [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
  ;     [na/divider {:clearing? true}]
  ;
  ;     [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
  ;      (for [[animation data] animations]
  ;        ^{:key animation}
  ;        [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "animation" :id (name animation)}
  ;                                                                                                    clj->js
  ;                                                                                                    js/JSON.stringify)))}
  ;
  ;         [sa/ItemContent {}
  ;          [sa/ItemHeader {:as "a"}
  ;           (str animation)]
  ;
  ;          [sa/ItemDescription {}
  ;           [:div {:style {:width "100px" :height "100px"} :ref #(when % (init-spine-player % (name animation)))}] ]]]
  ;        )]]))
  )
