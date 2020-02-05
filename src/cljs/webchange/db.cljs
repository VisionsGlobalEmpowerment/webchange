(ns webchange.db)

(def default-db
  {:name                   "re-frame"
   :active-page            :home-panel
   :playing                false
   :scene-started          false
   :viewport               {:width  1920
                            :height 1080}
   :current-course         "test"
   :current-scene          nil
   :scene-loading-progress {}
   :scene-loading-complete {}
   :scenes                 {}
   :ui-screen              :default
   :settings               {:music-volume   10
                            :effects-volume 30}
   :dashboard              {:access-codes {}}
   :service-worker         {:synced-resources {:game []}
                            :offline-mode     :not-started}
   :sync-resources         {:list-open false
                            :scenes    {:loading false
                                        :data    {}}}
   })
