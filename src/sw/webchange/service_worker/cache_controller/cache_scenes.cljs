(ns webchange.service-worker.cache-controller.cache-scenes
  (:require
    [webchange.service-worker.common.cache :refer [cache-game-resources
                                                   cache-endpoints
                                                   remove-game-resources]]))

(defn- get-field
  [data field-name]
  (if (contains? data field-name)
    (get data field-name)
    []))

(defn- get-params
  [data]
  (reduce (fn [result field]
            (assoc result field (get-field data field)))
          {}
          [:resources-to-add
           :resources-to-remove
           :endpoints-to-add
           :endpoints-to-remove]))

(defn cache-scenes
  [data course-name]
  (let [{:keys [resources-to-add
                resources-to-remove
                endpoints-to-add]} (get-params data)]
    (cache-game-resources resources-to-add course-name)
    (remove-game-resources resources-to-remove course-name)
    (cache-endpoints endpoints-to-add course-name)))