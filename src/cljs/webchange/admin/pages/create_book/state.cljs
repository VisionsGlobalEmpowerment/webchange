(ns webchange.admin.pages.create-book.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.validate :refer [validate]]
    [webchange.validation.specs.book :as book-spec]))

(def path-to-db :page/create-book)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc :illustrators-number 1)
             (assoc-in [:form :illustrators] [""])
             (assoc :authors-number 1)
             (assoc-in [:form :authors] [""]))}))

(re-frame/reg-sub
  ::title
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :cover-title])))

(re-frame/reg-event-fx
  ::change-title
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ title]]
    {:db (assoc-in db [:form :cover-title] title)}))

(re-frame/reg-sub
  ::language
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :lang])))

(re-frame/reg-event-fx
  ::change-lang
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ lang]]
    {:db (assoc-in db [:form :lang] lang)}))

(re-frame/reg-sub
  ::authors-number
  :<- [path-to-db]
  (fn [db]
    (get db :authors-number)))

(re-frame/reg-event-fx
  ::add-author
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (update :authors-number inc)
             (update-in [:form :authors] concat [""]))}))

(re-frame/reg-sub
  ::authors
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :authors])))

(re-frame/reg-event-fx
  ::change-author
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    {:db (assoc-in db [:form :authors idx] value)}))

(re-frame/reg-sub
  ::illustrators-number
  :<- [path-to-db]
  (fn [db]
    (get db :illustrators-number)))

(re-frame/reg-event-fx
  ::add-illustrator
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (update :illustrators-number inc)
             (update-in [:form :illustrators] concat [""]))}))

(re-frame/reg-sub
  ::illustrators
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :illustrators])))

(re-frame/reg-event-fx
  ::change-illustrator
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    {:db (assoc-in db [:form :illustrators idx] value)}))

;; Build
(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  (fn [db]
    (get db :errors)))

(re-frame/reg-event-fx
  ::build
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [lang (or (get-in db [:form :lang]) "english")
          defaults {:lang lang}
          data (merge defaults
                      (get db :form))
          validation-errors (validate ::book-spec/create-book data)]
      (if (nil? validation-errors)
        {:db       (assoc db :saving true)
         :dispatch [::warehouse/create-book
                    {:data data}
                    {:on-success [::build-success]
                     :on-failure [::build-failure]}]}
        {:db (assoc db :errors validation-errors)}))))

(re-frame/reg-event-fx
  ::build-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:db       (assoc db :saving true)
     :dispatch [::routes/redirect :lesson-builder :activity-id id]}))

(re-frame/reg-event-fx
  ::build-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))

(re-frame/reg-event-fx
  ::back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :create]}))


;; Cover image

(re-frame/reg-sub
  ::cover-image
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :cover-image :src])))

(re-frame/reg-event-fx
  ::upload-cover
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ file]]
    {:db       (assoc db :uploading true)
     :dispatch [::warehouse/upload-file
                {:file        file
                 :form-params [["type" "image"]]}
                {:on-success [::upload-cover-success]
                 :on-failure [::upload-cover-failure]}]}))

(re-frame/reg-event-fx
  ::upload-cover-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [url]}]]
    {:db (-> db
             (assoc :uploading false)
             (assoc-in [:form :cover-image :src] url))}))

(re-frame/reg-event-fx
  ::upload-cover-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-finish]} result]]
    {:db (assoc db :uploading false)}))


(re-frame/reg-sub
  ::layout
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :cover-layout])))

(re-frame/reg-event-fx
  ::choose-layout
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ layout]]
    {:db (assoc-in db [:form :cover-layout] layout)}))
