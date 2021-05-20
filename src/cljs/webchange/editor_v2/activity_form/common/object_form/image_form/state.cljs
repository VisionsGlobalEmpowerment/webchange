(ns webchange.editor-v2.activity-form.common.object-form.image-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [image-data (select-keys objects-data [:src])]
      {:dispatch [::state/init id {:data  image-data
                                   :names objects-names}]})))

;; Image Src

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [_]} [_ id src]]
    {:dispatch [::state/update-current-data id {:src src}]}))
