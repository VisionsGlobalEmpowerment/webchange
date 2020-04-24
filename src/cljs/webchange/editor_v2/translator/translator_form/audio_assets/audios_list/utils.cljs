(ns webchange.editor-v2.translator.translator-form.audio-assets.audios-list.utils)

(defn get-action-audio-data
  "Return a map of audio data from action.
   Can be used to render waveform
   keys:
  :key - audio url
  :start, :duration"
  [action-data audios]
  (when (some #{(:type action-data)} ["audio"
                                      "animation-sequence"])
    (let [action-key (or (get action-data :audio)
                         (get action-data :id))
          action-url (some (fn [{:keys [key url]}]
                             (and (= key action-key) url)) audios)]
      (merge (select-keys action-data [:start :duration])
             {:key (or action-url
                       action-key)}))))

(defn- audio-key->audio-data
  [audios]
  (map
    (fn [{:keys [url alias target]}]
      {:key       url
       :alias     alias
       :target    target
       :start     nil
       :duration  nil
       :selected? false})
    audios))

(defn- update-audios-with-action
  [audios-data current-key action-audio-data]
  (map
    (fn [audio-data]
      (if (= (:key audio-data) current-key)
        (merge audio-data
               {:start     (:start action-audio-data)
                :duration  (:duration action-audio-data)
                :selected? true})
        (merge audio-data
               {:start     nil
                :duration  nil
                :selected? false})))
    audios-data))

(defn get-prepared-audios-data
  [audios-list current-key action-audio-data]
  (-> audios-list
      (audio-key->audio-data)
      (update-audios-with-action current-key action-audio-data)))
