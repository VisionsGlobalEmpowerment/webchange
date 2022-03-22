(ns webchange.editor-v2.activity-form.generic.components.info-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.scene.components.flipbook.decorations :as decorations]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

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
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::warehouse/load-book-categories {:on-success [::set-book-categories]}]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(defn open-info-window
  []
  (re-frame/dispatch [::open]))

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
