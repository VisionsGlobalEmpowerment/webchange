(ns webchange.editor-v2.dialog.components.audio-assets.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]))

(re-frame/reg-sub
  ::audios-list
  (fn []
    (re-frame/subscribe [::state-scene/audio-assets]))
  (fn [audio-assets]
    (->> audio-assets
         (map (fn [{:keys [alias url] :as audio-asset}]
                (-> (select-keys audio-asset [:url :alias :date :size])
                    (assoc :name (or alias url)))))
         (sort-by :date)
         (reverse))))
