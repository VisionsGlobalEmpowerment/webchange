(ns webchange.lesson-builder.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.utils.flipbook :as flipbook-utils]))

(def path-to-db :lesson-builder/flipbook)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current stage

(def current-stage-key :current-stage)

(defn- set-current-stage
  [db value]
  (assoc db current-stage-key value))

(re-frame/reg-sub
  ::current-stage
  :<- [path-to-db]
  #(get % current-stage-key 0))

;; actions

(re-frame/reg-event-fx
  ::show-flipbook-stage
  [(re-frame/inject-cofx :activity-data)
   (re-frame/inject-cofx :registered-transitions)
   (i/path path-to-db)]
  (fn [{:keys [activity-data transitions db]} [_ stage-idx {:keys [hide-generated-pages?]}]]
    (let [book-name (flipbook-utils/get-book-object-name activity-data)
          component-wrapper (get transitions book-name)]
      (when (some? component-wrapper)
        {:db                   (set-current-stage db stage-idx)
         :flipbook-show-spread {:component-wrapper     @component-wrapper
                                :spread-idx            stage-idx
                                :hide-generated-pages? hide-generated-pages?}}))))

(re-frame/reg-fx
  :flipbook-show-spread
  (fn [{:keys [component-wrapper spread-idx hide-generated-pages?]}]
    ((:show-spread component-wrapper) spread-idx {:hide-generated-pages? hide-generated-pages?})))
