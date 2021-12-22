(ns webchange.templates.utils.merge)

(defn- replace-data
  [action action-key-fn object-key-fn]
  (cond-> action
          (contains? action :target) (assoc :target (object-key-fn (:target action)))
          (and (contains? action :success)
               (string? (:success action))) (assoc :success (action-key-fn (:success action)))
          (contains? action :fail) (assoc :fail (action-key-fn (:fail action)))
          (contains? action :skip) (assoc :skip (action-key-fn (:skip action)))
          (contains? action :transition) (assoc :transition (object-key-fn (:transition action)))
          (contains? action :transitions) (assoc :transitions (map (fn [transition] (object-key-fn transition)) (:transitions action)))))

(defn- replace-name
  [data func]
  (if (vector? data) (vec (map (fn [item] (func item)) data)) (func data)))

(defn- process-action
  [{:keys [action-name action action-key-fn object-key-fn params-object-names var-object-names var-action-names all-vars-in-actions]
    :or   {all-vars-in-actions []}}]
  (cond->
    (case (:type action)
      "action" (cond-> action
                       (contains? action :id) (assoc :id (action-key-fn (:id action)))
                       (contains? action :test) (assoc :test (replace-name (:test action) object-key-fn)))
      "parallel" (assoc action :data (map (fn [value] (process-action
                                                        {:action-name         action-name
                                                         :action              value
                                                         :action-key-fn       action-key-fn
                                                         :object-key-fn       object-key-fn
                                                         :params-object-names params-object-names
                                                         :var-object-names    var-object-names
                                                         :var-action-names    var-action-names
                                                         :all-vars-in-actions all-vars-in-actions}))
                                          (:data action)))
      "sequence-data" (assoc action :data (map (fn [value] (process-action
                                                             {:action-name         action-name
                                                              :action              value
                                                              :action-key-fn       action-key-fn
                                                              :object-key-fn       object-key-fn
                                                              :params-object-names params-object-names
                                                              :var-object-names    var-object-names
                                                              :var-action-names    var-action-names
                                                              :all-vars-in-actions all-vars-in-actions}))
                                               (:data action)))
      "sequence" (assoc action :data (map (fn [value] (action-key-fn value)) (:data action)))
      "set-interval" (assoc action :action (action-key-fn (:action action)))
      "set-timeout" (assoc action :action (action-key-fn (:action action)))
      "set-variable" (cond-> action
                             (some #(= (:var-name action) %) var-object-names) (assoc :var-value (replace-name (:var-value action) object-key-fn))
                             (some #(= (:var-name action) %) var-action-names) (assoc :var-value (replace-name (:var-value action) action-key-fn))
                             (some #{(clojure.core/name action-name)} all-vars-in-actions) (assoc :var-name (replace-name (:var-name action) action-key-fn)))
      "show-question" (assoc action :data (replace-data (:data action) action-key-fn object-key-fn))
      (replace-data action action-key-fn object-key-fn))
    (contains? action :params) (assoc :params (into {} (map (fn [[key data]]
                                                              (if (some #(= key %) params-object-names)
                                                                [key (replace-name data object-key-fn)]
                                                                [key data]))
                                                            (:params action))))))

(defn- prepare-states
  [states object-key-fn]
  (into {} (map (fn [[name state]]
                  [name
                   (if (contains? state :children)
                     (assoc state :children (replace-name (:children state) object-key-fn))
                     state)]) states)))

(defn- prepare-objects
  [objects object-key-fn]
  (reduce (fn [result [key object]]
            (let [object-key (object-key-fn key)
                  prepared-object (cond-> object
                                          (contains? object :transition) (assoc :transition object-key)
                                          (contains? object :states) (assoc :states (prepare-states (:states object) object-key-fn)))]

              (cond-> result
                      true (update :objects #(assoc % (keyword object-key) prepared-object))
                      (contains? object :transition) (update :transition-map #(assoc % key (keyword object-key)))
                      true (update :object-map #(assoc % key (keyword object-key)))))
            ) {:objects {} :object-map {} :transition-map {}} objects))

(defn- prepare-actions
  ([actions action-key-fn object-key-fn params-object-names var-object-names var-action-names]
   (prepare-actions actions action-key-fn object-key-fn params-object-names var-object-names var-action-names []))
  ([actions action-key-fn object-key-fn params-object-names var-object-names var-action-names all-vars-in-actions]
   (reduce (fn [result [key action]]
             (let [
                   action-key (action-key-fn key)
                   prepared-action (process-action
                                     {:action-name         key
                                      :action              action
                                      :action-key-fn       action-key-fn
                                      :object-key-fn       object-key-fn
                                      :params-object-names params-object-names
                                      :var-object-names    var-object-names
                                      :var-action-names    var-action-names
                                      :all-vars-in-actions all-vars-in-actions})]
               (cond-> result
                       true (update :actions #(assoc % (keyword action-key) prepared-action))
                       true (update :action-map #(assoc % key (keyword action-key)))))
             ) {:actions {} :action-map {}} actions)))

(defn- process-object-actions
  [objects action-key-fn object-key-fn
   params-object-names
   var-object-names var-action-names]
  (into {} (map (fn [[key object]]
                  (if (contains? object :actions)
                    [key (assoc object :actions
                                       (into {}
                                             (map (fn [[key action]]
                                                    [key
                                                     (process-action
                                                       {:action-name         key
                                                        :action              action
                                                        :action-key-fn       action-key-fn
                                                        :object-key-fn       object-key-fn
                                                        :params-object-names params-object-names
                                                        :var-object-names    var-object-names
                                                        :var-action-names    var-action-names})])
                                                  (:actions object))))]
                    [key object])
                  ) objects)))

(defn- prepare-scene-objects
  [prepared-objects scene-objects]
  (map (fn [objects]
         (map
           (fn [object] (name (get-in prepared-objects [:object-map (keyword object)])))
           objects))
       scene-objects))

(defn- prepare-triggers
  [prepared-actions triggers]
  (into {} (map
             (fn [[key action]]
               [key (assoc action :action (name (get-in prepared-actions [:action-map (keyword (:action action))])))])
             triggers)))

(defn- prepare-tracks
  [tracks action-key-fn]
  (map (fn [track]
         (assoc track :nodes
                      (vec (map (fn [node]
                                  (if (some #{(:type node)} ["dialog" "question"])
                                    (assoc node :action-id (keyword (action-key-fn (:action-id node))))
                                    node))
                                (:nodes track))))
         ) tracks))

(defn prepare-template
  ([template round params-object-names var-object-names var-action-names]
   (prepare-template template round params-object-names var-object-names var-action-names []))
  ([template round params-object-names var-object-names var-action-names all-vars-in-actions]
   (let [action-key-fn #(str (name %) "-" round)
         object-key-fn #(str (name %) "-" round)
         prepared-objects (prepare-objects (:objects template) object-key-fn)
         prepared-actions (prepare-actions (:actions template) action-key-fn
                                           object-key-fn params-object-names var-object-names var-action-names all-vars-in-actions)
         result-objects (process-object-actions (:objects prepared-objects) action-key-fn object-key-fn
                                                params-object-names
                                                var-object-names var-action-names)
         prepared-scene-objects (prepare-scene-objects prepared-objects (:scene-objects template))
         prepared-triggers (prepare-triggers prepared-actions (:triggers template))
         template (cond-> template
                          true (assoc-in [:metadata :tracks] (prepare-tracks (get-in template [:metadata :tracks]) action-key-fn))
                          (get-in template [:metadata :last-insert]) (update-in [:metadata :last-insert] action-key-fn))]
     {:assets        (:assets template)
      :objects       result-objects
      :actions       (:actions prepared-actions)
      :scene-objects prepared-scene-objects
      :triggers      prepared-triggers
      :metadata      (:metadata template)})))

(defn- add-technical-states
  [template]
  (assoc template :objects (into {} (map
                                      (fn [[key object]]
                                        [key (update object :states merge {:hide-technical {:visible false}
                                                                           :show-technical {:visible true}})])
                                      (:objects template)))))

(defn- from-first-round?
  [object-key]
  (-> object-key
      (name)
      (.endsWith "-r0")))

(defn- hide-all-objects
  [template]
  (assoc template :objects (into {} (map
                                      (fn [[key object]]
                                        (if (from-first-round? key)
                                          [key object]
                                          [key (assoc object :visible false)]))
                                      (:objects template)))))

(defn- set-state-actions
  [objects state]
  (map (fn [[key object]]
         {:type "state" :id state :target (name key)}
         ) objects))

(defn- init-actions
  [template rounds]
  (vec (map-indexed (fn [idx round]
                      [(keyword (str "init-technical-" idx))
                       {:type "parallel"
                        :data (concat
                                (if-not (= 0 idx) (set-state-actions (:objects (get rounds (dec idx))) "hide-technical") [])
                                (set-state-actions (:objects round) "show-technical"))}]
                      ) rounds)))

(defn- exctract-start-actions
  [rounds]
  (vec (map-indexed (fn [idx round]
                      (-> (filter (fn [[name triger]] (= (:on triger) "start")) (:triggers round)) first second :action)
                      ) rounds)))

(defn- extract-stop-actions
  [rounds]
  (vec (map-indexed (fn [idx round]
                      (-> (filter (fn [[name action]] (= (:type action) "finish-activity")) (:actions round)) first first name)
                      ) rounds)))

(defn- prepare-intermediate-action
  [init-round-actions start-actions]
  (map-indexed
    (fn [idx [action-name action]]
      [(keyword (str "intermediate-action-" idx))
       {:type "sequence-data"
        :data [{:type "action" :id (name action-name)}
               {:type "action" :id (get start-actions idx)}]}])
    init-round-actions))

(defn- add-intermidiate-actions
  [merged init-round-actions intermediate-action]
  (-> merged
      (update :actions merge (into {} init-round-actions))
      (update :actions merge (into {} intermediate-action))))

(defn- add-start-trigger-actions
  [template intermediate-action]
  (assoc-in template [:triggers :start] {:on "start", :action (name (first (first intermediate-action)))}))

(defn- replace-stop-actions
  [template stop-actions intermediate-action]
  template (reduce (fn [template [idx key action]]
                     (assoc-in template [:actions (keyword (get stop-actions idx))] action))
                   template
                   (map-indexed (fn [idx [key action]] [idx key action]) (subvec (vec intermediate-action) 1))))

(defn- add-stages
  [template rounds]
  (assoc-in template [:metadata :stages]
            (vec (map-indexed (fn [idx round]
                                {:name    (str "Round " (inc idx))
                                 :objects (vec (map (fn [key] (name key)) (keys (get round :objects))))})
                              rounds))))
(defn basic-merge
  [rounds]
  (let [
        merged (reduce (fn [result item]
                         (cond-> result
                                 true (assoc :assets (concat (:assets result) (:assets item)))
                                 true (assoc :objects (merge (:objects result) (:objects item)))
                                 true (assoc :actions (merge (:actions result) (:actions item)))
                                 true (assoc :scene-objects (concat (:scene-objects result) (:scene-objects item)))
                                 true (update-in [:metadata :tracks] concat (get-in item [:metadata :tracks]))
                                 (get-in item [:metadata :last-insert]) (assoc-in [:metadata :last-insert]
                                                                                  (get-in item [:metadata :last-insert])))
                         ) {} rounds)
        init-round-actions (init-actions merged rounds)
        start-actions (exctract-start-actions rounds)
        stop-actions (extract-stop-actions rounds)
        intermediate-action (prepare-intermediate-action init-round-actions start-actions)]
    (-> merged
        (add-intermidiate-actions init-round-actions intermediate-action)
        (add-start-trigger-actions intermediate-action)
        (replace-stop-actions stop-actions intermediate-action)
        (add-technical-states)
        (hide-all-objects)
        (add-stages rounds))))
