(ns webchange.templates.library.categorize-antonims.common)

(defn get-draggable-item
  ([params]
   (get-draggable-item params {}))
  ([{:keys [position src target say-item say-correct box]}
    {:keys [drag-start? drag-move?]
     :or   {drag-start? true
            drag-move?  true}}]
   (merge position
          {:type        "image"
           :src         src,
           :draggable   true
           :collidable? true
           :actions     (cond-> {:drag-end      {:on     "drag-end"
                                                 :type   "action"
                                                 :id     "handle-drag-end"
                                                 :params (cond-> {:box           box
                                                                  :init-position (merge position {:duration 1})
                                                                  :target        target}
                                                                 (some? say-correct) (assoc :correct-drop say-correct))}
                                 :collide-enter {:on               "collide-enter"
                                                 :test             ["#^.*-box"]
                                                 :type             "action"
                                                 :id               "handle-collide-enter"
                                                 :pick-event-param ["target"]}
                                 :collide-leave {:on               "collide-leave"
                                                 :test             ["#^.*-box"]
                                                 :type             "action"
                                                 :id               "handle-collide-leave"
                                                 :pick-event-param ["target"]}}
                                drag-start? (assoc :drag-start {:on     "drag-start"
                                                                :type   "action"
                                                                :id     "handle-drag-start"
                                                                :params {:target target}})
                                drag-move? (assoc :drag-move {:on      "drag-move"
                                                              :type    "action"
                                                              :id      "handle-drag-move"
                                                              :params  {:say-item say-item}
                                                              :options {:throttle "action-done"}}))})))



