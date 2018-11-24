(ns webchange.interpreter.variables.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    ))

(defn set-variable
  [db var-name value]
  (let [scene-id (:current-scene db)]
    (assoc-in db [:scenes scene-id :variables var-name] value)))

(defn set-variables
  [db vars]
  (reduce (fn [db [k v]] (set-variable db k v)) db vars))

(re-frame/reg-event-fx
  ::set-variable
  (fn [{:keys [db]} [_ var-name var]]
    {:db (set-variable db var-name var)}))

(re-frame/reg-event-fx
  ::execute-vars-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables]}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          items (->> from
                     (map #(get-in scene [:variables %]))
                     shuffle)]
      {:db (->> (zipmap variables items)
                (set-variables db))})))

(re-frame/reg-event-fx
  ::execute-dataset-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables]}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          items (-> (get-in scene [:datasets (keyword from)])
                    vals
                    shuffle)]
      {:db (->> (zipmap variables items)
                (set-variables db))})))