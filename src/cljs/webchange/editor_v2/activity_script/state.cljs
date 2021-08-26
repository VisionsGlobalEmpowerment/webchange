(ns webchange.editor-v2.activity-script.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions :refer [prepare-phrase-actions]]

    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.utils.scene-data :as scene-data-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:activity-script])
       (vec)))

;; Window State

(def window-state-path (path-to-db [:window-state]))

(re-frame/reg-sub
  ::window-open?
  (fn [db]
    (get-in db window-state-path false)))

(re-frame/reg-event-fx
  ::open-window
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db window-state-path true)
     :dispatch [::translator-form/init-state]}))

(re-frame/reg-event-fx
  ::close-window
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db window-state-path false)}))

;; Track

(re-frame/reg-sub
  ::available-tracks
  (fn []
    [(re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[scene-data]]
    (->> (scene-data-utils/get-tracks scene-data)
         (map-indexed (fn [idx {:keys [title]}]
                        {:text title
                         :idx  idx})))))

(def current-track-path (path-to-db [:current-track]))

(re-frame/reg-sub
  ::current-track
  (fn [db]
    (get-in db current-track-path 0)))

(re-frame/reg-event-fx
  ::set-current-track
  (fn [{:keys [db]} [_ track-idx]]
    {:db (assoc-in db current-track-path track-idx)}))

;; Dialogs List

(re-frame/reg-sub
  ::dialogs-to-show
  (fn []
    [(re-frame/subscribe [::current-track])
     (re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[current-track scene-data]]
    (->> (scene-data-utils/get-track-by-index scene-data current-track)
         (:nodes)
         (filter (fn [{:keys [type]}]
                   (= type "dialog")))
         (map (fn [{:keys [action-id]}]
                [(keyword action-id)])))))

(re-frame/reg-sub
  ::script-data
  (fn []
    [(re-frame/subscribe [::dialogs-to-show])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[dialogs-paths current-concept scene-data]]
    (map (fn [dialog-path]
           (let [{:keys [available-activities phrase-description]} (get-in scene-data (concat [:actions] dialog-path))
                 available-actions (->> (scene-data-utils/get-available-effects scene-data)
                                        (concat available-activities))]
             {:title       phrase-description
              :action-path dialog-path
              :nodes       (prepare-phrase-actions {:dialog-action-path  dialog-path
                                                    :concept-data        current-concept
                                                    :scene-data          scene-data
                                                    :available-effects   available-actions
                                                    :current-action-path nil})}))
         dialogs-paths)))
