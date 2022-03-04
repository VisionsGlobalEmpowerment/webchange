(ns webchange.book-library.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]))

(def routes {[""]                             :book-library
             ["/favorite"]                    :book-library-favorite
             ["/search"]                      :book-library-search
             ["/read/" [#"[\w-%]+" :book-id]] :book-library-read})

(re-frame/reg-event-fx
  ::open-book-library
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch-n (list [::events/redirect :book-library :id course-id])}))
