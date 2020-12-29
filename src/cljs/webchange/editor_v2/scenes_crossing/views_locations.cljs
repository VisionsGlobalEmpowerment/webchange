(ns webchange.editor-v2.scenes-crossing.views-locations
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scenes-crossing.state-locations :as state]))

(defn- scene-selector
  [{:keys [value on-change]}]
  (let [options @(re-frame/subscribe [::state/location-options])]
    [ui/select {:value     value
                :on-change #(on-change (.. % -target -value))}
     (for [{:keys [id name]} options]
       ^{:key id}
       [ui/menu-item {:value id} name])]))

(defn location-settings
  [{:keys [scene-id]}]
  (let [locations @(re-frame/subscribe [::state/scene-locations scene-id])
        handle-location-change (fn [level location]
                                 (re-frame/dispatch [::state/save-scene-location scene-id level location]))]
    [ui/list {:style {:padding-left "32px"}}
     (for [{:keys [level scene]} locations]
       ^{:key level}
       [ui/list-item
        [ui/list-item-text {:primary (str "Level " (inc level))}]
        [ui/list-item-secondary-action
         [scene-selector {:value     scene
                          :on-change #(handle-location-change level %)}]]])]))
