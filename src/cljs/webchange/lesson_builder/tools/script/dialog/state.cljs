(ns webchange.lesson-builder.tools.script.dialog.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-sub
  ::dialog-name
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (-> (utils/get-action activity-data dialog-action-path)
        (get :phrase-description "Untitled Dialog"))))

(re-frame/reg-sub
  ::dialog-items
  :<- [::state/activity-data]
  (fn [activity-data [_ dialog-action-path]]
    (->> (utils/get-action activity-data dialog-action-path)
         (:data)
         (map-indexed (fn [idx]
                        {:id          idx
                         :action-path (-> (concat dialog-action-path [:data idx]))})))))
