(ns webchange.lesson-builder.blocks.menu.menu-tabs.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.menu.state :as menu-state]))

(def path-to-db :lesson-builder/menu)

;; tabs

(def menu-tabs {:design       {:title     "Design"
                               :component :design-actions}
                :scene-layers {:title     "Scene Layers"
                               :component :scene-layers}
                :settings     {:title     "Settings"
                               :component :settings}})

(re-frame/reg-sub
  ::menu-tabs
  (fn []
    (map (fn [[id data]]
           (assoc data :id id))
         menu-tabs)))

(def current-tab-key :current-tab)

(re-frame/reg-sub
  ::current-tab
  :<- [path-to-db]
  #(get % current-tab-key :design))

(re-frame/reg-event-fx
  ::set-current-tab
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ tab-key]]
    (let [{:keys [component]} (get menu-tabs tab-key)]
      {:db       (assoc db current-tab-key tab-key)
       :dispatch [::menu-state/open-component component {:reset-history? true}]})))
