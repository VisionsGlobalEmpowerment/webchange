(ns webchange.editor-v2.translator.audios-block.utils.scene-audios)

(defn get-audio-assets
  [scene-data]
  (->> (:assets scene-data)
       (filter (fn [{:keys [type]}] (= type "audio")))
       (map (fn [{:keys [url alias character]}]
           {:url       url
            :key       url
            :alias     alias
            :character character}))))

(defn get-audio-keys
  [scene-data]
  (->> (:audio scene-data)
       (reduce
         (fn [result [key url]]
           (assoc result url key))
         {})))

(defn apply-audio-keys
  [assets audio-keys]
  (map
    (fn [{:keys [url] :as asset}]
         (if (contains? audio-keys url)
           (assoc asset :key (get audio-keys url))
           asset))
    assets))

(defn get-scene-audios
  [scene-data]
  (let [audio-assets (get-audio-assets scene-data)
        audio-keys (get-audio-keys scene-data)]
    (apply-audio-keys audio-assets audio-keys)))
