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
        volume (get-in background-music [:volume]),
        start-background-music {:type "audio", :id file-name :volume volume :loop true}
        music {:on "start", :action background-music-name}
        asset {:url file-name, :size 10, :type "audio"}
        triggers (:triggers scene-data)]
    (cond-> scene-data
            (contains? triggers :music)
            ((fn [data]
               (let [action-key (keyword (get-in triggers [:music :action]))
                     file (get-in data [:actions action-key :id])
                     start-background-music (if file-name
                                              start-background-music
                                              (assoc start-background-music :id file))
                     data (assoc-in data [:actions action-key] start-background-music)]
                 (if (file-used? data file) data (remove-asset data file))
                 )))
            (not (contains? triggers :music)) ((fn [data]
                                                 (-> data
                                                     (assoc-in [:actions (keyword background-music-name)] start-background-music)
                                                     (assoc-in [:triggers :music] music))))
            true (update :assets conj asset)
            true (update :assets vec))))

(defn- remove-background-music
  [scene-data]
  (let [triggers (:triggers scene-data)]
    (cond-> scene-data
            (contains? triggers :music)
            ((fn [data]
               (let [action-key (keyword (get-in triggers [:music :action]))
                     file (get-in data [:actions action-key :id])
                     data (update-in data [:actions] dissoc action-key)]
                 (if (file-used? data file) data (remove-asset data file)))))
            true (update-in [:triggers] dissoc :music))))

(defn- add-image
  [scene-data {:keys [name image]}]
  (let [image-idx (-> scene-data
                      (get-in [:metadata :uploaded-image-idx])
                      (or 0)
                      inc)
        object-name (str "uploaded-image-" image-idx)
        show-action-name (str "show-uploaded-image-" image-idx)
        hide-action-name (str "hide-uploaded-image-" image-idx)
        image-object {:type      "image"
                      :alias     name
                      :links     [{:type "action" :id show-action-name}
                                  {:type "action" :id hide-action-name}]
                      :src       (:src image)
                      :origin    {:type "center-center"}
                      :x         960
                      :y         540
                      :visible   false
                      :editable? {:select        true
                                  :drag          true
                                  :show-in-tree? true}}
        show-action {:type "set-attribute" :attr-name "visible", :attr-value true :target object-name}
        hide-action {:type "set-attribute" :attr-name "visible", :attr-value false :target object-name}
        available-actions [{:action show-action-name
                            :type   "image"
                            :links  [{:type "object" :id object-name}]
                            :name   (str "Show " name)}
                           {:action hide-action-name
                            :type   "image"
                            :links  [{:type "object" :id object-name}]
                            :name   (str "Hide " name)}]
        last-layer (-> scene-data
                       :scene-objects
                       last
                       (concat [object-name]))
        layers (-> scene-data
                   :scene-objects
                   drop-last
                   (concat [last-layer]))]
    (-> scene-data
        (update-in [:assets] concat [{:url (:src image) :type "image" :size 1}])
        (assoc-in [:objects (keyword object-name)] image-object)
        (assoc-in [:scene-objects] layers)
        (assoc-in [:actions (keyword show-action-name)] show-action)
        (assoc-in [:actions (keyword hide-action-name)] hide-action)
        (assoc-in [:metadata :uploaded-image-idx] image-idx)
        (update-in [:metadata :available-actions] concat available-actions))))

(defn update-activity
  [scene-data action data]
  (case (keyword action)
    :background-music (update-background-music scene-data data)
    :background-music-remove (remove-background-music scene-data)
    :add-image (add-image scene-data data)))
