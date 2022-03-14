(ns webchange.templates.library.recording-studio.generate-actions)

(defn- get-action-name
  [object action]
  (str action "-" object))

(defn- get-action-description
  [object action]
  (str (clojure.string/capitalize action)
       " "
       (clojure.string/replace object #"-" " ")))

(defn- add-available-action
  [activity-data {:keys [object action action-data]}]
  (let [action-name (get-action-name object action)
        action-description (get-action-description object action)]
    (-> activity-data
        (update-in [:actions] assoc (keyword action-name) action-data)
        (update-in [:metadata :available-actions] conj {:action action-name
                                                        :name   action-description}))))

(defn add-control-actions
  [activity-data object-name]
  (let [highlight-filters [{:name  "brightness"
                            :value 0}
                           {:name           "glow"
                            :outer-strength 0
                            :color          0xffd700}]
        highlight-action {:type               "transition"
                          :transition-id      object-name
                          :return-immediately true
                          :from               {:brightness 0 :glow 0}
                          :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}

        show-action {:type "set-attribute" :target object-name :attr-name "visible" :attr-value true}
        hide-action {:type "set-attribute" :target object-name :attr-name "visible" :attr-value false}]
    (-> activity-data
        (update-in [:objects (keyword object-name)] assoc :transition object-name)
        (update-in [:objects (keyword object-name)] assoc :filters highlight-filters)
        (add-available-action {:object      object-name
                               :action      "highlight"
                               :action-data highlight-action})
        (add-available-action {:object      object-name
                               :action      "show"
                               :action-data show-action})
        (add-available-action {:object      object-name
                               :action      "hide"
                               :action-data hide-action}))))

;;; Replace

(defn add-backward-compatibility-action
  [activity-data legacy-action-name object-name action]
  (let [current-action-name (get-action-name object-name action)
        updated-available-actions (map (fn [{:keys [action] :as available-action}]
                                         (if (= action current-action-name)
                                           (update available-action :synonyms conj legacy-action-name)
                                           available-action))
                                       (get-in activity-data [:metadata :available-actions] []))]
    (-> activity-data
        (assoc-in [:actions (keyword legacy-action-name)] {:type "action" :id current-action-name})
        (assoc-in [:metadata :available-actions] updated-available-actions))))
