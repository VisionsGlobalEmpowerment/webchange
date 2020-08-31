(ns webchange.interpreter.renderer.scene.components.utils
  (:require
    [clojure.data :refer [diff]]
    [webchange.logger :as logger]))

(defn remove-nil-fields [object]
  (apply merge (for [[k v] object :when (not (nil? v))] {k v})))

(defn set-not-nil-value
  [object name value]
  (when (-> value nil? not)
    (aset object name value)))

(defn get-position
  [spine-object]
  (let [position (.-position spine-object)]
    {:x (.-x position)
     :y (.-y position)}))

(defn set-position
  [spine-object position]
  (let [{:keys [x y]} (merge (get-position spine-object)
                             (remove-nil-fields position))]
    (-> (.-position spine-object)
        (.set x y))))

(defn get-scale
  [spine-object]
  (let [scale (.-scale spine-object)]
    {:x (.-x scale)
     :y (.-y scale)}))

(defn set-scale
  [spine-object scale]
  (let [{:keys [x y]} (merge (get-scale spine-object)
                             (remove-nil-fields (if (number? scale)
                                                  {:x scale
                                                   :y scale}
                                                  scale)))]
    (-> (.-scale spine-object)
        (.set x y))))

(defn set-visibility
  [spine-object visible?]
  (aset spine-object "alpha" (if visible? 1 0)))

(defn set-handler
  [object event-name event-handler]
  (let [synonyms {"click" ["click" "tap"]}
        button-events ["click"]]
    (aset object "interactive" true)
    (when (some #{event-name} button-events)
      (aset object "buttonMode" true))                      ;; ToDo: Move "buttonMode" setting to components
    (if (contains? synonyms event-name)
      (doseq [event (get synonyms event-name)]
        (.on object event event-handler))
      (.on object event-name event-handler))))

(defn get-size
  [object]
  {:width  (.-width object)
   :height (.-height object)})

(defn pick-params
  [default-params params-names]
  (->> params-names
       (select-keys default-params)
       (map second)))

(defn get-specific-params
  [props params]
  (reduce (fn [result param]
            (let [[param-name param-alias default-value] (if (map? param)
                                                           [(:name param)
                                                            (if (contains? param :alias) (:alias param) (:name param))
                                                            (if (contains? param :default) (:default param) nil)]
                                                           [param param nil])]
              (assoc result param-name (get props param-alias default-value))))
          {}
          params))

(defn check-rest-props
  [entity-id props & known-params-list]
  (let [rest-props (->> known-params-list
                        (reduce (fn [rest-params ignore-params]
                                  (let [ignore-aliases (map (fn [param]
                                                              (if (map? param)
                                                                (if (contains? param :alias) (:alias param) (:name param))
                                                                param))
                                                            ignore-params)]
                                    (clojure.set/difference rest-params (set ignore-aliases))))
                                (set (keys props)))
                        (select-keys props))]
    (when-not (empty? rest-props)
      (logger/warn "There are extra props for" entity-id)
      (logger/debug-folded (str entity-id " extra props") rest-props))))
