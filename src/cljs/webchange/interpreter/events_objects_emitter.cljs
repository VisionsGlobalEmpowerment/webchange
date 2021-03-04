(ns webchange.interpreter.events-objects-emitter
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.object-data.get-object-data :refer [get-object-data]]
    [webchange.interpreter.renderer.scene.app :refer [get-stage]]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.utils.counter :refer [set-countdown]]
    [webchange.logger.index :as logger]
    [webchange.utils.scene-data :refer [generate-name rename-object]]))

(ce/reg-simple-executor :create-object ::execute-create-object)

(re-frame/reg-event-fx
  ::execute-create-object
  [ce/event-as-action ce/with-flow]
  (fn [{:keys [_]} action]
    {:create-object action}))

(defn- set-transition
  [data object-name]
  (assoc-in data [(keyword object-name) :transition] object-name))

(defn- parse-data
  [data root-object-name]
  (get-object-data nil root-object-name data))

(defn- emit-object
  [stage {:keys [data root-object target on-emit] :as props}]
  (let [new-root-object (generate-name root-object)
        parent (if (some? target)
                 (.getChildByName stage target true)
                 stage)
        prepared-data (-> data
                          (set-transition root-object)
                          (rename-object root-object new-root-object)
                          (parse-data new-root-object)
                          (assoc :parent parent))
        emit-event-handler (merge on-emit
                                  {:display-name :on-emit
                                   :params       {:transition new-root-object}})]

    (logger/group-folded "emit object" (keyword new-root-object))
    (logger/trace "stage:" stage)
    (logger/trace "parent:" parent)
    (logger/trace "props:" props)
    (logger/trace "prepared object data:" prepared-data)
    (logger/trace "on-emit:" emit-event-handler)
    (logger/group-end "emit object" (keyword new-root-object))

    (if (some? parent)
      (do (create-component prepared-data)
          (re-frame/dispatch [::ce/execute-action emit-event-handler]))
      (logger/error "Emit object failed: parent is not defined"))))

(re-frame/reg-fx
  :create-object
  (fn [{:keys [emit-interval max-count] :as action}]
    (let [stage (get-stage)]
      (if (some? emit-interval)
        (set-countdown {:interval           emit-interval
                        :count              max-count
                        :start-immediately? true
                        :on-progress        (fn []
                                              (emit-object stage action))})
        (emit-object stage action)))))
