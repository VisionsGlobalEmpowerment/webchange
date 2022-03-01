(ns webchange.book-library.pages.read.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as parent-state]
    [webchange.interpreter.events :as interpreter]
    [webchange.state.state :as state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:read])
       (parent-state/path-to-db)))

(def scene-slug "book")

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [book-id course-id]}]]
    (let []
      {:dispatch-n [[::parent-state/init {:course-id course-id}]
                    [::set-page-state {:show-menu?           false
                                       :book-loaded?         false
                                       :stage-ready?         false
                                       :reading-in-progress? false}]
                    [::load-book book-id]
                    [::interpreter/load-lessons book-id]]})))

(re-frame/reg-event-fx
  ::load-book
  (fn [{:keys [_]} [_ book-id]]
    (let []
      {:dispatch-n [[::warehouse/load-scene
                     {:course-slug book-id
                      :scene-slug  scene-slug}
                     {:on-success [::init-scene-data]}]]})))

(re-frame/reg-event-fx
  ::init-scene-data
  (fn [{:keys [_]} [_ book-data]]
    (let [scene-data book-data]
      {:dispatch-n [[::state/set-scene-data scene-slug scene-data]
                    [::state/set-current-scene-id scene-slug]
                    [::set-book-loaded true]]})))

;; Book

(def book-path (path-to-db [:book]))

(defn get-book
  [db]
  (get-in db book-path))

(re-frame/reg-sub
  ::book
  get-book)

(re-frame/reg-event-fx
  ::set-book
  (fn [{:keys [db]} [_ book-id book-data]]
    {:db (assoc-in db book-path {:id   book-id
                                 :data book-data})}))

;; Page state

(def page-state-path (path-to-db [:page-state]))

(re-frame/reg-sub
  ::page-state
  (fn [db]
    (get-in db page-state-path)))

(re-frame/reg-event-fx
  ::set-page-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db page-state-path data)}))

(re-frame/reg-event-fx
  ::update-page-state
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-in db page-state-path merge data-patch)}))

; book-loaded?

(re-frame/reg-sub
  ::book-loaded?
  (fn []
    (re-frame/subscribe [::page-state]))
  (fn [menu-state]
    (get menu-state :book-loaded?)))

(re-frame/reg-event-fx
  ::set-book-loaded
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-page-state {:book-loaded? value}]}))

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
  ::show-menu?
  (fn []
    [(re-frame/subscribe [::book-loaded?])
     (re-frame/subscribe [::stage-ready?])
     (re-frame/subscribe [::reading-in-progress?])])
  (fn [[book-loaded? stage-ready? reading-in-progress?]]
    (and book-loaded?
         stage-ready?
         (not reading-in-progress?))))

(re-frame/reg-sub
  ::show-loading?
  (fn []
    [(re-frame/subscribe [::book-loaded?])
     (re-frame/subscribe [::stage-ready?])])
  (fn [[book-loaded? stage-ready?]]
    (or (not book-loaded?)
        (not stage-ready?))))

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
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::state/update-scene-data scene-slug [:metadata :flipbook] {:read-page? value}]}))

(re-frame/reg-event-fx
  ::read
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::interpreter/start-scene]
                  [::set-reading-in-progress true]]}))
