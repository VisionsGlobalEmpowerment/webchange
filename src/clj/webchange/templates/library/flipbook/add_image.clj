(ns webchange.templates.library.flipbook.add-image
  (:require
    [clojure.tools.logging :as log]
    [webchange.utils.flipbook :as f]
    [webchange.utils.scene-data :as s]))

(defn- get-image-name
  [activity-data page-object-name]
  (let [{:keys [children]} (->> (keyword page-object-name)
                                (s/get-scene-object activity-data))]
    (str page-object-name "-image-" (count children))))

(defn- get-image-data
  [{:keys [src]}]
  (let [content {:src        src
                 :image-size "contain"
                 :origin     {:type "center-center"}}
        dimensions {:x      300
                    :y      300
                    :width  832
                    :height 744}]
    (merge {:type      "image"
            :editable? {:drag      true
                        :select    true
                        :edit-form {:flip         true
                                    :scale        true
                                    :visible      true
                                    :select-image true
                                    :upload-image true}}}
           content dimensions)))

(defn add-image
  [activity-data page-idx image-params]
  (log/debug "---------- add-image")
  (log/debug "page-idx" page-idx)
  (log/debug "image-params" image-params)
  (let [{:keys [object]} (f/get-page-data activity-data page-idx)
        image-name (get-image-name activity-data object)
        image-data (get-image-data image-params)]
    (-> activity-data
        (update :objects assoc (keyword image-name) image-data)
        (update-in [:objects (keyword object) :children] conj image-name))))
