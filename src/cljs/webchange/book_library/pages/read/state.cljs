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

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [book-id course-id]}]]
    {:dispatch-n [[::parent-state/init {:course-id course-id}]
                  [::load-book book-id]]}))

(re-frame/reg-event-fx
  ::load-book
  (fn [{:keys [_]} [_ book-id]]
    (let []
      {:dispatch-n [[::warehouse/load-scene
                     {:course-slug book-id
                      :scene-slug  "book"}
                     {:on-success [::init-scene-data book-id]}]]})))

(re-frame/reg-event-fx
  ::init-scene-data
  (fn [{:keys [_]} [_ book-id book-data]]
    (let [course-slug book-id
          scene-slug "book"
          scene-data book-data]
      {:dispatch-n [
                    [::interpreter/load-course course-slug scene-slug]
                    ;[::state/set-scene-data scene-slug scene-data]
                    ;[::state/set-current-scene-id scene-slug]
                    ;[::interpreter/load-lessons course-slug]
                    ]})))

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

;; Menu

(def menu-state-path (path-to-db [:menu-state]))

(re-frame/reg-sub
  ::menu-state
  (fn [db]
    (get-in db menu-state-path)))

(re-frame/reg-event-fx
  ::set-menu-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db menu-state-path data)}))

(re-frame/reg-sub
  ::show-menu?
  (fn []
    (re-frame/subscribe [::menu-state]))
  (fn [menu-state]
    (get menu-state :show-menu?)))
