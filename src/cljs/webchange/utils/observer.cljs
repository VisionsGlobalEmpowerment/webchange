(ns webchange.utils.observer)

(defonce handlers (atom {}))

(defn- init-observable
  [id]
  (let [{:keys [on-init initialised?]} (get @handlers id)]
    (when (not initialised?)
      (when (fn? on-init)
        (on-init))
      (swap! handlers update id merge {:initialised? true}))))

(defn- destroy-observable
  [id]
  (let [{:keys [on-destroy initialised?]} (get @handlers id)]
    (when initialised?
      (when (fn? on-destroy)
        (on-destroy))
      (swap! handlers update id merge {:initialised? false}))))

(defonce observer (js/IntersectionObserver.
                    (fn [entries]
                      (.forEach entries
                                (fn [entry]
                                  (let [intersection-ratio (.. entry -intersectionRatio)
                                        target-id (.. entry -target -id)
                                        visible? (> intersection-ratio 0)]
                                    (if visible?
                                      (init-observable target-id)
                                      (destroy-observable target-id))))))))

(defn observe
  ([element id init-handler]
   (observe element id init-handler nil))
  ([element id init-handler destroy-handler]
   (swap! handlers assoc id {:element      element
                             :on-init      init-handler
                             :on-destroy   destroy-handler
                             :initialised? false})
   (.observe observer element)))

(defn un-observe
  [id]
  (if-let [observable (get @handlers id)]
    (do (.unobserve observer (:element observable))
        (swap! handlers dissoc id))))
