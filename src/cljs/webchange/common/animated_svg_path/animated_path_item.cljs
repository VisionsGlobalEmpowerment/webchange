(ns webchange.common.animated-svg-path.animated-path-item
  (:require
    [react-konva :refer [Path]]
    [webchange.common.animated-svg-path.utils :refer [path-length]]))

(defn animated-path-item
  [{:keys [data progress] :as props}]
  (let [length (atom 0)
        instance (atom nil)
        set-progress (fn [progress]
                       (when @instance
                         (.dash @instance [(* progress @length) @length])
                         (.visible @instance (> progress 0))))

        cb (:ref props)
        props (-> props
                  (dissoc :progress)
                  (assoc :visible (> progress 0))
                  (assoc :ref (fn [ref]
                                (reset! instance ref)
                                (reset! length (path-length data))
                                (set-progress progress)
                                (cb {:ref ref
                                     :set-progress set-progress}))))]
    [:> Path props]))
