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
  [{:keys [src]} {:keys [width padding]}]
  (let [content {:src        src
                 :image-size "contain"
                 :origin     {:type "top-left"}}
        side-length (->> (* 2 padding) (- width))
        dimensions {:x      padding
                    :y      padding
                    :width  side-length
                    :height side-length}]
    (merge {:type      "image"
            :editable? {:drag      true
                        :select    true
                        :edit-form {:flip         true
                                    :scale        true
                                    :visible      true
                                    :select-image true
                                    :upload-image true}}}
           content dimensions)))

(defn- sort-children
  [children activity-data]
  (loop [[object-name & rest-objects] (vec children)
         images []
         texts []
         etc []]
    (if (nil? object-name)
      (concat etc images texts)
      (let [object-type (get-in activity-data [:objects (keyword object-name) :type])]
        (case object-type
          "image" (recur rest-objects (conj images object-name) texts etc)
          "text" (recur rest-objects images (conj texts object-name) etc)
          (recur rest-objects images texts (conj etc object-name)))))))

(defn add-image
  [activity-data page-idx {:keys [image page-params]}]
  (let [{:keys [object]} (f/get-page-data activity-data page-idx)
        image-name (get-image-name activity-data object)
        image-data (get-image-data image page-params)
        image-asset {:url (:src image) :size 1 :type "image"}
        updated-activity-data (-> activity-data
                                  (update :assets conj image-asset)
                                  (update :objects assoc (keyword image-name) image-data)
                                  (update-in [:objects (keyword object) :children] concat [image-name]))]
    (update-in updated-activity-data [:objects (keyword object) :children] sort-children updated-activity-data)))
