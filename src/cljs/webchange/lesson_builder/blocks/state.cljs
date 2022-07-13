(ns webchange.lesson-builder.blocks.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.toolbox.state :as toolbox-state]))

(def path-to-db :lesson-builder/layout)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; focused blocks

(def focused-blocks-key :focused-blocks)

(defn- set-focused-blocks
  [db value]
  (assoc db focused-blocks-key value))

(re-frame/reg-sub
  ::focused-blocks
  :<- [path-to-db]
  #(get % focused-blocks-key #{}))

;; general

(def states
  {:default                 {:toolbox :welcome
                             :focus   #{}}
   :change-background-image {:toolbox :background-image
                             :focus   #{:toolbox :stage}}})

(re-frame/reg-event-fx
  ::set-state
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ state-id]]
    (let [state (get states state-id)]
      {:db         (cond-> db
                           (contains? state :focus) (set-focused-blocks (:focus state)))
       :dispatch-n (cond-> []
                           (contains? state :toolbox) (conj [::toolbox-state/set-current-widget (:toolbox state)]))})))

(re-frame/reg-event-fx
  ::reset-state
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-state :default]}))
