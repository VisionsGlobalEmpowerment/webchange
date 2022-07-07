(ns webchange.lesson-builder.pages.wizard.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :create-activity]}))

(re-frame/reg-event-fx
  ::create-book
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :create-book]}))
