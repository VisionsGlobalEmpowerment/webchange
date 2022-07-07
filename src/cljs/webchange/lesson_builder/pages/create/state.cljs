(ns webchange.lesson-builder.pages.create.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.routes :as routes]))


(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :create-activity]}))

(re-frame/reg-event-fx
  ::create-book
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :create-book]}))
