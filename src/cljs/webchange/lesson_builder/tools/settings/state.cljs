(ns webchange.lesson-builder.tools.settings.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :lesson-builder/settings)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-info)
   (re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-info activity-data]} [_]]
    (let [{:keys [name lang preview]} activity-info
          guide-settings (get-in activity-data [:metadata :guide-settings])
          animation-settings (get-in activity-data [:metadata :animation-settings])]
      {:db (-> db
               (assoc :panel :main-settings)
               (assoc :preview preview)
               (assoc :activity-settings {:name name
                                          :lang lang})
               (assoc :guide-settings guide-settings)
               (assoc :animation-settings animation-settings))})))

(re-frame/reg-sub
  ::panel
  :<- [path-to-db]
  (fn [db]
    (get db :panel :main-settings)))

(re-frame/reg-sub
  ::activity-name
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :name])))

(re-frame/reg-sub
  ::language
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :lang])))

(re-frame/reg-event-fx
  ::open-activity-settings
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :panel :activity-settings)}))

(re-frame/reg-event-fx
  ::open-create-preview-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :panel :create-preview)}))

(re-frame/reg-event-fx
  ::back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :panel :main-settings)}))

(re-frame/reg-sub
  ::preview
  :<- [path-to-db]
  (fn [db]
    (get-in db [:preview])))

(re-frame/reg-event-fx
  ::create-preview
  (fn [{:keys [db]} _]
    (let [upload-asset (fn [file]
                         (editor-state/show-frames)
                         (re-frame/dispatch [::warehouse/upload-file
                                             {:file        file
                                              :form-params [["type" "image"]]}
                                             {:on-success [::upload-success]
                                              :on-failure [::upload-failure]}]))]
      (editor-state/hide-frames)
      (app/take-screenshot upload-asset {:extract-canvas? false :render? true})
      {:db (assoc db :uploading? true)})))

(re-frame/reg-event-fx
  ::upload-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [url]}]]
    {:db (-> db
             (assoc :uploading false)
             (assoc :preview url))}))

(re-frame/reg-event-fx
  ::upload-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :uploading false)}))

;; apply

(def saving-key :saving?)

(defn- set-saving
  [db value]
  (assoc db saving-key value))

(re-frame/reg-sub
  ::saving?
  :<- [path-to-db]
  #(get % saving-key false))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db (set-saving true))
     :dispatch [::stage-actions/update-activity-settings
                {:data {:preview            (:preview db)
                        :activity-settings  (:activity-settings db)
                        :guide-settings     (:guide-settings db)
                        :animation-settings (:animation-settings db)}}
                {:on-success [::apply-success]
                 :on-failure [::apply-failure]}]}))

(re-frame/reg-event-fx
  ::apply-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-saving false))}))

(re-frame/reg-event-fx
  ::apply-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-saving false))}))

(re-frame/reg-sub
  ::show-guide
  :<- [path-to-db]
  (fn [db]
    (get-in db [:guide-settings :show-guide] true)))

(re-frame/reg-sub
  ::character
  :<- [path-to-db]
  (fn [db]
    (get-in db [:guide-settings :character])))

(re-frame/reg-event-fx
  ::set-show-guide
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:guide-settings :show-guide] value)}))

(re-frame/reg-event-fx
  ::set-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:guide-settings :character] value)}))

(re-frame/reg-sub
  ::animations-on
  :<- [path-to-db]
  (fn [db]
    (get-in db [:animation-settings :idle-animation-enabled?] true)))

(re-frame/reg-event-fx
  ::set-animations-on
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:animation-settings :idle-animation-enabled?] value)}))


(comment
  @(re-frame/subscribe [path-to-db]))
