(ns webchange.lesson-builder.tools.image-add.state
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.lesson-builder.widgets.confirm.state :as confirm]))

(def path-to-db :lesson-builder/image-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:form :name] "")}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

;;name

(def name-error-key :name-error)

(defn- set-name-error
  [db value]
  (assoc db name-error-key value))

(re-frame/reg-sub
  ::name-error
  :<- [path-to-db]
  #(get % name-error-key))

(re-frame/reg-sub
  ::name
  :<- [path-to-db]
  #(get-in % [:form :name]))

(re-frame/reg-sub
  ::name-defined?
  :<- [::name]
  #(-> % empty? not))

(re-frame/reg-event-fx
  ::set-name
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:form :name] value)}))

(re-frame/reg-sub
  ::image
  :<- [path-to-db]
  #(get-in % [:form :image]))

(re-frame/reg-sub
  ::image-selected?
  :<- [::image]
  (fn [image]
    (-> (get image :src)
        (some?))))

(re-frame/reg-event-fx
  ::set-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [name (-> value :src (str/split #"--") last)]
      {:db (cond-> db
                   :always (assoc-in [:form :image] value)
                   (some? name) (assoc-in [:form :name] name))})))
(comment
  (-> @re-frame.db/app-db
      (get path-to-db))
  (let [value {:src "etc--elements-concepts--mother.png"}]
    (-> value
        :src
        (str/split #"--") last)))

(re-frame/reg-sub
  ::apply-disabled?
  :<- [::image-selected?]
  :<- [::name-defined?]
  (fn [[image-selected? name-defined?]]
    (-> (and image-selected?
             name-defined?)
        (not))))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [data (get db :form)]
      (cond
        (empty? (:name data)) {:db (set-name-error db "Name is required")}
        (:page-number data) {:dispatch [::lesson-builder-state/add-image data {:on-success [::apply-success]
                                                                               :common-action? false}]}
        :else {:dispatch [::lesson-builder-state/add-image data {:on-success [::apply-success]}]}))))

(re-frame/reg-event-fx
  ::apply-success
  (fn [{:keys [_]} [_]]
    {:dispatch [::confirm/show-message-window
                {:title      "Image Added"
                 :message    "Image is added to scene layers, you can edit or change the image in the activity form there and choose when to show or hide."
                 :on-confirm [::menu-state/history-back]}]}))

(re-frame/reg-event-fx
  ::set-page-number
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:form :page-number] value)}))
