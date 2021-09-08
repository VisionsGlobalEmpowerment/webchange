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
    (let [image-data (merge {:scale {:x 1 :y 1}}
                            (select-keys objects-data [:src :scale]))
          image-tags (get-in objects-data [:editable? :image-tags])
          form-params (get-in objects-data [:editable? :edit-form])]
      {:dispatch-n [[::state/init id {:data        image-data
                                      :names       objects-names
                                      :form-params form-params}]
                    [::set-image-tags id image-tags]]})))

(re-frame/reg-sub
  ::show-select-image-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :select-image]))
  (fn [show-control?]
    show-control?))

(re-frame/reg-sub
  ::show-upload-image-control?
  (fn [[_ id]]
    (re-frame/subscribe [::state/form-component-available? id :upload-image]))
  (fn [show-control?]
    show-control?))

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