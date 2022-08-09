(ns webchange.resources.manager
  (:require
    [webchange.logger.index :as logger]
    [webchange.resources.scene-parser :as parser]
    [webchange.resources.que :as que]))

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

(defn has-resource?
  [resource-name]
  (-> resource-name get-resource nil? not))

(defn load-resources
  ([resources]
   (load-resources resources {}))
  ([resources callbacks]
   (logger/trace "load resources" resources)
   (que/reset callbacks)
   (let [resources-to-load (->> (or resources [])
                                (map (fn [resource]
                                       (if (sequential? resource)
                                         [(first resource) (second resource)]
                                         [resource resource])))
                                (filter (fn [[name _]]
                                          (-> (has-resource? name)
                                              (not))))
                                (map (fn [[name url]]
                                       {:name       name
                                        :url        url
                                        :onComplete #(-> (.-name %) (que/complete))})))]
     (if-not (empty? resources-to-load)
       (do (->> resources-to-load
                (map :name)
                (que/add-all))

           (doto @loader
             (.add (clj->js resources-to-load))
             (.load)))
       (que/call-callback :on-complete)))))

(defn load-resource
  [src callback]
  (load-resources
    [src]
    {:on-complete (fn []
                    (let [resource (get-resource src)]
                      (callback resource)))}))

(defn- hack-parsing-resources-for-spine
  "When loader is reset in the middle of loading spine resources
  it will leave resource in the queue forever"
  []
  (-> @loader .-_resourcesParsing (.splice 0)))

(defn reset-loader!
  []
  (when (some? @loader)
    (que/reset)
    (.reset @loader)
    (hack-parsing-resources-for-spine)))

(defn get-or-load-resource
  [resource-name {:keys [animation? on-complete] :as params}]
  (if (has-resource? resource-name)
    (-> resource-name get-resource on-complete)
    (let [resources-to-load (cond
                              animation? (parser/get-animation-resources resource-name)
                              (string? resource-name) [resource-name]
                              :else resource-name)]
      (load-resources resources-to-load (merge params
                                               {:on-complete (fn []
                                                               (when (fn? on-complete)
                                                                 (-> resource-name get-resource on-complete)))})))))
