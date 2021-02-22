(ns webchange.templates.core)

(def templates (atom {}))

(defn- template-registered?
  [id name]
  (let [registered-name (-> @templates (get id) :metadata :name)]
    (and registered-name (not= name registered-name))))

(defn register-template
  ([metadata template]
   (register-template metadata template nil))
  ([{:keys [id name] :as metadata} template template-update]
   (when (template-registered? id name)
     (throw (new Exception
                 (str "Failed to register " name "!"
                      " Template with id " id " already registered"
                      " (" (-> @templates (get id) :metadata :name) ")"))))
   (let [{:keys [handler props]} (if (fn? template-update)
                                   {:handler template-update}
                                   template-update)]
     (swap! templates assoc id {:metadata              metadata
                                :template              template
                                :template-update       handler
                                :template-update-props props}))))

(defn get-available-templates
  []
  (->> @templates
       (map second)
       (map :metadata)))

(defn activity-from-template
  [{id :template-id :as data}]
  (let [{:keys [template metadata]} (get-in @templates [id])]
    (-> (template data)
        (assoc-in [:metadata :template-id] id)
        (assoc-in [:metadata :template-name] (:name metadata)))))

(defn update-activity-from-template
  [scene-data {:keys [action data]}]
  (let [template-id (get-in scene-data [:metadata :template-id])
        {:keys [template-update template-update-props]} (get-in @templates [template-id])]
    (if (:with-action? template-update-props)
      (template-update scene-data data action)
      (template-update scene-data data))))

(defn metadata-from-template
  [{id :template-id}]
  (get-in @templates [id :metadata]))
