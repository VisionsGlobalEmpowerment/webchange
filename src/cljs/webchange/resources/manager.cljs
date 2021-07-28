(ns webchange.resources.manager
  (:require
    [webchange.logger.index :as logger]))

;; PIXI.Loader: https://pixijs.download/dev/docs/PIXI.Loader.html
(defonce loader (atom nil))

(defn init
  [loader-instance]
  (when (nil? @loader)
    (reset! loader loader-instance)))

(defn- get-resources-store
  []
  (.-resources @loader))

(defn get-resource
  [resource-name]
  (-> (get-resources-store)
      (aget resource-name)))

(defn- has-resource?
  [resource-name]
  (-> resource-name get-resource nil? not))

(defn- reset-callbacks
  [loader]
  (.detachAll (.-onComplete loader))
  (.detachAll (.-onError loader))
  (.detachAll (.-onLoad loader))
  (.detachAll (.-onProgress loader))
  (.detachAll (.-onStart loader)))

(defn- set-callbacks
  [loader {:keys [on-progress on-start on-error on-load on-complete]}]
  (when (fn? on-error) (.add loader.onError on-error))
  (when (fn? on-load) (.add loader.onLoad on-load))
  (when (fn? on-start) (.add loader.onStart on-start))
  (when (fn? on-progress) (.add loader.onProgress (fn [loader] (on-progress (-> loader (.-progress) (/ 100))))))

  (.add (.-onComplete loader) (fn []
                                (when (fn? on-complete) (on-complete))
                                (reset-callbacks loader))))

(defonce loading-que (atom {}))
(defn- add-to-que [key] (swap! loading-que assoc key true))
(defn- remove-from-que [key] (swap! loading-que dissoc key))
(defn- in-que? [key] (contains? @loading-que key))

(defn load-resources
  ([resources]
   (load-resources resources {}))
  ([resources callbacks]
   (logger/trace "load resources" resources)
   (set-callbacks @loader callbacks)
   (->> resources
        (reduce (fn [loader resource]
                  (let [[key src] (if (sequential? resource)
                                    [(first resource) (second resource)]
                                    [resource resource])]
                    (if-not (or (has-resource? key)
                                (in-que? key))
                      (do (add-to-que key)
                          (.add loader key src))
                      loader)))
                @loader)
        (.load))))

(defn load-resource
  [src callback]
  (load-resources
    [src]
    {:on-complete (fn []
                    (let [resource (get-resource src)]
                      (callback resource)))}))

(defn reset-loader!
  []
  (when (not (nil? @loader))
    (.reset @loader)))
