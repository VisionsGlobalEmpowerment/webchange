(ns webchange.editor-v2.translator.audios-block.view
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor.form-elements.wavesurfer.wave-form :refer [audio-wave-form]]
    [webchange.editor-v2.translator.audios-block.utils.scene-audios :refer [get-scene-audios]]))

(defn get-action-audio-data
  [action-data]
  (when (some #{(:type action-data)} ["audio"
                                      "animation-sequence"])
    (merge (select-keys action-data [:start :duration])
           {:key (or (get action-data :audio)
                     (get action-data :id))})))

(defn update-audios-with-action
  [scene-audios action-audio]
  (map
    (fn [audio]
      (if (= (:key audio) (:key action-audio))
        (merge audio (select-keys action-audio [:start :duration]))
        audio))
    scene-audios))

(defn audio-wave
  [{:keys [key start duration]} {:keys [selected? on-click on-change]}]
  (let [start (or start 0)
        duration (or duration 0)
        audio-data {:key   key
                    :start start
                    :end   (+ start duration)}
        form-params {:height         64
                     :on-change      #(on-change key %)
                     :show-controls? selected?}
        border-style (if selected? {:border "solid 1px #00c0ff"} {})]
    [ui/card {:style    (merge border-style
                               {:margin-bottom 8})
              :on-click #(on-click key)}
     [ui/card-content
      [ui/typography {:variant "subtitle2"
                      :color   "default"}
       key]
      [audio-wave-form audio-data form-params]]]))

(defn waves-list
  [{:keys [scene-audios current-audio-key on-wave-click on-wave-region-change]}]
  [:div
   (for [audio-data scene-audios]
     ^{:key (:key audio-data)}
     [audio-wave audio-data {:selected? (= (:key audio-data) current-audio-key)
                             :on-click  on-wave-click
                             :on-change on-wave-region-change}])])

(defn audios-block
  [{:keys [scene-data action-data on-change]}]
  [:div
   [ui/typography {:variant "h6"
                   :style   {:margin "5px 0"}}
    "Audios"]
   (let [action-audio-data (get-action-audio-data action-data)
         scene-audios (-> scene-data
                          (get-scene-audios)
                          (update-audios-with-action action-audio-data))]
     (r/with-let [current-track (r/atom (:key action-audio-data))
                  change-current-track #(reset! current-track %)]
                 [waves-list {:scene-audios          scene-audios
                              :current-audio-key     @current-track
                              :on-wave-click         change-current-track
                              :on-wave-region-change on-change}]))])