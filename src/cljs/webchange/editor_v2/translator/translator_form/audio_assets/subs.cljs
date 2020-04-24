(ns webchange.editor-v2.translator.translator-form.audio-assets.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.audio-assets.utils-subs :refer [get-action-audio-data
                                                                                    get-prepared-audios-data]]
    [webchange.editor-v2.translator.translator-form.subs :as form-subs]
    [webchange.subs :as ws]))

(re-frame/reg-sub
  ::assets-data
  (fn [db]
    (get-in db (path-to-db [:data]) {})))

(re-frame/reg-sub
  ::selected-asset
  (fn [db]
    (get-in db (path-to-db [:selected-asset]) {})))

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::form-subs/current-action-data])
     (re-frame/subscribe [::assets-data])
     (re-frame/subscribe [::selected-asset])])
  (fn [[current-action assets selected-asset]]
    (println "selected-asset" selected-asset)
    (let [assets-data (map second assets)
          action-data (:data current-action)
          action-audio-data (get-action-audio-data action-data assets-data)
          audios-data (get-prepared-audios-data assets-data selected-asset action-audio-data)]
      (println "action-data" action-data)
      (println "action-audio-data" action-audio-data)
      (println "audios-data" audios-data)
      audios-data)))

(re-frame/reg-sub
  ::available-targets
  (fn []
    [(re-frame/subscribe [::ws/current-scene-objects])])
  (fn [[scene-objects]]
    (->> scene-objects
         (filter (fn [[_ {:keys [type]}]]
                   (= type "animation")))
         (map first)
         (map name))))
