(ns webchange.editor-v2.translator.translator-form.audio-assets.utils)

(defn get-action-audio-data
  [{:keys [type data] :as action-data}]
  (cond
    (= type "audio") [(merge {:url (:id action-data)}
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

(defn- set-current-audio-selection
  [current-audio-data assets]
  (map (fn [{:keys [url] :as asset-data}]
         (if (= url (:url current-audio-data))
           (-> asset-data
               (assoc :selected? true)
               (assoc :start (:start current-audio-data))
               (assoc :duration (:duration current-audio-data)))
           asset-data))
       assets))

(defn get-audio-assets-data
  "Add region selection data to audio assets list - :start :duration :selected?

   audio-assets - list of assets data {:url :target :alias :type}
   current-audio-data - audio data of current action {:url :start :duration}

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