(ns webchange.templates.core
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.common-actions :as common-actions]
    [webchange.templates.common-actions-guide :as guide]))

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
        (assoc-in [:metadata :template-version] (:version metadata))
        (assoc-in [:metadata :template-name] (:name metadata))
        (assoc-in [:metadata :history] {:created data
                                        :updated []})
        (guide/with-guide-actions))))

(defn- apply-heuristics
  [updated-data]
  (cond
    (:background-music updated-data) {:common-action? true :action "background-music" :data updated-data}
    (empty? updated-data) {:common-action? true :action "background-music-remove" :data updated-data}
    (:dialog updated-data) {:action "add-dialog" :data updated-data}
    (:question-page updated-data) {:action "add-question" :data updated-data}
    (and (:type updated-data) (:text updated-data) (:image updated-data)) {:action "add-page" :data updated-data}
    (and (:stage updated-data) (:page-side updated-data)) {:action "remove-page" :data updated-data}
    (and (:page-idx-from updated-data) (:page-idx-to updated-data)) {:action "move-page" :data updated-data}
    :else (throw (Exception. (str "unknown update history" updated-data)))))

(defn- try-restore-history
  [updated]
  (->> updated
       (map apply-heuristics)
       (into [])))

(defn prepare-history
  "Check if history of this activity is absent or it is of old format."
  [{{history :history} :metadata :as scene-data}]
  (let [updated (:updated history)
        corrupted (and
                   (seq updated)
                   (-> updated first :action nil?))]
    (if corrupted
      (assoc-in scene-data [:metadata :history :updated] (try-restore-history updated))
      scene-data)))

(defn update-activity-from-template
  [scene-data {:keys [common-action? action data] :as update-data}]
  (let [template-id (get-in scene-data [:metadata :template-id])
        {:keys [template-update]} (get-in @templates [template-id])
        activity (if common-action?
                   (common-actions/update-activity scene-data action data)
                   (template-update scene-data (assoc data :action-name action)))]
    (-> activity
        (prepare-history)
        (update-in [:metadata :history :updated] conj update-data))))

(defn get-template-metadata-by-id
  [id]
  (get-in @templates [id :metadata]))

(defn metadata-from-template
  [{id :template-id}]
  (get-template-metadata-by-id id))
