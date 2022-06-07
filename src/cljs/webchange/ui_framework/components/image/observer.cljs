(ns webchange.ui-framework.components.image.observer)

(defonce handlers (atom {}))

(defn- init-observable
  [id]
  (let [{:keys [on-init initialised?]} (get @handlers id)]
    (when (not initialised?)
      (when (fn? on-init)
        (on-init))
      (swap! handlers update id merge {:initialised? true}))))

(defonce observer (js/IntersectionObserver.
                    (fn [entries]
                      (.forEach entries
                                (fn [entry]
                                  (let [intersection-ratio (.. entry -intersectionRatio)
                                        target-id (.. entry -target -id)
                                        visible? (> intersection-ratio 0)]
                                    (when visible?
                                      (init-observable target-id))))))))

(defn observe
  [element id init-handler]
  (swap! handlers assoc id {:element      element
                            :on-init      init-handler
                            :initialised? false})
  (.observe observer element))

(defn un-observe
  [id]
  (let [{:keys [element]} (get @handlers id)]
    (.unobserve observer element)
    (swap! handlers dissoc id)))
