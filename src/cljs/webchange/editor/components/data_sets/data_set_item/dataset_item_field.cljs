(ns webchange.editor.components.data-sets.data-set-item.dataset-item-field
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :refer [Button
                           Divider
                           FormInput
                           FormSelect
                           Header
                           Icon
                           Label
                           Modal
                           ModalActions
                           ModalContent
                           ModalHeader
                           Segment]]
    [webchange.subs :as subs]))

(defmulti dataset-item-control #(:type %))

;
; String-typed
;

(defmethod dataset-item-control "string"
  [{:keys [on-change value]}]
  [FormInput {:inline        true
              :default-value value
              :on-change     #(on-change (-> %2 .-value))}])

;
; Action-typed
;

;; ToDo: move to common ui controls helpers
(defn- get-options-from-plain-list
  [l]
  (->> l
       (map #(hash-map :key % :value % :text %))
       (vec)))

(defn- action-placeholder
  []
  [Segment {:placeholder true}
   [Header {:icon true}
    [Icon {:name    "level up"
           :flipped "horizontally"}]
    "Select Scene"]])

(defmethod dataset-item-control "action"
  [{:keys [on-change value]}]
  (r/with-let [modal-open (r/atom false)]
              [Modal {:open @modal-open
                      :trigger (r/as-element [Button {:basic    true
                                                      :on-click #(reset! modal-open true)}
                                              (or value "Set Action")])}
               [ModalHeader {} "Edit dataset item action"]
               (let [scenes (re-frame/subscribe [::subs/course-scenes])]
                 [ModalContent {:scrolling true}
                  [FormSelect {:placeholder "Select scene"
                               :options     (get-options-from-plain-list @scenes)
                               :on-change   #(println (.-value %2))}]
                  [Divider]
                  [action-placeholder]])
               [ModalActions {}
                [Button {:basic true
                         :on-click #(reset! modal-open false)} "Cancel"]
                [Button {:primary true
                         :on-click #(do (reset! modal-open false)
                                        (println "Save data"))} "Save"]]
               ]))

(defmethod dataset-item-control :default
  [_]
  [Label {:color      "red"
          :horizontal true}
   "Unsupported type"])