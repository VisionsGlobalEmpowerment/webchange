(ns webchange.utils.drag-and-drop
  (:require
    [reagent.core :as r]
    [webchange.utils.element :as utils]
    [webchange.utils.observer :as observer]
    [webchange.utils.uid :refer [get-uid]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

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
      (utils/add-class "dragged")))

(defn- handle-drag-over
  [event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [target (event->target event)
        {:keys [vertical]} (get-hover-side event)]
    (case vertical
      :top (do (utils/remove-class target "drag-over-bottom")
               (utils/add-class target "drag-over-top"))
      :bottom (do (utils/remove-class target "drag-over-top")
                  (utils/add-class target "drag-over-bottom")))))

(defn- clear-drag-over-classes
  ([]
   (clear-drag-over-classes {}))
  ([{:keys [except only]}]
   (let [remove-drag-over-classes #(do (utils/remove-class % "drag-over-top")
                                       (utils/remove-class % "drag-over-bottom")
                                       (utils/remove-class % "drag-over"))]
     (if (some? only)
       (remove-drag-over-classes only)
       (-> (.querySelectorAll js/document "[draggable]")
           (.forEach (fn [item]
                       (when (or (nil? except)
                                 (not= except item))
                         (remove-drag-over-classes item)))))))))

(defn- handle-drag-enter
  [drop-allowed? event]
  (swap! hover-counter inc)
  (let [target (event->target event)
        target-data (get-data-set event)]
    (clear-drag-over-classes {:except target})
    (if (drop-allowed? @dragged-item target-data)
      (-> (event->target event)
          (utils/add-class "drag-over"))
      (clear-drag-over-classes {:only target}))))

(defn- handle-drag-leave
  [event]
  (swap! hover-counter dec)
  (when (= @hover-counter 0)
    (let [target (event->target event)]
      (utils/remove-class target "drag-over-top")
      (utils/remove-class target "drag-over-bottom")
      (utils/remove-class target "drag-over"))))

(defn- handle-drag-end
  [event]
  (reset! hover-counter 0)
  (let [target (.-target event)
        all-items (.querySelectorAll js/document "[draggable]")]
    (utils/remove-class target "dragged")
    (.forEach all-items (fn [item]
                          (utils/remove-class item "drag-over-top")
                          (utils/remove-class item "drag-over-bottom")
                          (utils/remove-class item "drag-over")))))

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
  [{:keys [class-name data drop-allowed? on-drop]
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
                             :class-name (get-class-name {"wc-draggable" true
                                                          class-name     (some? class-name)})}
                            (get-data-attrs data))]))
    (finally
      (reset-dnd @el handlers)
      (observer/un-observe id))))
