(ns webchange.resources.que)

(defonce que (atom {}))
(defonce callbacks (atom {}))
(defonce progress (atom {:total 0}))

(defn- add-callback
  [key callback]
  (when (fn? callback)
    (swap! callbacks assoc key callback)))

(defn- remove-callback
  [key]
  (swap! callbacks dissoc key))

(defn- reset-callbacks
  ([]
   (reset-callbacks {}))
  ([handlers]
   (reset! callbacks handlers)))

(defn- call-callback
  [key & args]
  (let [callback (get @callbacks key)]
    (when (fn? callback)
      (apply callback args))))

(defn- reset-progress
  []
  (reset! progress {:total 0}))

(defn- set-total
  [value]
  (swap! progress assoc :total value))

(defn- get-progress
  []
  (let [{:keys [total]} @progress
        left (-> @que keys count)]
    (if-not (= total 0)
      (/ (- total left) total)
      0)))

(defn- add
  [key]
  (swap! que assoc key true))

(defn add-all
  [keys]
  (reset-progress)
  (set-total (count keys))
  (doseq [key keys]
    (add key)))

(defn- all-completed?
  []
  (empty? @que))

(defn complete
  [key]
  (swap! que dissoc key)
  (call-callback :on-progress (get-progress))
  (when (all-completed?)
    (call-callback :on-complete)
    (remove-callback :on-complete)))

(defn- complete-all
  []
  (reset! que {}))

(defn reset
  ([]
   (reset {}))
  ([callbacks]
   (complete-all)
   (reset-progress)
   (reset-callbacks callbacks)))
