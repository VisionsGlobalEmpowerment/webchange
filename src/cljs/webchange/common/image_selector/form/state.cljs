(ns webchange.common.image-selector.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.image-selector.state :as parent]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [without-item]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:form])
       (parent/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ tags-names]]
    {:dispatch [::load-tags tags-names]}))


;; Current Tag

(def current-tag-path (path-to-db [:current-tag]))

(defn- get-current-tags
  [db]
  (get-in db current-tag-path))

(re-frame/reg-sub
  ::current-tags
  get-current-tags)

(re-frame/reg-event-fx
  ::set-current-tags
  (fn [{:keys [db]} [_ tags]]
    (let [value (if (and (string? tags) (empty? tags)) nil tags)]
      {:db       (assoc-in db current-tag-path value)
       :dispatch [::load-assets value]})))

(re-frame/reg-event-fx
  ::update-current-tags
  (fn [{:keys [db]} [_ tag-id selected?]]
    (let [current-tags (get-current-tags db)]
      {:dispatch [::set-current-tags (if selected?
                                       (conj current-tags tag-id)
                                       (without-item current-tags tag-id))]})))

;; Tags

(def tags-path (path-to-db [:tags]))

(re-frame/reg-event-fx
  ::load-tags
  (fn [{:keys [_]} [_ current-tags-names]]
    {:dispatch [::warehouse/load-assets-tags {:on-success [::set-tags current-tags-names]}]}))

(re-frame/reg-event-fx
  ::set-tags
  (fn [{:keys [db]} [_ current-tags-names tags]]
    (let [current-tags-ids (map (fn [tag-name]
                                  (some (fn [{:keys [name id]}]
                                          (and (= name tag-name) id))
                                        tags))
                                current-tags-names)]
      {:db       (assoc-in db tags-path tags)
       :dispatch [::set-current-tags current-tags-ids]})))

(re-frame/reg-sub
  ::available-tags
  (fn [db]
    (get-in db tags-path)))

(re-frame/reg-sub
  ::tags-options
  (fn []
    [(re-frame/subscribe [::available-tags])
     (re-frame/subscribe [::current-tags])])
  (fn [[available-tags current-tags]]
    (->> available-tags
         (map (fn [{:keys [id name]}]
                {:text      name
                 :value     id
                 :selected? (or (some #{id} current-tags) false)}))
         (sort-by :text))))

;; Assets

(def assets-path (path-to-db [:assets]))

(re-frame/reg-event-fx
  ::load-assets
  (fn [{:keys [_]} [_ tags]]
    {:dispatch [::warehouse/load-assets {:tags tags} {:on-success [::set-assets]}]}))

(re-frame/reg-event-fx
  ::set-assets
  (fn [{:keys [db]} [_ assets]]
    {:db (assoc-in db assets-path assets)}))

(re-frame/reg-sub
  ::assets
  (fn [db]
    (get-in db assets-path [])))
