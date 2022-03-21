(ns webchange.book-library.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.state.core :as state]))

(def routes {[""]                             :book-library
             ["/favorite"]                    :book-library-favorite
             ["/search"]                      :book-library-search
             ["/read/" [#"[\w-%]+" :book-id]] :book-library-read})

(defn dispatch-route
  [{:keys [route-params]}]
  (let [{course-id :id} route-params]
    (re-frame/dispatch [::state/set-current-course-id course-id])))

(re-frame/reg-event-fx
  ::open-book-library
  (fn [{:keys [_]} [_ {:keys [course-id]}]]
    {:dispatch-n (list [::events/redirect :book-library :id course-id])}))
