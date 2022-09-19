(ns webchange.lesson-builder.layout.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.index :refer [tools-data]]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.lesson-builder.layout.toolbox.state :as toolbox-state]
    [webchange.lesson-builder.state :as activity-state]))

(def path-to-db :lesson-builder/layout)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::activity-loading?
  :<- [::activity-state/activity-loading?]
  :<- [::activity-state/activity-info-loading?]
  (fn [[activity-loading? activity-info-loading?]]
    (or activity-loading? activity-info-loading?)))

;; focused blocks

(def focused-blocks-key :focused-blocks)

(defn- set-focused-blocks
  [db value]
  (assoc db focused-blocks-key value))

(re-frame/reg-sub
  ::focused-blocks
  :<- [path-to-db]
  #(get % focused-blocks-key #{}))

;; tools

(def current-tool-key :current-tool)

(defn- set-current-tool
  [db value]
  (assoc db current-tool-key value))

(re-frame/reg-sub
  ::current-tool
  :<- [path-to-db]
  #(get % current-tool-key :default))

(re-frame/reg-event-fx
  :layout/open-tool
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ tool-id tool-props options]]
    (print "open-tool" tool-id tool-props)
    (let [{:keys [init reset menu] :as tool-data} (get tools-data tool-id)
          focus (get tool-data :focus #{})
          destroy-event (when (some? reset)
                          [reset tool-props])]
      {:db         (cond-> db
                           (some? focus) (set-focused-blocks focus))
       :dispatch-n (cond-> [[::toolbox-state/set-current-widget tool-id]]
                           (some? menu) (conj [::menu-state/open-component tool-id (merge options {:on-back (cond-> [[:layout/reset]]
                                                                                                                    (some? destroy-event) (conj destroy-event))})])
                           (some? init) (conj [init (merge tool-props {:reset [:layout/reset]})]))})))

(re-frame/reg-event-fx
  :layout/reset
  (fn [{:keys [_]} [_]]
    {:dispatch [:layout/open-tool :default]}))
