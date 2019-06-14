(ns webchange.db)

(def default-db
  {:name "re-frame"
   :active-page :home-panel
   :playing false
   :scene-started false
   :viewport {:width 1920
              :height 1080}
   :current-course nil
   :current-scene nil
   :scene-loading-progress {}
   :scene-loading-complete {}
   :scenes {}
   :ui-screen :default
   :music-volume 10
   :effects-volume 30
   :dashboard {:access-codes {}}
   })
