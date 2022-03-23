(ns webchange.editor-v2.activity-form.generic.components.info-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.scene.components.flipbook.decorations :as decorations]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.utils.list :refer [sort-by-getters]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :info-action-modal])))

(def modal-state-path [:editor-v2 :info-action-modal :state])
(def book-categories-path [:editor-v2 :info-action-modal :book-categories])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

;; Events

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db modal-state-path true)
     :dispatch-n [[::warehouse/load-book-categories {:on-success [::set-book-categories]}]
                  [::warehouse/load-book-languages {:on-success [::set-available-languages]}]
                  [::warehouse/load-book-ages {:on-success [::set-available-ages]}]
                  [::warehouse/load-book-genres {:on-success [::set-available-genres]}]
                  [::warehouse/load-book-reading-levels {:on-success [::set-available-reading-levels]}]
                  [::warehouse/load-book-tags {:on-success [::set-available-tags]}]]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(defn open-info-window
  []
  (re-frame/dispatch [::open]))

;; Languages

(def available-languages-path (path-to-db [:available-languages]))

(re-frame/reg-sub
  ::available-languages
  (fn [db]
    (get-in db available-languages-path)))

(re-frame/reg-event-fx
  ::set-available-languages
  (fn [{:keys [db]} [_ data]]
    {:db (->> data
              (sort-by-getters [#(get-in % [:metadata :primary?] false) #(get % :name)])
              (map #(select-keys % [:name :value]))
              (assoc-in db available-languages-path ))}))

;; Book categories

(fn [db]
  (get-in db book-categories-path))

(re-frame/reg-sub
  ::book-categories
  (fn [db]
    (get-in db book-categories-path)))

(re-frame/reg-event-fx
  ::set-book-categories
  (fn [{:keys [db]} [_ data]]
    {:db (->> data
              (sort-by :name)
              (assoc-in db book-categories-path))}))

;; Ages

(def available-ages-path (path-to-db [:available-ages]))

(re-frame/reg-sub
  ::available-ages
  (fn [db]
    (get-in db available-ages-path)))

(re-frame/reg-event-fx
  ::set-available-ages
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-ages-path data)}))

;; Genres

(def available-genres-path (path-to-db [:available-genres]))

(re-frame/reg-sub
  ::available-genres
  (fn [db]
    (get-in db available-genres-path)))

(re-frame/reg-event-fx
  ::set-available-genres
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-genres-path data)}))

;; Reading levels

(def available-reading-levels-path (path-to-db [:available-reading-levels]))

(re-frame/reg-sub
  ::available-reading-levels
  (fn [db]
    (get-in db available-reading-levels-path)))

(re-frame/reg-event-fx
  ::set-available-reading-levels
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-reading-levels-path data)}))

;; Tags

(def available-tags-path (path-to-db [:available-tags]))

(re-frame/reg-sub
  ::available-tags
  (fn [db]
    (get-in db available-tags-path)))

(re-frame/reg-event-fx
  ::set-available-tags
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-tags-path data)}))

;; Book preview

(re-frame/reg-event-fx
  ::update-book-preview
  (fn [{:keys [db]} [_ callback]]
    (let [cover-object (-> @collisions/objects (get "page-cover") :object)
          shadow-object-name (decorations/crease-name 0)
          shadow-object (-> @collisions/objects (get shadow-object-name) :object)
          pages-object-name (decorations/right-pages-name 0)
          pages-object (-> @collisions/objects (get pages-object-name) :object)
          prepared-callback #(do
                               (editor-state/show-frames)
                               (utils/set-visibility shadow-object true)
                               (utils/set-visibility pages-object true)
                               (callback %))]
      (editor-state/hide-frames)
      (utils/set-visibility shadow-object false)
      (utils/set-visibility pages-object false)
      (app/take-object-screenshot cover-object callback)
      {})))
