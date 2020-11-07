(ns webchange.resources.generate-resources-list
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :as p]
    [config.core :refer [env]]
    [webchange.resources.core-web-app :refer [generate-app-resources
                                              get-resources-file-path]]))

(defn -main []
  (let [resources (generate-app-resources)
        resources-file-path (get-resources-file-path)]
    (->> resources-file-path
         (io/resource)
         (clojure.java.io/writer)
         (clojure.pprint/pprint resources))))
