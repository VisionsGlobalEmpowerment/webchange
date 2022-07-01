(ns webchange.utils.module-router
  (:require
    [bidi.bidi :as bidi]
    [clojure.string :as s]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]
    [webchange.utils.numbers :refer [try-parse-int]]))

(def locations {:profile "/user/profile"
                :courses "/user/courses"
                :login   "/user/login"
                :logout  "/user/logout"
                :books   "/user/books"})

(defn- parse-url-param
  [value]
  (try-parse-int value))

(defn- get-url-params
  []
  (let [search (.-search js/location)]
    (when-not (empty? search)
      (->> (-> (subs search 1)
               (s/split "&"))
           (map #(s/split % "="))
           (map (fn [[key value]]
                  [(keyword key) (parse-url-param value)]))
           (into {})))))

(defn- redirect-args->url-params
  [args]
  (->> args
       (partition 2)
       (map vec)
       (into {})
       (:url-params)))

(defn- url-params->search-string
  [params]
  (if-not (empty? params)
    (->> params
         (map (fn [[key value]]
                (str (clojure.core/name key) "=" value)))
         (s/join "&")
         (str "?"))
    ""))

(defn init!
  [root-path routes dispatch]
  (let [routes [root-path routes]
        parse-url (partial bidi/match-route routes)
        history (pushy/pushy (fn [props]
                               (->> (get-url-params)
                                    (assoc props :url-params)
                                    (dispatch)))
                             parse-url)
        url-for (partial bidi/path-for routes)
        redirect-to (fn [key & args]
                      (let [path (str (if (= (type key) Keyword)
                                        (apply url-for (concat [key] args))
                                        key)
                                      (-> args redirect-args->url-params url-params->search-string))]
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
