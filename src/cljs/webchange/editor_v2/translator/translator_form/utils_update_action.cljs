(ns webchange.editor-v2.translator.translator-form.utils-update-action)

(defn- get-update-path
  [current-path edited-field action-data]
  (let [single-action? (not (some #{(:type action-data)} ["parallel" "sequence-data"]))
        path-without-action-name (-> current-path rest vec)
        path-prefix (if single-action? [] [:data])]
    (vec (concat path-prefix
                 path-without-action-name
                 [edited-field]))))

(defn- default-action-data
  [selected-action-concept? action-name concept-data scene-data]
  (let [action-data (if selected-action-concept?
                      (get-in concept-data [:data action-name])
                      (get-in scene-data [:actions action-name]))]
    action-data))

(defn- get-scene-action-data
  [selected-node-data]
  (let [action-name (-> selected-node-data :name keyword)
        action-data (:data selected-node-data)]
    [:scene nil action-name action-data]))

(defn- get-concept-action-data
  [selected-node-data current-concept-data]
  (let [action-id (:id current-concept-data)
        action-name (-> selected-node-data :name keyword)
        action-data (:data selected-node-data)]
    [:concept action-id action-name action-data]))

(defn- update-with-current-data
  [path action-id node-action-data data-store]
  (let [action-name (first path)
        edited-action-data (get-in data-store [[action-name action-id] :data])
        single-action? (not (some #{(:type edited-action-data)} ["parallel" "sequence-data"]))
        path-without-action-name (-> path rest vec)
        path-prefix (if single-action? [] [:data])
        inner-path (vec (concat path-prefix path-without-action-name))
        edited-node-action-data (get-in edited-action-data inner-path)]
    (merge node-action-data edited-node-action-data)))

(defn get-current-action-data
  [selected-node-data current-concept-data data-store]
  (let [concept-action? (get-in selected-node-data [:data :concept-action])
        [type id name data] (if concept-action?
                              (get-concept-action-data selected-node-data current-concept-data)
                              (get-scene-action-data selected-node-data))]
    {:id   id
     :name name
     :type type
     :data (update-with-current-data (-> selected-node-data :path) id data data-store)}))

(defn- fix-data-patch
  [data-patch selected-action-node]
  (if (and (= "audio" (get-in selected-action-node [:data :type]))
           (contains? data-patch :audio))
    (-> data-patch
        (assoc :id (:audio data-patch))
        (dissoc :audio))
    data-patch))

(defn get-action-update-data
  [{:keys [scene-data data-store current-concept selected-action-node original-action-name data-patch]}]
  (let [{:keys [id type]} (get-current-action-data selected-action-node current-concept data-store)
        path (:path selected-action-node)
        selected-action-concept? (-> selected-action-node (get-in [:data :concept-action]) (boolean))
        action-name (first path)
        current-data (get-in data-store [[action-name id] :data] (default-action-data selected-action-concept? action-name current-concept scene-data))
        new-data (reduce
                   (fn [current-data [field value]]
                     (let [field-path (get-update-path path field current-data)]
                       (assoc-in current-data field-path value)))
                   current-data
                   (fix-data-patch data-patch selected-action-node))]
    (when (= original-action-name action-name)
      {:id   id
       :type type
       :name action-name
       :data new-data})))
