(ns webchange.editor-v2.activity-form.common.objects-tree.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]))

; :uploaded-image-4                     {:y         540
;                                        :editable? {:select        true
;                                                    :show-in-tree? true}
;                                        :type      image
;                                        :src       /upload/HNSQRCIRRYTJWIZY.png
;                                        :alias     New Image
;                                                   :origin {:type center-center}
;                                                   :x 960
;                                                   :visible false
;                                                   :links [{:type action
;                                                            :id   show-uploaded-image-4}
;                                                           {:type action
;                                                            :id   hide-uploaded-image-4}]}

(re-frame/reg-sub
  ::objects
  (fn []
    [(re-frame/subscribe [::state/objects-data])])
  (fn [[objects-data]]
    (->> objects-data
         (filter (fn [[_ {:keys [editable?]}]]
                   (:show-in-tree? editable?)))
         (map (fn [[object-name {:keys [alias] :as object-data}]]
                (let []
                  {:name        (or alias object-name)
                   :object-name object-name
                   :object-data object-data}))))))
