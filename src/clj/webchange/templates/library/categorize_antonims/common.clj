(ns webchange.templates.library.categorize-antonims.common)

(defn get-draggable-item
  [{:keys [position src target say-item say-correct box]}]
  (merge position
         {:type        "image"
          :src         src,
          :draggable   true
          :collidable? true
          :actions     {:drag-start    {:on     "drag-start"
                                        :type   "action"
                                        :id     "handle-drag-start"
                                        :params {:target target}}
                        :drag-move     {:on      "drag-move"
                                        :type    "action"
                                        :id      "handle-drag-move"
                                        :params  {:say-item say-item}
                                        :options {:throttle "action-done"}}
                        :drag-end      {:on     "drag-end"
                                        :type   "action"
                                        :id     "handle-drag-end"
                                        :params {:box           box
                                                 :correct-drop  say-correct
                                                 :init-position (merge position
                                                                       {:duration 1})
                                                 :target        target}}
                        :collide-enter {:on               "collide-enter"
                                        :test             ["#^.*-box"]
                                        :type             "action"
                                        :id               "handle-collide-enter"
                                        :pick-event-param ["target"]}
                        :collide-leave {:on               "collide-leave"
                                        :test             ["#^.*-box"]
                                        :type             "action"
                                        :id               "handle-collide-leave"
                                        :pick-event-param ["target"]}}}))


