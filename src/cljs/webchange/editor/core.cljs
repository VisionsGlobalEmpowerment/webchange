(ns webchange.editor.core)

(defn to-action [id]
  {:type "action" :id (name id)})

(defn parallel-from [actions keys]
  (let [data (->> keys
                  (filter #(contains? actions %))
                  (map to-action)
                  vec)]
    {:type "parallel"
     :data data}))

(defn combine-to-parallel [actions keys name]
  (let [new-action (parallel-from actions keys)]
    (assoc actions (keyword name) new-action)))

(defn sequence-from [actions keys]
  (let [data (->> keys
                  (filter #(contains? actions %))
                  (map name)
                  vec)]
    {:type "sequence"
     :data data}))

(defn combine-to-sequence [actions keys name]
  (let [new-action (sequence-from actions keys)]
    (assoc actions (keyword name) new-action)))

(defn parallel-data-from [actions keys]
  (let [data (->> keys
                  (filter #(contains? actions %))
                  (map #(get actions %))
                  vec)]
    {:type "parallel"
     :data data}))

(defn convert-to-parallel [actions keys name]
  (let [new-action (parallel-data-from actions keys)]
    (-> actions
        (#(apply dissoc % keys))
        (assoc (keyword name) new-action))))

(defn sequence-data-from [actions keys]
  (let [data (->> keys
                  (filter #(contains? actions %))
                  (map #(get actions %))
                  vec)]
    {:type "sequence-data"
     :data data}))

(defn convert-to-sequence [actions keys name]
  (let [new-action (sequence-data-from actions keys)]
    (-> actions
        (#(apply dissoc % keys))
        (assoc (keyword name) new-action))))

