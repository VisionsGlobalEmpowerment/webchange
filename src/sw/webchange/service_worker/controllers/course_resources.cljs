(ns webchange.service-worker.controllers.course-resources
  (:require
    [webchange.service-worker.cache.core :as cache]
    [webchange.service-worker.config :as config]
    [webchange.service-worker.controllers.game-resources :as game]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.controllers.web-app-resources :as web-app]
    [webchange.service-worker.wrappers :refer [then]]))

(defn fetch-current-course-data
  []
  (logger/debug "[Course controller] Fetch current course data..")
  (-> (web-app/cache-app)
      (then game/restore-cached-lessons)))

(defn remove-outdated-data
  []
  (cache/remove-old-caches config/cache-names-prefix config/release-number))
