(ns webchange.state.warehouse-native
  (:require
    [clojure.string :as string]
    [clojure.edn :as edn]
    [re-frame.core :as re-frame]
    [webchange.capacitor :as capacitor]))

(def loaded-data (atom nil))
(def state (atom {}))

(def default-school
  {:name "Offline mode"
   :id 1})

(defn- load-data!
  []
  (-> (js/fetch "/responses.edn")
      (.then #(.text %))
      (.then #(edn/read-string %))
      (.then #(reset! loaded-data %))
      (.then #(re-frame/dispatch [:complete-loading-data]))))

(comment
  (load-data!)
  (get @loaded-data "api-activities-923-current-version"))

(defn- success-response
  [{:keys [on-success]} response]
  (re-frame/dispatch (conj on-success response)))

(defn student-login
  [{:keys [params] :as request}]
  (let [user {:id (:access-code params)
              :first-name (:access-code params)
              :last-name (:access-code params)
              :course-slug "english"
              :school-id (:id default-school)}]
    (swap! state assoc :current-user user)
    (success-response request user)))

(defn load-progress
  [request]
  (let [current-user-id (-> @state :current-user :id)
        filename (str "current-progress-" current-user-id ".json")]
    (-> (capacitor/read-file filename)
        (.then #(js/JSON.parse (.-data %)))
        (.then #(js->clj % :keywordize-keys keyword))
        (.then #(success-response request %))
        (.catch #(success-response request {})))))

(defn save-progress
  [{:keys [params] :as request}]
  (let [current-user-id (-> @state :current-user :id)
        filename (str "current-progress-" current-user-id ".json")]
    (-> (capacitor/write-file filename (js/JSON.stringify (clj->js params)))
        (.then #(success-response request {:message "ok"}))
        (.catch #()))))

(def handlers
  {:student-login student-login
   :load-progress-data load-progress
   :save-progress-data save-progress
   :load-lesson-sets #(success-response % {})
   :load-schools #(success-response % {:schools [default-school]})
   :load-current-user #(success-response % (-> @state :current-user))})

(defn- prepare-uri
  [uri]
  (-> uri
      (string/replace \/ \-)
      (string/replace #"^-" "")))

(defn- cached-http-effect
  [{:keys [key method uri on-success on-failure] :as request}]
  (let [handler (get handlers key)]
    (if (fn? handler)
      (handler request)
      (if-let [data (get-in @loaded-data [:responses method (prepare-uri uri)])]
        (do
          (js/console.log "cached response found for" uri method)
          (if (fn? on-success)
            (on-success data)
            (re-frame/dispatch (conj on-success data))))
        (do
          (js/console.log "cached response not found for" uri method)
          (re-frame/dispatch (conj on-failure {:message "Not found"})))))))

(defn init!
  []
  (re-frame/dispatch [:start-loading-data])
  (load-data!)
  (re-frame/reg-fx :cached-http-xhrio cached-http-effect))
