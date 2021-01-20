(ns webchange.templates.utils.common)

(defn init-metadata
  [metadata template args]
  (-> template
      (assoc-in [:metadata :actions] (:actions metadata))
      (assoc-in [:metadata :unique-suffix] 0)
      (assoc-in [:metadata :history] [{:type :create :args args}])))

(defn get-replace-params
  [scene-data]
  (let [old-action-name (get-in scene-data [:metadata :last-insert])
        unique-suffix (get-in scene-data [:metadata :unique-suffix])]
    {:old-action-name old-action-name
     :unique-suffix   unique-suffix
     :new-action-name (str old-action-name "-" unique-suffix)}))

(defn merge-new-action
  [scene-data actions {:keys [old-action-name new-action-name]}]
  (-> scene-data
      (assoc-in [:actions (keyword new-action-name)] (get-in scene-data [:actions (keyword old-action-name)]))
      (update-in [:actions] merge actions)
      (assoc-in [:metadata :last-insert] new-action-name)
      (update-in [:metadata :unique-suffix] inc)))


(defn add-scene-object
  [scene-data scene-objects]
  (let [to-add (vec (filter (fn [object]
                              (= 0 (count (filter #(= % object) (flatten (:scene-objects scene-data)))))
                              ) scene-objects))]
    (if (not (= 0 (count to-add)))
      (-> scene-data
          (update-in [:scene-objects] conj to-add))
      scene-data)))

(defn add-track-action
  [scene-data {:keys [track-name type action-id]}]
  (println "track-name type action-id" track-name type action-id)
  (let
    [step {:type type, :action-id action-id}
     item (->> (get-in scene-data [:metadata :tracks])
               (map-indexed vector)
               (filter #(= (:title (second %)) track-name))
               first)]
    (cond-> scene-data
            (not (get-in scene-data [:metadata :tracks])) (assoc-in [:metadata :tracks] [])
            item (update-in [:metadata :tracks (first item) :nodes] conj step)
            (not item) (update-in [:metadata :tracks] conj {:title track-name :nodes [step]}))))