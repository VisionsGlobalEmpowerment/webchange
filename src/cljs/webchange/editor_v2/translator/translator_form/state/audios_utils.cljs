(ns webchange.editor-v2.translator.translator-form.state.audios-utils)

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
  (map (fn [{:keys [url] :as asset-data}]
         (if (defined-and-equal url (:url current-audio-data))
           (-> asset-data
               (assoc :selected? true)
               (assoc :start (:start current-audio-data))
               (assoc :duration (:duration current-audio-data)))
           asset-data))
       assets))

(defn- action-audio-url
  [{:keys [type] :as action-data}]
  (if (= type "audio")
    (or (:audio action-data) (:id action-data))
    (:audio action-data)))

(defn get-action-audio-data
  [action-data]
  (let [url (action-audio-url action-data)]
    (-> action-data
        (select-keys [:target :start :duration])
        (assoc :url url))))

(defn get-audio-assets-data
  "Add region selection data to audio assets list - :start :duration :selected?
   Result list contains fields :url :target :start :duration :alias :type :selected?"
  [current-phrase-action scene-audios]
  (let [current-audio-data (get-action-audio-data current-phrase-action)]
    (->> scene-audios
         (add-region-info)
         (set-current-audio-selection current-audio-data))))

(defn get-form-data
  [form-params]
  (reduce (fn [form-data [key value]]
            (.append form-data key value)
            form-data)
          (js/FormData.)
          form-params))
