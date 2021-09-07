(ns webchange.editor-v2.activity-form.common.object-form.image-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:image-form])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [image-data (select-keys objects-data [:src])
          image-tags (get-in objects-data [:editable? :image-tags])]
      (print "image-tags" image-tags)
      {:dispatch-n [[::state/init id {:data  image-data
                                      :names objects-names}]
                    [::set-image-tags id image-tags]]})))

;; Image Src

(re-frame/reg-sub
  ::image-src
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :src)))

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [_]} [_ id src]]
    {:dispatch [::state/update-current-data id {:src src}]}))

;; Image Tag

(def image-tags-path :image-tag)

(re-frame/reg-event-fx
  ::set-image-tags
  (fn [{:keys [db]} [_ id tag]]
    {:db (assoc-in db (path-to-db id [image-tags-path]) tag)}))

(re-frame/reg-sub
  ::image-tags
  (fn [db [_ id]]
    (get-in db (path-to-db id [image-tags-path]))))