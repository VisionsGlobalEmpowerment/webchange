(ns webchange.templates.core)

(def templates (atom {}))

(defn- template-registered?
  [id name]
  (let [registered-name (-> @templates (get id) :name)]
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
   (swap! templates assoc id {:metadata        metadata
                              :template        template
                              :template-update template-update})))

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
  [scene-data data]
  (let [template-id (get-in scene-data [:metadata :template-id])
        template-update (get-in @templates [template-id :template-update])]
    (template-update scene-data data)))

(defn metadata-from-template
  [{id :template-id}]
  (get-in @templates [id :metadata]))
