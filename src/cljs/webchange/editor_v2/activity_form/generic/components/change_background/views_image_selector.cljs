(ns webchange.editor-v2.activity-form.generic.components.change-background.views-image-selector
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.change-background.common :refer [available-layers]]
    [webchange.editor-v2.activity-form.generic.components.change-background.state :as background]))

(defn- get-styles
  []
  (let [{:keys [width height]} @(re-frame/subscribe [::background/dimension])
        height-percentage (* (/ height width) 100)]
    {:gallery              {:display         "flex"
                            :flex-wrap       "wrap"
                            :justify-content "space-between"
                            :width           "600px"}
     :gallery-tile         {:cursor        "pointer"
                            :margin-bottom "20px"
                            :width         "30%"}
     :gallery-tile-image   {:padding-top (str height-percentage "%")}
     :gallery-tile-remove  {:height  "100%"
                            :padding "16px"
                            :display "flex"}
     :gallery-tile-button  {:width "100%"}
     :layer-card           {:cursor         "pointer"
                            :padding-bottom "72px"
                            :position       "relative"
                            :width          "30%"}
     :layer-image          {:padding-top (str height-percentage "%")}
     :layer-button         {:top "4px"}
     :layer-button-wrapper {:bottom     0
                            :position   "absolute"
                            :text-align "center"
                            :width      "100%"}
     :selector-wrapper     {:display         "flex"
                            :justify-content "space-between"
                            :margin-bottom   "15px"}}))

(defn- select-image-tile
  [{:keys [image on-click]}]
  (let [styles (get-styles)]
    [ui/card {:on-click #(on-click image)
              :style    (:gallery-tile styles)}
     [ui/card-media {:image image
                     :style (:gallery-tile-image styles)}]]))

(defn- remove-image-tile
  [{:keys [image on-click]}]
  (let [styles (get-styles)]
    [ui/card {:on-click #(on-click image)
              :style    (:gallery-tile styles)}
     [ui/card-content {:style (:gallery-tile-remove styles)}
      [ui/button {:style (:gallery-tile-button styles)} "Remove"]]]))

(defn- select-image-window
  []
  (let [window-open? @(re-frame/subscribe [::background/gallery-window-open?])
        images @(re-frame/subscribe [::background/current-source-gallery])
        handle-cancel (fn [] (re-frame/dispatch [::background/close-gallery-window]))
        handle-pick (fn [image]
                      (re-frame/dispatch [::background/set-current-layer image])
                      (re-frame/dispatch [::background/close-gallery-window]))
        styles (get-styles)]
    [ui/dialog {:open     window-open?
                :on-close handle-cancel}
     [ui/dialog-title "Select Background"]
     [ui/dialog-content {:style (:gallery styles)}
      (for [{:keys [src thumb] :as image} images]
        ^{:key src}
        [select-image-tile {:image    thumb
                            :on-click #(handle-pick image)}])
      [remove-image-tile {:on-click #(handle-pick nil)}]]
     [ui/dialog-actions
      [ui/button {:on-click handle-cancel} "Cancel"]]]))

(defn- layer-image-button
  [{:keys [type title on-click]}]
  (let [{:keys [thumb]} @(re-frame/subscribe [::background/layer-image type])
        styles (get-styles)]
    [ui/card {:on-click on-click
              :style    (:layer-card styles)}
     (when-not (empty? thumb)
       [ui/card-media {:image thumb
                       :style (:layer-image styles)}])
     [ui/card-content {:style (:layer-button-wrapper styles)}
      [ui/button {:style (:layer-button styles)} title]]]))

(defn image-selector
  []
  (let [current-type @(re-frame/subscribe [::background/background-type])
        layers (get available-layers current-type)
        handle-layer-click (fn [type] (re-frame/dispatch [::background/open-gallery-window type]))
        styles (get-styles)]
    [:div {:style (:selector-wrapper styles)}
     (for [{:keys [type title]} layers]
       ^{:key type}
       [layer-image-button {:type     type
                            :title    title
                            :on-click #(handle-layer-click type)}])
     [select-image-window]]))
