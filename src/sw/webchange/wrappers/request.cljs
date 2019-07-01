(ns webchange.wrappers.request)

(defn pathname
  [request]
  (->> request
       (.-url)
       (js/URL. )
       (.-pathname)))

