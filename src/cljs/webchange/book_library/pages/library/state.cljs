(ns webchange.book-library.pages.library.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as parent-state]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch [::parent-state/init {:course-id course-id}]}))
