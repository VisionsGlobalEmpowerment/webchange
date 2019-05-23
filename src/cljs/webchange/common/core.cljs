(ns webchange.common.core
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.common.events :as ce]))

(defn prepare-action
  [action]
  (let [type (:on action)]
    (if (= type "click")
      {:on-click #(re-frame/dispatch [::ce/execute-action action])
       :on-tap #(re-frame/dispatch [::ce/execute-action action])}
      {(keyword (str "on-" (:on action))) #(re-frame/dispatch [::ce/execute-action action])})))

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