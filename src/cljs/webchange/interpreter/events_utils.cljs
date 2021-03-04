(ns webchange.interpreter.events-utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]))

(defn get-component-wrapper
  [db component-name]
  (let [scene-id (:current-scene db)
        wrapper (get-in db [:transitions scene-id component-name])]
    (when (some? wrapper)
      @wrapper)))

(defn reg-simple-effect-executor
  [effect]
  (let [executor-name (->> effect (clojure.core/name) (str "execute-") (keyword))]
    (re-frame/reg-event-fx
      executor-name
      (fn [{:keys [db]} [_ action]]
        (let [component-wrapper (get-component-wrapper db (:target action))]
          (-> {:dispatch (ce/success-event action)}
              (assoc effect {:component-wrapper component-wrapper})))))
    (ce/reg-simple-executor effect executor-name)))
