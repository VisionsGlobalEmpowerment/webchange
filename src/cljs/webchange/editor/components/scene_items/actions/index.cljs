(ns webchange.editor.components.scene-items.actions.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.action-properties.core :refer [action-types]]
    [webchange.editor.enums :refer [actions-with-selected-actions]]
    [webchange.editor.events :as events]
    [webchange.subs :as subs]))

(defn list-actions-panel []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])
        selected (r/atom #{})
        props (r/atom {})
        errors (r/atom {})]
    (fn []
      [na/segment {}
       [na/header {}
        [sa/Menu {:size "mini" :secondary true}
         [sa/MenuItem {:header true} "Actions"]
         [sa/MenuItem {}
          [na/form {:size "mini"}
           [na/form-input {:placeholder "Name" :on-change #(do
                                                             (swap! errors dissoc :name)
                                                             (swap! props assoc :name (-> %2 .-value)))
                           :error? (:name @errors)}]]]
         [sa/MenuItem {:position "right"}
          [sa/Dropdown {:text "New" :options action-types :scrolling true
                        :on-change #(if (:name @props)
                                      (re-frame/dispatch [::events/add-new-scene-action (:name @props) (-> %2 .-value keyword)])
                                      (swap! errors assoc :name true))}]]
         [sa/MenuItem {:position "right"}
          [sa/Dropdown {:text "Selected" :options actions-with-selected-actions
                        :direction "left"
                        :on-change #(if (:name @props)
                                      (re-frame/dispatch [::events/process-selected-actions @selected (:name @props) (-> %2 .-value keyword)])
                                      (swap! errors assoc :name true))}]]
         [sa/MenuItem {:position "right"}
          [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
         ]
        ]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[key action] (sort-by first (:actions @scene))]
          ^{:key (str scene-id key)}
          [sa/Item {}
           [sa/ItemImage {:size "mini"}
            [na/checkbox {:on-change #(swap! selected (fn [selected]
                                                        (if (.-checked %2)
                                                          (conj selected key)
                                                          (disj selected key))))}]]
           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/show-scene-action key])}
            [sa/ItemHeader {:as "a"}
             (str key)]

            [sa/ItemDescription {}
             (str "type: " (:type action)) ]
            ]]
          )]])))