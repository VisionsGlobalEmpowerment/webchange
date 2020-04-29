(ns webchange.editor-v2.translator.translator-form.audio-assets.utils)

(defn get-action-audio-data
  [{:keys [type data] :as action-data}]
  (cond
    (= type "audio") [(merge {:id (:id action-data)}
                             (select-keys action-data [:target :start :duration]))]
    (= type "animation-sequence") [(merge {:url (:audio action-data)}
                                          (select-keys action-data [:target :start :duration]))]
    (or (= type "parallel")
        (= type "sequence-data")) (->> data
                                       (map get-action-audio-data)
                                       (flatten)
                                       (distinct))
    :else []))

(defn get-concept-actions
  "Get actions data from 'concepts' list by 'actions-names' list"
  [concepts actions-names]
  (->> concepts
       (reduce (fn [result concept]
                 (concat result (-> (:data concept)
                                    (select-keys actions-names)
                                    (vals))))
               [])))

(defn get-concept-scene-actions
  "Get names of actions in 'concept-data' related to 'scene-id'"
  [concept-data scene-id]
  (->> (get-in concept-data [:scheme :fields])
       (filter (fn [{:keys [type scenes]}]
                 (and (= type "action")
                      (some #(= scene-id %) scenes))))
       (map :name)
       (map keyword)))

(defn- add-region-info
  [assets]
  (map (fn [asset-data]
         (merge asset-data
                {:start     nil
                 :duration  nil
                 :selected? false}))
       assets))

(defn- defined-and-equal
  [value-1 value-2]
  (and (not (nil? value-1))
       (not (nil? value-2))
       (= value-1 value-2)))

(defn- set-current-audio-selection
  [current-audio-data assets]
  (map (fn [{:keys [url id] :as asset-data}]
         (if (or (defined-and-equal url (:url current-audio-data))
                 (defined-and-equal id (:id current-audio-data))
                 (defined-and-equal url (:id current-audio-data)))
           (-> asset-data
               (assoc :selected? true)
               (assoc :start (:start current-audio-data))
               (assoc :duration (:duration current-audio-data)))
           asset-data))
       assets))

(defn get-audio-assets-data
  "Add region selection data to audio assets list - :start :duration :selected?

   audio-assets - list of assets data {:url [:id] :target :alias :type}
   current-audio-data - audio data of current action {[:url] [:id] :start :duration}

   Result list contains fields :url :target :start :duration :alias :type :selected?"
  [audio-assets current-audio-data]
  (->> audio-assets
       (add-region-info)
       (set-current-audio-selection current-audio-data)))

(defn get-current-action-audio-data
  "Get audio data {:url :target :start :duration} from action.

   action - action data from changes store {:id *, :data { *action data* }}."
  [action]
  (->> (:data action)
       (get-action-audio-data)
       (first)))