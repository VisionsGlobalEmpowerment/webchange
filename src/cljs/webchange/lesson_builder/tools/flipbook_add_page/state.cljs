(ns webchange.lesson-builder.tools.flipbook-add-page.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state-flipbook :as flipbook-state]))

(def path-to-db :lesson-builder/flipbook-add-page)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def available-layouts
  [;; page
   {:value  "text-over-image-at-top"
    :params ["text" "image"]
    :icon   "page-text-over-image-at-top"
    :type   "page"}
   {:value  "text-small-at-bottom"
    :params ["text" "image"]
    :icon   "page-text-small-at-bottom"
    :type   "page"}
   {:value  "text-big-at-bottom"
    :params ["text" "image"]
    :icon   "page-text-big-at-bottom"
    :type   "page"}
   {:value  "image-only"
    :params ["image"]
    :icon   "page-image-only"
    :type   "page"}
   {:value  "text-over-image-at-bottom"
    :params ["text" "image"]
    :icon   "page-text-over-image-at-bottom"
    :type   "page"}
   {:value  "text-only"
    :params ["text"]
    :icon   "page-text-only"
    :type   "page"}
   {:value  "text-at-top"
    :params ["text" "image"]
    :icon   "page-text-at-top"
    :type   "page"}

   ;; spread
   {:value  "text-right-top"
    :params ["text" "image"]
    :icon   "spread-text-right-top"
    :type   "spread"}
   {:value  "text-right-bottom"
    :params ["text" "image"]
    :icon   "spread-text-right-bottom"
    :type   "spread"}
   {:value  "text-left-top"
    :params ["text" "image"]
    :icon   "spread-text-left-top"
    :type   "spread"}
   {:value  "text-left-bottom"
    :params ["text" "image"]
    :icon   "spread-text-left-bottom"
    :type   "spread"}
   {:value  "image-only"
    :params ["image"]
    :icon   "spread-image-only"
    :type   "spread"}])

;; current layout

(def current-layout-key :current-layout)

(defn- get-current-layout
  [db]
  (get db current-layout-key))

(defn- set-current-layout
  [db value]
  (assoc db current-layout-key value))

(re-frame/reg-sub
  ::current-layout
  :<- [path-to-db]
  get-current-layout)

(re-frame/reg-event-fx
  ::set-current-layout
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-current-layout db value)}))

(re-frame/reg-sub
  ::layout-selected?
  :<- [::current-layout]
  some?)

;; layout type

(def layout-type-key :layout-type)

(defn- get-layout-type
  [db]
  (get db layout-type-key "page"))

(re-frame/reg-sub
  ::layout-type
  :<- [path-to-db]
  get-layout-type)

(re-frame/reg-event-fx
  ::set-layout-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (assoc layout-type-key value)
             (set-current-layout nil))}))

;; layout options

(re-frame/reg-sub
  ::available-layouts
  :<- [::layout-type]
  :<- [::current-layout]
  (fn [[layout-type current-layout]]
    (->> available-layouts
         (filter (fn [{:keys [type]}]
                   (= type layout-type)))
         (map (fn [{:keys [value] :as layout-data}]
                (assoc layout-data :selected? (= value current-layout)))))))

(re-frame/reg-sub
  ::layout-params
  :<- [::current-layout]
  (fn [current-layout-value]
    (if-let [current-layout-data (some (fn [{:keys [value] :as layout-data}]
                                         (and (= value current-layout-value) layout-data))
                                       available-layouts)]
      (->> (get current-layout-data :params [])
           (map (fn [param-type]
                  {:id   (keyword param-type)
                   :type param-type})))
      [])))

;; form params

(def form-params-key :form-params)

(defn get-form-params
  [db]
  (get db form-params-key))

(re-frame/reg-sub
  ::form-param
  :<- [path-to-db]
  (fn [db [_ param]]
    (get-in db [form-params-key param])))

(re-frame/reg-event-fx
  ::set-form-param
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ param value]]
    {:db (assoc-in db [form-params-key param] value)}))

(re-frame/reg-sub
  ::form-params
  :<- [path-to-db]
  (fn [db]
    (get db form-params-key)))

;; actions

(def options-key :options)

(defn- get-options
  [db]
  (get db options-key))

(defn- set-options
  [db value]
  (assoc db options-key value))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ options]]
    {:db (set-options db options)}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::cancel
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [reset]} (get-options db)]
      {:dispatch-n (cond-> [[::reset]]
                           (some? reset) (conj reset))})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [form-data (get-form-params db)
          layout-type (get-layout-type db)
          layout (get-current-layout db)
          page-data (merge form-data
                           {:type                      layout-type
                            (case layout-type
                              "page" :page-layout
                              "spread" :spread-layout) layout})]
      {:dispatch [::flipbook-state/add-page page-data {:on-success [::cancel]}]})))

(re-frame/reg-sub
  ::save-button-disabled?
  :<- [::current-layout]
  :<- [::layout-params]
  :<- [::form-params]
  (fn [[current-layout layout-params form-params]]
    (-> (and (some? current-layout)
             (->> layout-params
                  (map (fn [{:keys [id]}]
                         (get form-params id)))
                  (every? some?)))
        (not))))
