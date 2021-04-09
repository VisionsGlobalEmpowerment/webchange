(ns webchange.book-creator.pages.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.state :as db]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.state.state-flipbook :as state-flipbook]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:book-stages-list])
       (db/path-to-db)))

(re-frame/reg-sub
  ::move-page-running?
  (fn [db]
    (get-in db (path-to-db [:move-page-running?]) false)))

(re-frame/reg-event-fx
  ::set-move-page-running
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:move-page-running?]) value)}))

(re-frame/reg-event-fx
  ::move-page
  (fn [{:keys [_]} [_ source-page target-page relative-position]]
    {:dispatch-n [[::set-move-page-running true]
                  [::state-flipbook/move-page source-page target-page relative-position
                   {:on-success [::set-move-page-running false]}]]}))

(re-frame/reg-sub
  ::pages-list-disabled?
  (fn []
    [(re-frame/subscribe [::move-page-running?])
     (re-frame/subscribe [::state-flipbook/generate-screenshots-running?])
     (re-frame/subscribe [::state-renderer/rendering?])])
  (fn [[move-page-running? generate-screenshots-running? rendering?]]
    (or move-page-running? generate-screenshots-running? rendering?)))

(re-frame/reg-sub
  ::show-generated-pages?
  (fn [db]
    (get-in db (path-to-db [:show-generated-pages?]) true)))

(re-frame/reg-event-fx
  ::set-show-generated-pages?
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:show-generated-pages?]) value)
     :dispatch [::state-flipbook/generate-stages-screenshots {:hide-generated-pages? (not value)}]}))
