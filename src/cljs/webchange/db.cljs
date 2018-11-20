(ns webchange.db)

(def default-db
  {:name "re-frame"
   :playing false
   :viewport {:width 1920
              :height 1080}
   :current-course nil
   :current-scene nil
   :scene-loading-progress {}
   :scene-loading-complete {}
   :scenes {}
   :ui-screen :default
   :music-volume 50
   :effects-volume 50
   })
