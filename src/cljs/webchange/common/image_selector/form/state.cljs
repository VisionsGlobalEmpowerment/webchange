(ns webchange.common.image-selector.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.image-selector.state :as parent]
    [webchange.state.warehouse :as warehouse]))

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

(def current-query [:current-query])
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
    (let [query (get-in db (path-to-db id current-query) "")]
      {:db       (assoc-in db (path-to-db id current-tag-path) tags)
       :dispatch [::load-assets id tags query]})))

(re-frame/reg-sub
  ::current-query
  (fn [db [_ id]]
    (get-in db (path-to-db id current-query) "")))

(re-frame/reg-event-fx
  ::update-search-query
  (fn [{:keys [db]} [_ id query]]
    (let [value (get-in db (path-to-db id current-tag-path))]
      {:db (assoc-in db (path-to-db id current-query) query)
       :dispatch [::load-assets id value query]})))

;; Tags

(def tags-path [:tags])

(re-frame/reg-event-fx
  ::load-tags
  (fn [{:keys [_]} [_ id current-tags-names]]
    {:dispatch [::warehouse/load-assets-tags-by-names
                {:tags current-tags-names}
                {:on-success [::set-current-tags id]}]}))

;; Assets

(def assets-path [:assets])

(re-frame/reg-event-fx
  ::load-assets
  (fn [{:keys [_]} [_ id tags query]]
    {:dispatch [::warehouse/search-assets {:tag (-> tags first :id)
                                           :query query} {:on-success [::set-assets id]}]}))

(re-frame/reg-event-fx
  ::set-assets
  (fn [{:keys [db]} [_ id assets]]
    {:db (assoc-in db (path-to-db id assets-path) assets)}))

(re-frame/reg-sub
  ::assets
  (fn [db [_ id]]
    (get-in db (path-to-db id assets-path) [])))
