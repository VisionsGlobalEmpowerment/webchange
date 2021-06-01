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
          image-tag (get-in objects-data [:editable? :image-tag])]
      {:dispatch-n [[::state/init id {:data  image-data
                                      :names objects-names}]
                    [::set-image-tag id image-tag]]})))

;; Image Src

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [_]} [_ id src]]
    {:dispatch [::state/update-current-data id {:src src}]}))

;; Image Tag

(def image-tag-path :image-tag)

(re-frame/reg-event-fx
  ::set-image-tag
  (fn [{:keys [db]} [_ id tag]]
    {:db (assoc-in db (path-to-db id [image-tag-path]) tag)}))

(re-frame/reg-sub
  ::image-tag
  (fn [db [_ id]]
    (get-in db (path-to-db id [image-tag-path]))))