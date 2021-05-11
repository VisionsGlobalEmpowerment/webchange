(ns webchange.common.background-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]
    [webchange.utils.scene-data :refer [get-scene-background]]))

(defn path-to-db [relative-path] (concat [:background-selector] relative-path))

(re-frame/reg-event-fx
  ::load-scene
  (fn [{:keys [_]} [_ {:keys [scene-slug course-slug]}]]
    {:dispatch [::state/load-scene
                {:course-slug course-slug
                 :scene-slug  scene-slug}
                {:on-success [::load-scene-success]}]}))

(re-frame/reg-event-fx
  ::load-scene-success
  (fn [{:keys [_]} [_ data]]
    (let [[background-name background-data] (get-scene-background data)]
      {:dispatch [::set-background-data {:name background-name
                                         :data background-data}]})))

(def background-data-path (path-to-db [:background-data]))

(re-frame/reg-event-fx
  ::set-background-data
  (fn [{:keys [db]} [_ background-data]]
    {:db (assoc-in db background-data-path background-data)}))

(re-frame/reg-sub
  ::background-data
  (fn [db]
    (get-in db background-data-path)))
