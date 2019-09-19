(ns webchange.interpreter.components.utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [camel-snake-kebab.core :refer [->camelCase]]))

(defn- prepare-action-data
  [action event]
  (let [event-param-name (keyword (:pick-event-param action))
        params (if event-param-name
                 (merge (:params action) (hash-map event-param-name (get event event-param-name)))
                 (:params action))]
    (merge action {:params params})))

(defn- actions->event-handlers
  [actions]
  (reduce
    (fn [result action]
      (assoc result (:on action) #(re-frame/dispatch [::ce/execute-action (prepare-action-data action %)])))
    {}
    (vals actions)))

(defn- add-mobile-handlers
  [handlers]
  (let [event-map {"click" "tap"}]
    (reduce
      (fn [result [event handler]]
        (assoc
          (if (contains? event-map event)
            (assoc result (get event-map event) handler)
            result)
          event handler))
      {}
      (seq handlers))))

(defn add-events-listeners
  [node actions]
  (doseq [[event handler] (->> actions
                               actions->event-handlers
                               add-mobile-handlers
                               seq)]
    (.on node event (clj->js handler)))
  node)

(defn map->konva-params
  [{:keys [actions] :as params}]
  (let [attr (dissoc params :actions)]
    {:actions    actions
     :attributes (->> attr
                      (seq)
                      (reduce
                        (fn [result [key value]]
                          (assoc result (->camelCase key) value))
                        {})
                      (clj->js))}))
