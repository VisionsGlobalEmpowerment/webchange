(ns webchange.editor.components.data-sets.data-set-item.dataset-item-field
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :refer [Button
                             ButtonGroup
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
    [webchange.editor.common.insert-json-modal :refer [insert-json-modal]]
    [webchange.subs :as subs]
    [webchange.editor.subs :as editor.subs]
    [webchange.editor.common.actions.subs :as actions.subs]
    [webchange.editor.common.actions.events :as actions.events]))

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

(defn- action-properties-panel
  [scene-id]
  (r/with-let [form-data @(re-frame/subscribe [::actions.subs/form-data])
               selected-scene (r/atom scene-id)
               props (r/atom form-data)]
    (let [scenes (re-frame/subscribe [::subs/course-scenes])
          params {:scene-id @selected-scene}]
      [ModalContent {:scrolling true}
       [FormField {:inline true}
        [FormSelect {:label         "Scene: "
                     :placeholder   "Select scene"
                     :default-value @selected-scene
                     :options       (get-options-from-plain-list @scenes)
                     :on-change     #(do
                                       (reset! selected-scene (.-value %2))
                                       (re-frame/dispatch [::actions.events/set-form-data {:scene-id @selected-scene}]))}]]
       [Divider]
       (if-not @selected-scene
         [action-placeholder]
         [Form {}
          [action-form props params]
          [Button {:primary  true
                   :on-click #(do (re-frame/dispatch [::actions.events/edit-selected-action @props]))} "Apply"]])
       ])))

(defn- action-properties-panel-with-key [scene-id]
  (let [hash @(re-frame/subscribe [::actions.subs/form-data-hash])]
    ^{:key hash}
    [action-properties-panel scene-id]))

(defmethod dataset-item-control "action"
  [{:keys [on-change value]}]
  (r/with-let [modal-open (r/atom false)]
    (let [description (:description value)
          data @(re-frame/subscribe [::actions.subs/form-data-original])]
      [:div {}
       [ButtonGroup {}
        [insert-json-modal {:text "JSON"
                            :value (clj->js value)
                            :on-change #(on-change (js->clj %))}]
        [Modal {:open    @modal-open
                :trigger (r/as-element [:div
                                        [Button {:basic    true
                                                 :on-click #(do
                                                              (re-frame/dispatch [::actions.events/set-form-data value])
                                                              (reset! modal-open true))}
                                         (if value "Edit Action" "Set Action")]
                                        (and description [Label {:tag true} description])])}
         [ModalHeader {} "Edit dataset item action"]
         [action-properties-panel-with-key (:scene-id data)]
         [ModalActions {}
          [Button {:basic    true
                   :on-click #(reset! modal-open false)} "Cancel"]
          [Button {:primary  true
                   :on-click #(do (on-change data)
                                  (reset! modal-open false))} "Save"]]]]])))

;
; Unknown type
;

(defmethod dataset-item-control :default
  [_]
  [Label {:color      "red"
          :horizontal true}
   "Unsupported type"])