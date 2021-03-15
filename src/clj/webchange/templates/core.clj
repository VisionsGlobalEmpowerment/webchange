(ns webchange.templates.core
  (:require [webchange.templates.common-actions :as common-actions]
            [clojure.tools.logging :as log]))

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
    (log/debug "Create activity" id data)
    (-> (template data)
        (assoc-in [:metadata :template-id] id)
        (assoc-in [:metadata :template-name] (:name metadata))
        (assoc-in [:metadata :history] {:created data
                                        :updated []}))))

(defn prepare-history
  "Check if history of this activity is absent or it is of old format."
  [{{history :history} :metadata :as scene-data}]
  (if (map? history)
    scene-data
    (-> scene-data
        (assoc-in [:metadata :history-old] history)
        (assoc-in [:metadata :history] {:created {}
                                        :updated []}))))

(defn update-activity-from-template
  [scene-data {:keys [common-action? action data]}]
  (let [template-id (get-in scene-data [:metadata :template-id])
        {:keys [template-update]} (get-in @templates [template-id])
        activity (if common-action?
                   (common-actions/update-activity scene-data action data)
                   (template-update scene-data (assoc data :action-name action)))]
    (log/debug "Update activity" template-id action data)
    (-> activity
        (prepare-history)
        (update-in [:metadata :history :updated] conj data))))

(defn metadata-from-template
  [{id :template-id}]
  (get-in @templates [id :metadata]))
