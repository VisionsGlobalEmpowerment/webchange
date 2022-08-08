(ns webchange.lesson-builder.tools.object-form.text-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.tools.object-form.state :as object-form-state :refer [path-to-db]]
    [webchange.utils.text :refer [text->chunks]]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    (let [scale (-> (get-in db [:objects object-name :scale :x])
                    (js/Math.abs))]
      {:db (assoc-in db [:values object-name :scale] scale)})))

(re-frame/reg-sub
  ::text
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :text])))

(re-frame/reg-event-fx
  ::set-text
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    (let [has-chunks? (-> (get-in db [:objects object-name :chunks]) (some?))]
      {:db (cond-> db
                   :always (assoc-in [:objects object-name :text] value)
                   has-chunks? (assoc-in [:objects object-name :chunks] (text->chunks value)))
       :dispatch [::state-renderer/set-scene-object-state object-name {:text value}]})))

;; Font Family

(re-frame/reg-sub
  ::font-family
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :font-family] "")))

(re-frame/reg-event-fx
  ::set-font-family
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :font-family] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:font-family value}]}))

;; Font Size

(re-frame/reg-sub
  ::font-size
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :font-size] "")))

(re-frame/reg-event-fx
  ::set-font-size
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :font-size] (js/parseInt value))
     :dispatch [::state-renderer/set-scene-object-state object-name {:font-size (js/parseInt value)}]}))

;; Font Color

(re-frame/reg-sub
  ::font-color
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :fill] "")))

(re-frame/reg-event-fx
  ::set-font-color
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :fill] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:fill value}]}))

;; Text Align

(re-frame/reg-sub
  ::text-align
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :align] "")))

(re-frame/reg-event-fx
  ::set-text-align
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (assoc-in db [:objects object-name :align] value)
     :dispatch [::state-renderer/set-scene-object-state object-name {:align value}]}))
