(ns webchange.book-creator.asset-form.image-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.asset-form.state :refer [path-to-db] :as state]
    [webchange.logger.index :as logger]
    [webchange.state.utils :refer [get-scene-object]]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id object-data]]
    (let [image-name (get-in object-data [:image :name])
          image-data (-> object-data
                         (get-in [:image :data])
                         (select-keys [:src]))
          spread-image (get-in object-data [:image :data :spread-image-name])
          [paired-image-name] (if (some? spread-image)
                                (get-scene-object db (fn [object-name {:keys [spread-image-name]}]
                                                       (and (= spread-image-name spread-image)
                                                            (not= object-name image-name))))
                                [])]

      (logger/group-folded "Init image form" id)
      (logger/trace "image-name" image-name)
      (logger/trace "image-data" image-data)
      (logger/trace "spread-image" spread-image)
      (logger/trace "paired-image-name" paired-image-name)
      (logger/group-end "Init image form" id)

      {:dispatch [::state/init id {:assets-names (cond-> [image-name]
                                                         (some? paired-image-name) (conj paired-image-name))
                                   :data         image-data}]})))

;; Image Src

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [_]} [_ id src]]
    {:dispatch [::state/update-current-data id {:src src}]}))
