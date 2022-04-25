(ns webchange.utils.module-router
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]))

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
                        (pushy/set-token! history path)))]
    (pushy/start! history)
    {:history     history
     :routes      routes
     :redirect-to redirect-to}))

(re-frame/reg-fx
  ::redirect
  (fn [{:keys [router redirect-params]}]
    (let [{:keys [redirect-to]} router]
      (apply redirect-to redirect-params))))
