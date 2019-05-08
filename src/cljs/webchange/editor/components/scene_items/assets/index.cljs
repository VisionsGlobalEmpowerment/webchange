(ns webchange.editor.components.scene-items.assets.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.enums :refer [asset-types]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]))

(defn- animation-asset? [asset]
  (let [type (:type asset)]
    (some #(= type %) ["anim-text" "anim-texture"])))

(defn- update-asset
  [scene-id id state]
  (re-frame/dispatch [::events/edit-asset {:scene-id scene-id :id id :state state}]))

(defn- asset-properties-panel
  [scene-id id]
  (let [props (r/atom {})]
    (fn [scene-id id]
      (let [asset (re-frame/subscribe [::subs/scene-asset scene-id id])]
        [na/form {}
         [sa/Dropdown {:placeholder "Type" :search true :selection true :options asset-types
                       :default-value (:type @asset) :on-change #(swap! props assoc :type (.-value %2))}]
         [na/divider {}]
         [na/form-input {:label "url" :default-value (:url @asset) :on-change #(swap! props assoc :url (-> %2 .-value)) :inline? true}]
         [na/form-input {:label "size" :default-value (:size @asset) :on-change #(swap! props assoc :size (-> %2 .-value js/parseInt)) :inline? true}]
         [na/form-input {:label "alias" :default-value (:alias @asset) :on-change #(swap! props assoc :alias (-> %2 .-value)) :inline? true}]
         [na/divider {}]
         [na/form-button {:content "Save" :on-click #(do (update-asset scene-id id @props)
                                                         (re-frame/dispatch [::events/reset-asset]))}]
         ]))))

(defn- edit-asset-section
  []
  (let [{:keys [scene-id id]} @(re-frame/subscribe [::es/selected-asset])]
    (if id
      ^{:key (str scene-id id)} [asset-properties-panel scene-id id]
      [:div
       [na/button {:basic? true :content "Add existing asset" :on-click #(re-frame/dispatch [::events/select-new-asset])}]
       [na/button {:basic? true :content "Upload asset" :on-click #(re-frame/dispatch [::events/show-upload-asset-form])}]])))


(defn list-assets-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Assets"}]
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[key asset] (->> @scene :assets (filter (complement animation-asset?)) (map-indexed (fn [idx itm] [idx itm])))]
          ^{:key (str scene-id key)}
          [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "asset" :id key}
                                                                                                      clj->js
                                                                                                      js/JSON.stringify)))}
           (cond
             (= "image" (:type asset)) [sa/ItemImage {:size "tiny" :src (:url asset)}]
             (= "audio" (:type asset)) [sa/ItemImage {:size "tiny"} [na/icon {:name "music" :size "huge"}]]
             :else [sa/ItemImage {:size "tiny"} [na/icon {:name "file" :size "huge"}]])

           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/select-asset scene-id key])}
            [sa/ItemHeader {:as "a"}
             (str (:type asset))]

            [sa/ItemDescription {}
             (str (:url asset)) ]]]
          )]
       [na/divider {:clearing? true}]

       [edit-asset-section]])))
