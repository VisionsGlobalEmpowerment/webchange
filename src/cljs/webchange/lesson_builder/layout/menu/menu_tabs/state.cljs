(ns webchange.lesson-builder.layout.menu.menu-tabs.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.lesson-builder.state :as state]))

(def path-to-db :lesson-builder/menu)

;; tabs

(def menu-tabs {:design       {:title "Design"
                               :tool  :default}
                :scene-layers {:title     "Scene Layers"
                               :component :scene-layers}
                :settings     {:title     "Settings"
                               :component :settings}})

(re-frame/reg-sub
  ::menu-tabs
  :<- [::state/flipbook?]
  (fn [flipbook?]
    (->> menu-tabs
         (remove (fn [[id _data]]
                   (and flipbook? (= id :scene-layers))))
         (map (fn [[id data]]
                (assoc data :id id))))))

(def current-tab-key :current-tab)

(re-frame/reg-sub
  ::current-tab
  :<- [path-to-db]
  #(get % current-tab-key :design))

(re-frame/reg-event-fx
  ::set-current-tab
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ tab-key]]
    (let [{:keys [component tool]} (get menu-tabs tab-key)]
      (cond-> {:db (assoc db current-tab-key tab-key)}
              (some? tool) (assoc :dispatch [:layout/open-tool tool nil {:reset-history? true}])
              (some? component) (assoc :dispatch [::menu-state/open-component component {:reset-history? true}])))))
