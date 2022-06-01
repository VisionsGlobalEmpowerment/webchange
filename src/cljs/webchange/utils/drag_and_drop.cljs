(ns webchange.utils.drag-and-drop
  (:require
    [reagent.core :as r]))

(defn- event->target
  [event]
  (-> (.-target event)
      (.closest "[draggable]")))

(defn- get-data-set
  [event]
  (->> (event->target event)
       (.-dataset)
       (js/Object.entries)
       (js->clj)
       (map (fn [[field value]]
              [(keyword field) value]))
       (into {})))

(defn get-transfer-data
  [event]
  (let [data-transfer (.-dataTransfer event)
        items (.-items data-transfer)]
    (->> (range (.-length items))
         (reduce (fn [result index]
                   (let [item (aget items index)
                         key (.-type item)
                         value (.getData data-transfer key)]
                     (assoc result (keyword key) value)))
                 {}))))

(defn set-transfer-data
  [event data]
  (let [data-transfer (.-dataTransfer event)]
    (doseq [key (keys data)]
      (.setData data-transfer (clojure.core/name key) (get data key)))))

;;

(defn- event->wrapped-child
  [event]
  (.. event -target -firstChild))

(defn- add-class
  [el class-name]
  (-> (.-classList el)
      (.add class-name)))

(defn- remove-class
  [el class-name]
  (-> (.-classList el)
      (.remove class-name)))

(defonce dragged-item (atom {}))
(defonce hover-counter (atom 0))

(defn- handle-drag-start
  [event]
  (reset! dragged-item (get-data-set event))
  (set! (.. event -dataTransfer -effectAllowed) "move")
  (set! (.. event -dataTransfer -dropEffect) "move")
  (-> (event->target event)
      (add-class "dragged")))

(defn- handle-drag-over
  [event]
  (.preventDefault event)
  (.stopPropagation event))

(defn- handle-drag-enter
  [drop-allowed? event]
  (swap! hover-counter inc)
  (let [target-data (get-data-set event)]
    (when (drop-allowed? @dragged-item target-data)
      (-> (event->target event)
          (add-class "drag-over")))))

(defn- handle-drag-leave
  [event]
  (swap! hover-counter dec)
  (when (= @hover-counter 0)
    (-> (event->target event)
        (remove-class "drag-over"))))

(defn- handle-drag-end
  [event]
  (reset! hover-counter 0)
  (let [target (.-target event)
        all-items (.querySelectorAll js/document "[draggable]")]
    (.remove (.-classList target) "dragged")
    (.forEach all-items (fn [item]
                          (.remove (.-classList item) "drag-over")))))

(defn- handle-drop
  [drop-allowed? on-drop event]
  (let [target-data (get-data-set event)]
    (when (and (fn? on-drop)
               (drop-allowed? @dragged-item target-data))
      (on-drop {:dragged @dragged-item
                :target  (get-data-set event)}))))

(defn- init-dnd
  [el handlers]
  (doseq [[event handler] handlers]
    (.addEventListener el event handler)))

(defn- reset-dnd
  [el handlers]
  (doseq [[event handler] handlers]
    (.removeEventListener el event handler)))

(defn- get-data-attrs
  [data]
  (->> data
       (map (fn [[field-name field-value]]
              [(->> field-name name (str "data-") keyword) field-value]))
       (into {})))

(defn draggable
  [{:keys [data drop-allowed? on-drop]
    :or   {drop-allowed? (constantly true)}}]
  (r/with-let [el (atom nil)
               handlers {"dragstart" handle-drag-start
                         "dragover"  handle-drag-over
                         "dragenter" (partial handle-drag-enter drop-allowed?)
                         "dragleave" handle-drag-leave
                         "dragend"   handle-drag-end
                         "drop"      (partial handle-drop drop-allowed? on-drop)}
               handle-ref #(when (some? %)
                             (reset! el %)
                             (init-dnd @el handlers))]
    (->> (r/current-component)
         (r/children)
         (into [:div (merge {:draggable  true
                             :ref        handle-ref
                             :class-name "wc-draggable"}
                            (get-data-attrs data))]))
    (finally
      (reset-dnd @el handlers))))
