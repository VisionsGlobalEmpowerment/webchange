(ns webchange.editor-v2.translator.translator-form.audio-assets.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.db :refer [path-to-db]]
    [webchange.subs :as ws]))

(re-frame/reg-sub
  ::assets-data
  (fn [db]
    (get-in db (path-to-db [:data]) {})))

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
