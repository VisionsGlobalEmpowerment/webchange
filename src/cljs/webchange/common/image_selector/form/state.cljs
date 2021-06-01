(ns webchange.common.image-selector.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.image-selector.state :as parent]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:form])
       (parent/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ tag]]
    {:dispatch-n [[::set-current-tag tag]
                  [::load-tags]
                  [::load-assets tag]]}))

;; Tags

(def tags-path (path-to-db [:tags]))

(re-frame/reg-event-fx
  ::load-tags
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-assets-tags {:on-success [::set-tags]}]}))

(re-frame/reg-event-fx
  ::set-tags
  (fn [{:keys [db]} [_ tags]]
    {:db (assoc-in db tags-path tags)}))

(re-frame/reg-sub
  ::tags-options
  (fn [db]
    (->> (get-in db tags-path)
         (map (fn [{:keys [id name]}]
                {:text  name
                 :value id}))
         (concat [{:text  "All"
                   :value ""}]))))

;; Current Tag

(def current-tag-path (path-to-db [:current-tag]))

(re-frame/reg-sub
  ::current-tag
  (fn [db]
    (get-in db current-tag-path)))

(re-frame/reg-event-fx
  ::set-current-tag
  (fn [{:keys [db]} [_ tag]]
    (let [value (if (and (string? tag) (empty? tag)) nil tag)]
      {:db       (assoc-in db current-tag-path value)
       :dispatch [::load-assets value]})))

;; Assets

(def assets-path (path-to-db [:assets]))

(re-frame/reg-event-fx
  ::load-assets
  (fn [{:keys [_]} [_ tag]]
    {:dispatch [::warehouse/load-assets {:tag tag} {:on-success [::set-assets]}]}))

(re-frame/reg-event-fx
  ::set-assets
  (fn [{:keys [db]} [_ assets]]
    {:db (assoc-in db assets-path assets)}))

(re-frame/reg-sub
  ::assets
  (fn [db]
    (get-in db assets-path [])))
