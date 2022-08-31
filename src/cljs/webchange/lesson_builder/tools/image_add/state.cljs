(ns webchange.lesson-builder.tools.image-add.state
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.menu.state :as menu-state]
    [webchange.lesson-builder.state :as lesson-builder-state]))

(def path-to-db :lesson-builder/image-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :form {:name ""})}))

(re-frame/reg-sub
  ::name
  :<- [path-to-db]
  #(get-in % [:form :name]))

(re-frame/reg-event-fx
  ::set-name
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:form :name] value)}))

(re-frame/reg-sub
  ::image
  :<- [path-to-db]
  #(get-in % [:form :image]))

(re-frame/reg-event-fx
  ::set-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [name (-> value :src (str/split #"--") last)]
      {:db (cond-> db
                   :always (assoc-in [:form :image] value)
                   (some? name) (assoc-in [:form :name] name))})))
(comment
  (let [value {:src "etc--elements-concepts--mother.png"}]
    (-> value
        :src
        (str/split #"--") last)))


(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [data (get db :form)]
      {:dispatch-n [[::lesson-builder-state/add-image data]
                    [::menu-state/history-back]]})))

