(ns webchange.editor-v2.layout.components.background.views-preview
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.background.common :refer [available-layers]]
    [webchange.editor-v2.layout.components.background.state :as background]))

(defn- get-styles
  []
  (let [{:keys [width height]} @(re-frame/subscribe [::background/dimension])
        height-percentage (* (/ height width) 100)]
    {:background-container {:padding-bottom (str height-percentage "%")
                            :position       "relative"
                            :width          "100%"}
     :background-layer     {:position "absolute"
                            :top      0
                            :left     0
                            :width    "100%"}}))

(defn- background-layer
  [{:keys [src]}]
  (let [styles (get-styles)]
    [:img {:src   src
           :style (:background-layer styles)}]))

(defn preview
  []
  (let [background-layers @(re-frame/subscribe [::background/background-layers])
        layers-images (->> @(re-frame/subscribe [::background/background-type])
                           (get available-layers)
                           (map :type)
                           (map (fn [type] (merge (get background-layers type)
                                                  {:type type}))))
        styles (get-styles)]

    [ui/paper {:style (:background-container styles)}
     (for [{:keys [src type]} layers-images]
       (when-not (empty? src)
         ^{:key type}
         [background-layer {:src src}]))]))
