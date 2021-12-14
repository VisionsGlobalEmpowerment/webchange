(ns webchange.templates.library.categorize.templates.common)

(defn- add-single-background
  [template {:keys [background]}]
  (let [{:keys [src]} background
        asset {:url src :size 10 :type "image"}
        object-name "background"
        object-data {:type "background" :src src}]
    (-> template
        (update :assets conj asset)
        (update :objects assoc (keyword object-name) object-data)
        (update :scene-objects conj [object-name]))))

(defn- add-layered-background
  [template {:keys [background]}]
  (let [{:keys [background decoration surface]} background
        assets (->> [background decoration surface]
                    (map :src)
                    (map (fn [src] {:url src :size 10 :type "image"})))
        object-name "background"
        object-data {:type       "layered-background"
                     :background background
                     :decoration decoration
                     :surface    surface}]
    (-> template
        (update :assets concat assets)
        (update :objects assoc (keyword object-name) object-data)
        (update :scene-objects conj [object-name]))))

(defn add-background
  [template {:keys [background] :as params}]
  (if (contains? background :src)
    (add-single-background template params)
    (add-layered-background template params)))

(defn get-draggable-item
  ([params]
   (get-draggable-item params {}))
  ([{:keys [position src target say-item say-correct box test states]
     :or   {test ["#^.*-box"]}}
    {:keys [drag-start? drag-move?]
     :or   {drag-start? true
            drag-move?  true}}]
   (merge position
          (cond-> {:type        "image"
                   :src         src,
                   :draggable   true
                   :collidable? true
                   :actions     (cond-> {:drag-end      {:on     "drag-end"
                                                         :type   "action"
                                                         :id     "handle-drag-end"
                                                         :params (cond-> {:init-position (-> position
                                                                                             (select-keys [:x :y])
                                                                                             (merge {:duration 1}))}
                                                                         (some? box) (assoc :box box)
                                                                         (some? target) (assoc :target target)
                                                                         (some? say-correct) (assoc :correct-drop say-correct))}
                                         :collide-enter {:on               "collide-enter"
                                                         :test             test
                                                         :type             "action"
                                                         :id               "handle-collide-enter"
                                                         :pick-event-param ["target"]}
                                         :collide-leave {:on               "collide-leave"
                                                         :test             test
                                                         :type             "action"
                                                         :id               "handle-collide-leave"
                                                         :pick-event-param ["target"]}}
                                        drag-start? (assoc :drag-start {:on     "drag-start"
                                                                        :type   "action"
                                                                        :id     "handle-drag-start"
                                                                        :params (cond-> {}
                                                                                        (some? target) (assoc :target target))})
                                        drag-move? (assoc :drag-move {:on      "drag-move"
                                                                      :type    "action"
                                                                      :id      "handle-drag-move"
                                                                      :params  {:say-item say-item}
                                                                      :options {:throttle "action-done"}}))}
                  (some? states) (assoc :states states)))))