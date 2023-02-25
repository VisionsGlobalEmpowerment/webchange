(ns webchange.utils.module-router
  (:require
    [bidi.bidi :as bidi]
    [clojure.edn :as edn]
    [clojure.string :as s]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]
    [webchange.utils.numbers :refer [try-parse-int]]
    [webchange.utils.session-storage :as storage]))

(def prompt-before-leave (atom false))
(def text-before-leave "You have unsaved changes. Are you sure you want to leave the page?")

(defn set-before-leave!
  []
  (reset! prompt-before-leave true)
  (let [text text-before-leave 
        event-handler (fn [event]
                        (aset event "returnValue" text)
                        text)]
    (aset js/window "onbeforeunload" event-handler)))

(defn reset-before-leave!
  []
  (reset! prompt-before-leave false)
  (aset js/window "onbeforeunload" nil))

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

(defn- redirect-args->params
  [args]
  (->> args
       (partition 2)
       (map vec)
       (into {})))

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
        history (pushy/pushy (fn [{:keys [handler] :as props}]
                               (let [storage-params (-> (storage/get-item handler)
                                                        (edn/read-string))
                                     url-params (get-url-params)]
                                 (storage/remove-item handler)
                                 (-> (cond-> props
                                             (some? storage-params) (update-in [:route-params :params] merge storage-params)
                                             (some? url-params) (update-in [:route-params :params] merge url-params))
                                     (dispatch))))
                             parse-url)
        url-for (partial bidi/path-for routes)
        get-path (fn [key & args]
                   (str (if (= (type key) Keyword)
                          (apply url-for (concat [key] args))
                          key)
                        (-> args redirect-args->params :url-params url-params->search-string)))
        redirect-to (fn [key & args]
                      (when (or (not @prompt-before-leave) (js/window.confirm text-before-leave))
                        (reset-before-leave!)
                        (when-let [params (-> args redirect-args->params :storage-params)]
                          (storage/set-item key params))
                        (let [path (apply get-path (concat [key] args))]
                          (pushy/set-token! history path))))
        location (fn [location-name]
                   (->> (get locations location-name)
                        (set! js/document.location)
                        (redirect-to)))]
    (pushy/start! history)
    {:history     history
     :routes      routes
     :redirect-to redirect-to
     :location    location
     :get-path    get-path}))

(re-frame/reg-fx
  ::redirect
  (fn [{:keys [router redirect-params]}]
    (let [{:keys [location redirect-to]} router]
      (if (-> redirect-params first (= :location))
        (apply location (rest redirect-params))
        (apply redirect-to redirect-params)))))
