(ns webchange.editor-v2.translator.translator-form.audio-assets.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.audio-assets.utils :refer [get-audio-assets-data
                                                                               get-action-audio-data
                                                                               get-current-action-audio-data]]
    [webchange.editor-v2.translator.translator-form.subs :as form-subs]
    [webchange.subs :as ws]))

(re-frame/reg-sub
  ::assets-data
  (fn [db]
    (get-in db (path-to-db [:data]) {})))

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::form-subs/current-action-data])
     (re-frame/subscribe [::assets-data])])
  (fn [[current-action assets]]
    (get-audio-assets-data (map second assets)
                           (get-current-action-audio-data current-action))))

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
