(ns webchange.lesson-builder.tools.flipbook-add-page.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :lesson-builder/flipbook-add-page)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::available-page-layouts
  (constantly
    (->> [{:value "text-over-image-at-top"
           :icon  "page-text-over-image-at-top"}
          {:value "text-small-at-bottom"
           :icon  "page-text-small-at-bottom"}
          {:value "text-big-at-bottom"
           :icon  "page-text-big-at-bottom"}
          {:value "image-only"
           :icon  "page-image-only"}
          {:value "text-over-image-at-bottom"
           :icon  "page-text-over-image-at-bottom"}
          {:value "text-only"
           :icon  "page-text-only"}
          {:value "text-at-top"
           :icon  "page-text-at-top"}]
         (map #(assoc % :type "page")))))

(re-frame/reg-sub
  ::available-spread-layouts
  (constantly
    (->> [{:value "text-right-top"
           :icon  "spread-text-right-top"}
          {:value "text-right-bottom"
           :icon  "spread-text-right-bottom"}
          {:value "text-left-top"
           :icon  "spread-text-left-top"}
          {:value "text-left-bottom"
           :icon  "spread-text-left-bottom"}
          {:value "image-only"
           :icon  "spread-image-only"}]
         (map #(assoc % :type "spread")))))

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
  :<- [::available-page-layouts]
  :<- [::available-spread-layouts]
  :<- [::layout-type]
  :<- [::current-layout]
  (fn [[page-layouts spread-layouts layout-type current-layout]]
    (->> (case layout-type
           "page" page-layouts
           "spread" spread-layouts
           [])
         (map (fn [{:keys [value] :as layout-data}]
                (assoc layout-data :selected? (= value current-layout)))))))
