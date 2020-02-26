(ns webchange.resources.core-game-app)

(defn get-game-app-resources
  []
  (let [endpoints ["/api/schools/current"]]
    {:resources  []
     :endpoints  endpoints}))
