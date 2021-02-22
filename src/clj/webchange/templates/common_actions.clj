(ns webchange.templates.common-actions
  (:require
    [clojure.data.json :as json]))

(defn- file-used?
  [scene-data file]
  (clojure.string/includes? (json/write-str (select-keys scene-data [:objects :actions])) (json/write-str file)))

(defn- remove-asset
  [scene-data file]
  (assoc scene-data :assets
                    (vec (remove (fn [asset] (and (= (:type asset) "audio") (= (:url asset) file)))
                                 (:assets scene-data)))))

(defn- update-background-music
  [scene-data {:keys [background-music]}]
  (let [background-music-name "start-background-music"
        file-name (get-in background-music [:src]),
        start-background-music {:type "audio", :id file-name :loop true}
        music {:on "start", :action background-music-name}
        asset {:url file-name, :size 10, :type "audio"}
        triggers (:triggers scene-data)]
    (cond-> scene-data
            (contains? triggers :music)
            ((fn [data]
               (let [action-key (keyword (get-in triggers [:music :action]))
                     file (get-in data [:actions action-key :id])
                     data (assoc-in data [:actions action-key] start-background-music)]
                 (if (file-used? data file) data (remove-asset data file))
                 )))
            (not (contains? triggers :music)) ((fn [data]
                                                 (-> data
                                                     (assoc-in [:actions (keyword background-music-name)] start-background-music)
                                                     (assoc-in [:triggers :music] music))))
            true (update :assets conj asset)
            true (update :assets vec))))

(defn update-activity
  [scene-data {:keys [data]}]
  (cond-> scene-data
          (contains? data :background-music) (update-background-music data)))