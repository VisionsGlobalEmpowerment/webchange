(ns webchange.book-library.components.categories.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:categories])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-book-categories {:on-success [::load-book-categories-success]}]}))

(re-frame/reg-event-fx
  ::load-book-categories-success
  (fn [{:keys [_]} [_ categories]]
    {:dispatch [::set-available-categories categories]}))

;; Available categories

(def available-categories-path (path-to-db [:available-categories]))

(defn get-available-categories
  [db]
  (get-in db available-categories-path))

(re-frame/reg-sub
  ::available-categories
  get-available-categories)

(re-frame/reg-event-fx
  ::set-available-categories
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-categories-path data)}))

;; Categories options

(def category-images-path "/images/book_library/categories/")
(def category-images {"animals"            "animals.png"
                      "family-and-friends" "family.png"
                      "science-stem"       "science.png"
                      "sports"             "sports.png"
                      "vehicles"           "transport.png"})

(defn- get-category-image
  [category]
  (->> (get category-images category)
       (str category-images-path)))

(re-frame/reg-sub
  ::category-image
  (fn [_ [_ category]]
    (get-category-image category)))

(re-frame/reg-sub
  ::categories-options
  (fn []
    [(re-frame/subscribe [::available-categories])])
  (fn [[available-categories]]
    (->> available-categories
         (map (fn [{:keys [value] :as category}]
                (merge category
                       {:image (get-category-image value)}))))))
