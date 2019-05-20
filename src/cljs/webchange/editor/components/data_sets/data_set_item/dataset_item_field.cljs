(ns webchange.editor.components.data-sets.data-set-item.dataset-item-field
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :refer [Button
                           Divider
                           Form
                           FormField
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
    [webchange.editor.common.actions.action-form :refer [action-form]]
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
  (r/with-let [modal-open (r/atom false)
               selected-scene (r/atom (:scene-id value))
               props (r/atom value)]
              (let [scenes (re-frame/subscribe [::subs/course-scenes])
                    params {:scene-id @selected-scene}
                    description (:description value)]
                [Modal {:open    @modal-open
                        :trigger (r/as-element [:div
                                                [Button {:basic    true
                                                         :on-click #(reset! modal-open true)}
                                                 (if value "Edit Action" "Set Action")]
                                                (and description [Label {:tag true} description])])}
                 [ModalHeader {} "Edit dataset item action"]
                 [ModalContent {:scrolling true}
                  [FormField {:inline true}
                   [FormSelect {:label         "Scene: "
                                :placeholder   "Select scene"
                                :default-value @selected-scene
                                :options       (get-options-from-plain-list @scenes)
                                :on-change     #(do (reset! props {})
                                                    (reset! selected-scene (.-value %2)))}]]
                  [Divider]
                  (if-not @selected-scene
                    [action-placeholder]
                    [Form {} [action-form props params]])
                  ]
                 [ModalActions {}
                  [Button {:basic    true
                           :on-click #(reset! modal-open false)} "Cancel"]
                  [Button {:primary  true
                           :on-click #(do (on-change (merge @props {:scene-id @selected-scene}))
                                          (reset! modal-open false))} "Save"]]
                 ]
                )))

;
; Unknown type
;

(defmethod dataset-item-control :default
  [_]
  [Label {:color      "red"
          :horizontal true}
   "Unsupported type"])