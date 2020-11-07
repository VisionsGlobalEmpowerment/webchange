(ns webchange.interpreter.renderer.scene.components.group.propagate
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.state.lessons.subs :as lessons]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.utils.propagate-objects :refer [get-propagated-objects
                                                           replace-object]]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]))

(ce/reg-simple-executor :propagate-objects ::execute-propagate-objects)

(re-frame/reg-event-fx
  ::execute-propagate-objects
  (fn [{:keys [db]} [_ {object-to-propagate :id lesson-name :from :as action}]]
    "Execute `propagate-objects` action - propagate component with concepts data.

    Action params:
    :id - name of 'propagate' component.
    :from - dataset name.

    Example:
    {:type 'propagate-objects'
     :id   'cards'
     :from 'concepts-all'}"
    (let [scene-id (:current-scene db)
          object-data (get-in db [:scenes scene-id :objects (keyword object-to-propagate)])
          lesson-items (lessons/lesson-dataset-items db lesson-name)
          {:keys [objects scene-objects]} (get-propagated-objects object-data lesson-items)]
      (let [propagated-object-wrapper (->> (keyword object-to-propagate)
                                           (scene/get-scene-object db))]
        (doseq [object-name (map keyword scene-objects)]
          (create-component (merge (get-object-data scene-id object-name objects)
                                   {:parent (:container propagated-object-wrapper)})))
        ;; Assigning propagated objects to db is needed for :execute-state work
        {:db       (-> db
                       (update-in [:scenes scene-id :objects] merge objects)
                       (update-in [:scenes scene-id :scene-objects] replace-object object-to-propagate scene-objects))
         :dispatch (ce/success-event action)}))))
