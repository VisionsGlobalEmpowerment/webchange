(ns webchange.resources.manager)

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
  (.detachAll (.-onProgress loader))
  (.detachAll (.-onError loader))
  (.detachAll (.-onLoad loader))
  (.detachAll (.-onComplete loader)))

(defn- set-callbacks
  [loader {:keys [on-progress on-error on-load on-complete]}]
  (when-not (nil? on-progress) (.add (.-onProgress loader)
                                     (fn [loader] (on-progress (-> loader (.-progress) (/ 100))))))
  (when-not (nil? on-error) (.add (.-onError loader) on-error))
  (when-not (nil? on-load) (.add (.-onLoad loader) on-load))
  (.add (.-onComplete loader) (fn []
                                (when-not (nil? on-complete)
                                  (on-complete))
                                (reset-callbacks loader))))

(defn load-resources
  ([resources]
   (load-resources resources {}))
  ([resources callbacks]
   (print "load-resources" resources)
   (set-callbacks @loader callbacks)
   (->> resources
        (reduce (fn [loader resource]
                  (let [[key src] (if (sequential? resource)
                                    [(first resource) (second resource)]
                                    [resource resource])]
                    (if-not (has-resource? key)
                      (.add loader key src)
                      loader)))
                @loader)
        (.load))))

(defn reset-loader!
  []
  (when @loader
    (.reset @loader)))
