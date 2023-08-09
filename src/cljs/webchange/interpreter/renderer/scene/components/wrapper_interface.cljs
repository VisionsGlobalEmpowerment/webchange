(ns webchange.interpreter.renderer.scene.components.wrapper-interface
  (:require
    [webchange.logger.index :as logger]))

(defn- execute
  [wrapper method-name params]
  (if-not (nil? wrapper)
    (try
      (let [method (get wrapper method-name)]
        (if-not (nil? method)
          (apply method params)
          (logger/warn (str "Method " method-name " is not implemented for " (:type wrapper) ". (params: " params ")"))))
      (catch js/Error e
        (logger/error (str "[Wrapper Interface] Failed to execute <" method-name ">"))
        (logger/error e)))
    (logger/error (str "[Wrapper Interface] Failed to execute <" method-name "> method: wrapper is nil"))))

(defn execute-method [target method & params]
  (let [targets (if (sequential? target) target [target])]
    (doseq [wrapper targets]
      (execute wrapper method params))))

(defn add-animation [wrapper & params] (execute wrapper :add-animation params))
(defn get-data [wrapper & params] (execute wrapper :get-data params))
(defn get-filter-value [wrapper & params] (execute wrapper :get-filter-value params))
(defn get-opacity [wrapper & params] (execute wrapper :get-opacity params))
(defn get-position [wrapper & params] (execute wrapper :get-position params))
(defn get-init-position [wrapper & params] (execute wrapper :get-init-position params))
(defn get-rotation [wrapper & params] (execute wrapper :get-rotation params))
(defn remove-animation [wrapper & params] (execute wrapper :remove-animation params))
(defn set-data [wrapper & params] (execute wrapper :set-data params))
(defn set-filter-value [wrapper & params] (execute wrapper :set-filter-value params))
(defn set-opacity [wrapper & params] (execute wrapper :set-opacity params))


(defn set-position [wrapper & params] (execute wrapper :set-position params))
(defn set-rotation [wrapper & params] (execute wrapper :set-rotation params))
(defn set-scale [wrapper & params] (execute wrapper :set-scale params))
(defn get-scale [wrapper & params] (execute wrapper :get-scale params))


(defn set-skin [wrapper & params] (execute wrapper :set-skin params))


(defn set-slot [wrapper & params] (execute wrapper :set-slot params))
(defn set-src [wrapper & params] (execute wrapper :set-src params))
(defn reset-video [wrapper & params] (execute wrapper :reset-video params))




(defn set-parent [wrapper & params] (execute wrapper :set-parent params))

(defn clear-area [wrapper & params] (execute wrapper :clear params))



(defn set-visibility [wrapper & params] (execute wrapper :set-visibility params))
(defn start-animation [wrapper & params] (execute wrapper :start-animation params))
(defn set-filter [wrapper & params] (execute wrapper :set-filter params))
(defn stop [wrapper & params] (execute wrapper :stop params))
(defn set-tool [wrapper & params] (execute wrapper :set-tool params))
(defn set-interactive [wrapper & params] (execute wrapper :set-interactive params))
(defn set-fill [wrapper & params] (execute wrapper :set-fill params))
(defn get-fill [wrapper & params] (execute wrapper :get-fill params))
(defn set-traffic-light [wrapper & params] (execute wrapper :set-traffic-light params))

(defn get-wrapped-props [wrapper & params] (execute wrapper :get-wrapped-props params))
(defn get-prop [wrapper & params] (execute wrapper :get-prop params))
(defn set-prop [wrapper & params] (execute wrapper :set-prop params))
