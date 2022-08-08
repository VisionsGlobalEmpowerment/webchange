(ns webchange.lesson-builder.tools.object-form.video-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.tools.object-form.state :as object-form-state :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    (let [scale (-> (get-in db [:objects object-name :scale :x])
                    (js/Math.abs))]
      {:db (assoc-in db [:values object-name :scale] scale)})))

(re-frame/reg-sub
  ::volume
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :volume] 0.5)))

(re-frame/reg-event-fx
  ::set-volume
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    (js/console.log "volume" value)
    {:db (assoc-in db [:objects object-name :volume] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:volume value}]}))
