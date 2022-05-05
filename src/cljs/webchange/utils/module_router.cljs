(ns webchange.utils.module-router
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]))

(def locations {:profile "/user/profile"
                :courses "/user/courses"
                :login   "/user/login"
                :logout  "/user/logout"
                :books   "/user/books"})

(defn init!
  [root-path routes dispatch]
  (let [routes [root-path routes]
        parse-url (partial bidi/match-route routes)
        history (pushy/pushy dispatch parse-url)
        url-for (partial bidi/path-for routes)
        redirect-to (fn [& args]
                      (let [key (first args)
                            path (if (= (type key) Keyword)
                                   (apply url-for (vec args))
                                   key)]
                        (pushy/set-token! history path)))
        location (fn [location-name]
                   (->> (get locations location-name)
                        (set! js/document.location)
                        (redirect-to)))]
    (pushy/start! history)
    {:history     history
     :routes      routes
     :redirect-to redirect-to
     :location    location}))

(re-frame/reg-fx
  ::redirect
  (fn [{:keys [router redirect-params]}]
    (let [{:keys [location redirect-to]} router]
      (if (-> redirect-params first (= :location))
        (apply location (rest redirect-params))
        (apply redirect-to redirect-params)))))
