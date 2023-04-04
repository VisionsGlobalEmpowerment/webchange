(ns webchange.book-library.pages.read.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.book-library.state :as parent-state]
    [webchange.interpreter.events :as interpreter]
    [webchange.state.state :as state]
    [webchange.state.state-progress :as progress-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.numbers :refer [try-parse-int]]))

(def path-to-db :book-library-read-book)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [book-id course-id]}]]
    (let [book-id (try-parse-int book-id)]
      {:db (-> db
               (assoc :book-id book-id)
               (assoc :course-id course-id)
               (assoc :page-state {:show-menu?           false
                                   :book-loaded?         false
                                   :stage-ready?         false
                                   :reading-in-progress? false
                                   :error                nil}))
       :dispatch-n [[::parent-state/init {:course-id course-id}]
                    [::state/set-current-scene-id book-id]
                    [::warehouse/load-activity-current-version
                     {:activity-id book-id}
                     {:on-success [::init-scene-data]
                      :on-failure [::load-book-failed]}]]})))

(re-frame/reg-event-fx
  ::init-scene-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ book-data]]
    (let [scene-id (:book-id db)]
      {:db (-> db
               (assoc-in [:page-state :book-loaded?] true))
       :dispatch [::state/set-scene-data scene-id book-data]})))

(re-frame/reg-event-fx
  ::load-book-failed
  (fn [{:keys [_]} [_ _]]
    {:dispatch [::set-error {:message "Book loading failed"}]}))

;; Page state

(re-frame/reg-sub
  ::page-state
  :<- [path-to-db]
  (fn [db]
    (get db :page-state)))

(re-frame/reg-event-fx
  ::update-page-state
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update db :page-state merge data-patch)}))

; book-loaded?

(re-frame/reg-sub
  ::book-loaded?
  (fn []
    (re-frame/subscribe [::page-state]))
  (fn [menu-state]
    (get menu-state :book-loaded?)))

;; error

(re-frame/reg-sub
  ::error
  (fn []
    (re-frame/subscribe [::page-state]))
  (fn [menu-state]
    (get menu-state :error)))

(re-frame/reg-event-fx
  ::set-error
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-page-state {:error value}]}))

; stage-ready?

(re-frame/reg-sub
  ::stage-ready?
  (fn []
    (re-frame/subscribe [::page-state]))
  (fn [menu-state]
    (get menu-state :stage-ready?)))

(re-frame/reg-event-fx
  ::set-stage-ready
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-page-state {:stage-ready? value}]}))

; reading-in-progress?

(re-frame/reg-sub
  ::reading-in-progress?
  (fn []
    (re-frame/subscribe [::page-state]))
  (fn [menu-state]
    (get menu-state :reading-in-progress?)))

(re-frame/reg-event-fx
  ::set-reading-in-progress
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-page-state {:reading-in-progress? value}]}))

; show-menu?

(re-frame/reg-sub
  ::show-buttons?
  (fn []
    [(re-frame/subscribe [::book-loaded?])
     (re-frame/subscribe [::stage-ready?])
     (re-frame/subscribe [::reading-in-progress?])])
  (fn [[book-loaded? stage-ready? reading-in-progress?]]
    (and book-loaded?
         stage-ready?
         (not reading-in-progress?))))

(re-frame/reg-sub
  ::show-error?
  (fn []
    [(re-frame/subscribe [::error])])
  (fn [[error]]
    (some? error)))

(re-frame/reg-sub
  ::show-loading?
  (fn []
    [(re-frame/subscribe [::book-loaded?])
     (re-frame/subscribe [::stage-ready?])
     (re-frame/subscribe [::show-error?])])
  (fn [[book-loaded? stage-ready? show-error?]]
    (and (not show-error?)
         (or (not book-loaded?)
             (not stage-ready?)))))

;; Menu

(re-frame/reg-event-fx
  ::read-with-sound
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-read-page true]
                  [::read]]}))

(re-frame/reg-event-fx
  ::read-without-sound
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-read-page false]
                  [::read]]}))

(re-frame/reg-event-fx
  ::set-read-page
  (fn [{:keys [db]} [_ value]]
    (let [scene-slug (:current-scene db)]
      {:dispatch [::state/update-scene-data scene-slug [:metadata :flipbook] {:read-page? value}]})))

(re-frame/reg-event-fx
  ::read
  (fn [{:keys [db]} [_]]
    {:db (progress-state/set-course-in-progress db false)
     :dispatch-n [[::interpreter/trigger :start]
                  [::set-reading-in-progress true]]}))
