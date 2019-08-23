(ns webchange.common.core
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.common.events :as ce]
    [clojure.string :as s]))

(defn format-date [date]
  (let [pad (fn [number] (if (< number 10) (str "0" number) (str number)))
        year (.getUTCFullYear date)
        month (inc (.getUTCMonth date))
        day (.getUTCDate date)]
    (str year "-" (pad month) "-" (pad day))))

(defn format-date-string [date-string]
  (if (not (s/blank? date-string))
    (format-date (js/Date. date-string))))

(defn prepare-action-data
  [action event]
  (let [event-param-name (keyword (:pick-event-param action))
        params (if event-param-name
                 (merge (:params action) (hash-map event-param-name (get event event-param-name)))
                 (:params action))]
    (merge action {:params params})))

(defn prepare-action
  [action]
  (let [type (:on action)]
    (if (= type "click")
      {:on-click #(re-frame/dispatch [::ce/execute-action action])
       :on-tap #(re-frame/dispatch [::ce/execute-action action])}
      {(keyword (str "on-" (:on action))) #(re-frame/dispatch [::ce/execute-action (prepare-action-data action %)])})))

(defn prepare-actions
  [{:keys [actions] :as object}]
  (->> actions
       (map second)
       (map #(assoc % :var (:var object)))
       (map prepare-action)
       (into {})
       (merge object)))

(defn with-origin-offset
  [{:keys [width height origin] :as object}]
  (let [{:keys [type]} origin]
    (case type
      "center-center" (-> object
                          (assoc :offset {:x (/ width 2) :y (/ height 2)}))
      "center-top" (-> object
                       (assoc :offset {:x (/ width 2)}))
      "center-bottom" (-> object
                          (assoc :offset {:x (/ width 2) :y height}))
      object)))

(defn with-transition
  [{:keys [transition] :as object}]
  (if transition
    (let [component (r/atom nil)]
      (re-frame/dispatch [::ie/register-transition transition component])
      (assoc object :ref (fn [ref] (reset! component ref))))
    object))

(defn with-draggable
  [{:keys [draggable] :as object}]
  (if draggable
    (assoc object :draggable true)
    object))

(defn prepare-group-params
  [object]
  (-> object
      prepare-actions
      with-origin-offset
      with-transition
      with-draggable))

(defn prepare-painting-area-params
  [object]
  (-> object
      (merge {:key (:var-name object)
              :on-change #(re-frame/dispatch [::vars.events/execute-set-progress {:var-name  (:var-name object)
                                                                                  :var-value %}])})))

(defn prepare-colors-palette-params
  [object]
  (-> object
      prepare-actions))

(defn prepare-animated-svg-path-params
  [object]
  (-> object
      (merge {:data (:path object)})
      (dissoc :path)))
