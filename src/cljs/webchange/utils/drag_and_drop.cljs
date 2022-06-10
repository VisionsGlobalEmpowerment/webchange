(ns webchange.utils.drag-and-drop
  (:require
    [reagent.core :as r]
    [webchange.utils.observer :as observer]
    [webchange.utils.uid :refer [get-uid]]))

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

(defn- get-hover-side
  [event]
  (let [offset-y (.-offsetY event)
        target-height (.-clientHeight (event->target event))]
    {:vertical (if (->> (/ target-height 2) (< offset-y)) :top :bottom)}))

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
  (.stopPropagation event)
  (let [target (event->target event)
        {:keys [vertical]} (get-hover-side event)]
    (case vertical
      :top (do (remove-class target "drag-over-bottom")
               (add-class target "drag-over-top"))
      :bottom (do (remove-class target "drag-over-top")
                  (add-class target "drag-over-bottom")))))

(defn- clear-drag-over-classes
  []
  (-> (.querySelectorAll js/document "[draggable]")
      (.forEach (fn [item]
                  (remove-class item "drag-over-top")
                  (remove-class item "drag-over-bottom")
                  (remove-class item "drag-over")))))

(defn- handle-drag-enter
  [drop-allowed? event]
  (swap! hover-counter inc)
  (let [target-data (get-data-set event)]
    (clear-drag-over-classes)
    (when (drop-allowed? @dragged-item target-data)
      (-> (event->target event)
          (add-class "drag-over")))))

(defn- handle-drag-leave
  [event]
  (swap! hover-counter dec)
  (when (= @hover-counter 0)
    (let [target (event->target event)]
      (remove-class target "drag-over-top")
      (remove-class target "drag-over-bottom")
      (remove-class target "drag-over"))))

(defn- handle-drag-end
  [event]
  (reset! hover-counter 0)
  (let [target (.-target event)
        all-items (.querySelectorAll js/document "[draggable]")]
    (remove-class target "dragged")
    (.forEach all-items (fn [item]
                          (remove-class item "drag-over-top")
                          (remove-class item "drag-over-bottom")
                          (remove-class item "drag-over")))))

(defn- handle-drop
  [drop-allowed? on-drop event]
  (let [target-data (get-data-set event)]
    (when (and (fn? on-drop)
               (drop-allowed? @dragged-item target-data))
      (on-drop {:dragged @dragged-item
                :target  (get-data-set event)
                :side    (get-hover-side event)}))))

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
  (r/with-let [id (get-uid)
               el (atom nil)
               handlers {"dragstart" handle-drag-start
                         "dragover"  handle-drag-over
                         "dragenter" (partial handle-drag-enter drop-allowed?)
                         "dragleave" handle-drag-leave
                         "dragend"   handle-drag-end
                         "drop"      (partial handle-drop drop-allowed? on-drop)}
               init #(init-dnd @el handlers)
               reset #(reset-dnd @el handlers)
               handle-ref #(when (some? %)
                             (reset! el %)
                             (observer/observe @el id init reset))]
    (->> (r/current-component)
         (r/children)
         (into [:div (merge {:id         id
                             :draggable  true
                             :ref        handle-ref
                             :class-name "wc-draggable"}
                            (get-data-attrs data))]))
    (finally
      (reset-dnd @el handlers)
      (observer/un-observe id))))
