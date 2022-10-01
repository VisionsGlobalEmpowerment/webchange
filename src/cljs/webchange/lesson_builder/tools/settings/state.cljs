(ns webchange.lesson-builder.tools.settings.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]
    [webchange.interpreter.renderer.scene.components.flipbook.decorations :as decorations]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.flipbook :refer [flipbook-activity?]]))

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
    (let [{:keys [name lang preview metadata]} activity-info
          guide-settings (get-in activity-data [:metadata :guide-settings])
          animation-settings (get-in activity-data [:metadata :animation-settings])
          flipbook? (flipbook-activity? activity-data)]
      {:db (-> db
               (assoc :panel :main-settings)
               (assoc :preview preview)
               (assoc :activity-settings {:name name
                                          :lang lang
                                          :metadata metadata})
               (assoc :guide-settings guide-settings)
               (assoc :animation-settings animation-settings)
               (assoc :flipbook? flipbook?))})))

(re-frame/reg-sub
  ::flipbook?
  :<- [path-to-db]
  (fn [db]
    (get db :flipbook? false)))

(re-frame/reg-sub
  ::panel
  :<- [path-to-db]
  (fn [db]
    (get db :panel :main-settings)))

(re-frame/reg-event-fx
  ::set-activity-name
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :name] value)}))

(re-frame/reg-event-fx
  ::set-language
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :lang] value)}))

(re-frame/reg-event-fx
  ::set-ages
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :ages] value)}))

(re-frame/reg-event-fx
  ::set-genres
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :genres] value)}))

(re-frame/reg-event-fx
  ::set-reading-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :reading-level] value)}))

(re-frame/reg-event-fx
  ::set-categories
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :categories] value)}))

(re-frame/reg-event-fx
  ::set-tags
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :tags] value)}))

(re-frame/reg-event-fx
  ::set-about
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :about] value)}))

(re-frame/reg-event-fx
  ::set-short-description
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:activity-settings :metadata :short-description] value)}))

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

(re-frame/reg-sub
  ::ages
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :ages])))

(re-frame/reg-sub
  ::genres
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :genres])))

(re-frame/reg-sub
  ::reading-level
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :reading-level])))

(re-frame/reg-sub
  ::categories
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :categories])))

(re-frame/reg-sub
  ::tags
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :tags])))

(re-frame/reg-sub
  ::about
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :about])))

(re-frame/reg-sub
  ::short-description
  :<- [path-to-db]
  (fn [db]
    (get-in db [:activity-settings :metadata :short-description])))

(re-frame/reg-event-fx
  ::open-activity-settings
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :panel :activity-settings)}))

(re-frame/reg-event-fx
  ::open-book-settings
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :panel :book-settings)}))

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
  ::cancel
  [(re-frame/inject-cofx :activity-info)
   (re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-info activity-data]} [_]]
    (let [{:keys [name lang preview metadata]} activity-info
          guide-settings (get-in activity-data [:metadata :guide-settings])
          animation-settings (get-in activity-data [:metadata :animation-settings])
          flipbook? (flipbook-activity? activity-data)]
      {:db (-> db
               (assoc :panel :main-settings)
               (assoc :preview preview)
               (assoc :activity-settings {:name name
                                          :lang lang
                                          :metadata metadata})
               (assoc :guide-settings guide-settings)
               (assoc :animation-settings animation-settings)
               (assoc :flipbook? flipbook?))})))

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
    {:db (-> db
             (set-saving false)
             (assoc :panel :main-settings))}))

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


;; Book preview

(re-frame/reg-event-fx
  ::create-book-preview
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:get-book-screenshot {:on-success [::book-preview-ready]
                           :on-failure [::book-preview-failed]}
     :db (assoc db :uploading? true)}))

(re-frame/reg-event-fx
  ::book-preview-ready
  (fn [{:keys [_]} [_ file]]
    {:dispatch [::warehouse/upload-file
                {:file        file
                 :form-params [["type" "image"]]}
                {:on-success [::upload-success]
                 :on-failure [::upload-failure]}]}))

(re-frame/reg-event-fx
  ::book-preview-failed
  (fn [{:keys [db]} [_ error]]
    {:db (assoc db :uploading? false)}))

(re-frame/reg-fx
  :get-book-screenshot
  (fn [{:keys [on-success on-failure]}]
    (try
      (let [cover-object (-> @collisions/objects (get "page-cover") :object)
            shadow-object-name (decorations/crease-name 0)
            shadow-object (-> @collisions/objects (get shadow-object-name) :object)
            pages-object-name (decorations/right-pages-name 0)
            pages-object (-> @collisions/objects (get pages-object-name) :object)]
        (editor-state/hide-frames)
        (utils/set-visibility shadow-object false)
        (utils/set-visibility pages-object false)
        (app/take-object-screenshot cover-object #(re-frame/dispatch (conj on-success %))))
      (catch js/Error e
        (when (some? on-failure)
          (re-frame/dispatch (conj on-failure e)))))))

(comment
  @(re-frame/subscribe [path-to-db]))
