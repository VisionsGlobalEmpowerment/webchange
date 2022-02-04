(ns webchange.interpreter.utils.counter)

(defn set-countdown
  [{:keys [interval count on-progress on-end start-immediately?]
    :or   {start-immediately? false
           on-progress        #()
           on-end             #()}}]
  (let [counter (atom 0)
        id (atom nil)
        tick (fn []
               (swap! counter inc)
               (on-progress @counter))
        last-tick? (fn [] (>= @counter count))
        finish (fn []
                 (on-end)
                 (js/clearInterval @id))]
    (when start-immediately? (tick))
    (if-not (last-tick?)
      (reset! id (js/setInterval (fn []
                                   (tick)
                                   (when (last-tick?) (finish)))
                                 interval))
      (finish))
    (fn []
      (js/clearInterval @id))))
