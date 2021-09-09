(ns webchange.common.image-selector.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.image-selector.state :as parent]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :refer [without-item]]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:form])
       (parent/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id tags-names]]
    {:dispatch [::load-tags id tags-names]}))


;; Current Tag

(def current-tag-path [:current-tag])

(defn- get-current-tags
  [db id]
  (get-in db (path-to-db id current-tag-path)))

(re-frame/reg-sub
  ::current-tags
  (fn [db [_ id]]
    (get-current-tags db id)))

(re-frame/reg-event-fx
  ::set-current-tags
  (fn [{:keys [db]} [_ id tags]]
    (let [value (if (and (string? tags) (empty? tags)) nil tags)]
      {:db       (assoc-in db (path-to-db id current-tag-path) value)
       :dispatch [::load-assets id value]})))

(re-frame/reg-event-fx
  ::update-current-tags
  (fn [{:keys [db]} [_ id tag-id selected?]]
    (let [current-tags (get-current-tags db id)]
      {:dispatch [::set-current-tags id (if selected?
                                          (conj current-tags tag-id)
                                          (without-item current-tags tag-id))]})))

;; Tags

(def tags-path [:tags])

(re-frame/reg-event-fx
  ::load-tags
  (fn [{:keys [_]} [_ id current-tags-names]]
    {:dispatch [::warehouse/load-assets-tags {:on-success [::set-tags id current-tags-names]}]}))

(re-frame/reg-event-fx
  ::set-tags
  (fn [{:keys [db]} [_ id current-tags-names tags]]
    (let [current-tags-ids (map (fn [tag-name]
                                  (some (fn [{:keys [name id]}]
                                          (and (= name tag-name) id))
                                        tags))
                                current-tags-names)]
      {:db       (assoc-in db (path-to-db id tags-path) tags)
       :dispatch [::set-current-tags id current-tags-ids]})))

(re-frame/reg-sub
  ::available-tags
  (fn [db [_ id]]
    (get-in db (path-to-db id tags-path))))

(re-frame/reg-sub
  ::tags-options
  (fn [[_ id]]
    [(re-frame/subscribe [::available-tags id])
     (re-frame/subscribe [::current-tags id])])
  (fn [[available-tags current-tags]]
    (->> available-tags
         (map (fn [{:keys [id name]}]
                {:text      name
                 :value     id
                 :selected? (or (some #{id} current-tags) false)}))
         (sort-by :text))))

;; Assets

(def assets-path [:assets])

(re-frame/reg-event-fx
  ::load-assets
  (fn [{:keys [_]} [_ id tags]]
    {:dispatch [::warehouse/load-assets {:tags tags} {:on-success [::set-assets id]}]}))

(re-frame/reg-event-fx
  ::set-assets
  (fn [{:keys [db]} [_ id assets]]
    {:db (assoc-in db (path-to-db id assets-path) assets)}))

(re-frame/reg-sub
  ::assets
  (fn [db [_ id]]
    (get-in db (path-to-db id assets-path) [])))
