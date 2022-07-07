(ns webchange.parent.pages.faq.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.routes :as routes]))

(re-frame/reg-event-fx
  ::open-students-list-page
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :students]}))
