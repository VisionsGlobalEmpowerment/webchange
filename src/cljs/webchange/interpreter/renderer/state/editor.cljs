(ns webchange.interpreter.renderer.state.editor
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as scene]))

(defn- object-name->wrapper-name
  [object-name]
  (if-not (nil? object-name)
    (-> object-name (name) (str "-editor") (keyword))
    nil))

(re-frame/reg-event-fx
  ::register-object
  (fn [{:keys [_]} [_ object-wrapper]]
    (let [wrapper-name (object-name->wrapper-name (:name object-wrapper))]
      {:dispatch [::scene/register-object (assoc object-wrapper :name wrapper-name)]})))

(re-frame/reg-event-fx
  ::select-object
  (fn [{:keys [db]} [_ previous-target current-target]]
    {:select-object [(->> previous-target (object-name->wrapper-name) (scene/get-scene-object db))
                     (->> current-target (object-name->wrapper-name) (scene/get-scene-object db))]}))

(re-frame/reg-fx
  :select-object
  (fn [[previous-target current-target]]
    (when previous-target (let [{:keys [deselect]} previous-target] (deselect)))
    (when current-target (let [{:keys [select]} current-target] (select)))))
