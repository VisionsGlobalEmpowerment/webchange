(ns webchange.resources.manager
  (:require
    [webchange.interpreter.pixi :refer [clear-texture-cache]]
    [webchange.logger.index :as logger]))

;; PIXI.Loader: https://pixijs.download/dev/docs/PIXI.Loader.html
(defonce loaders (atom {}))

(def default-loader-id "default")

(defn get-loader
  ([] (get-loader default-loader-id))
  ([id] (get @loaders (or id default-loader-id))))

(defn set-loader
  ([instance] (set-loader default-loader-id instance))
  ([id instance] (swap! loaders assoc (or id default-loader-id) instance)))

(defn init
  [instance id]
  (when (nil? (get-loader id))
    (set-loader id instance)))

(defn- get-resources-store
  []
  (.-resources (get-loader)))

(defn get-resource
  [resource-name]
  (-> (get-resources-store)
      (aget resource-name)))

(defn- has-resource?
  [resource-name]
  (-> resource-name get-resource nil? not))

(defn- reset-callbacks
  [loader]
  (.detachAll (.-onProgress loader))
  (.detachAll (.-onError loader))
  (.detachAll (.-onLoad loader))
  (.detachAll (.-onComplete loader)))

(defn- set-callbacks
  [loader {:keys [on-progress on-error on-load on-complete]} id]
  (when-not (nil? on-progress) (.add (.-onProgress loader)
                                     (fn [loader] (on-progress (-> loader (.-progress) (/ 100))))))
  (when-not (nil? on-error) (.add (.-onError loader) on-error))
  (when-not (nil? on-load) (.add (.-onLoad loader) on-load))
  (.add (.-onComplete loader) (fn []
                                (logger/trace "load complete" id)
                                (when (fn? on-complete)
                                  (on-complete))
                                (reset-callbacks loader))))

(defn load-resources
  ([resources]
   (load-resources resources {}))
  ([resources callbacks]
   (load-resources resources callbacks nil))
  ([resources callbacks id]
   (logger/trace "load resources" resources id)
   (let [loader (get-loader id)]
     (set-callbacks loader callbacks id)
     (->> resources
          (reduce (fn [loader resource]
                    (let [[key src] (if (sequential? resource)
                                      [(first resource) (second resource)]
                                      [resource resource])]
                      (if-not (has-resource? key)
                        (.add loader key src)
                        loader)))
                  loader)
          (.load)))))

(defn load-resource
  [src callback]
  (load-resources
    [src]
    {:on-complete (fn []
                    (let [resource (get-resource src)]
                      (callback resource)))}))

(defn reset-loader!
  [id]
  (when-not (nil? (get-loader id))
    (clear-texture-cache)
    (.reset (get-loader id))))
