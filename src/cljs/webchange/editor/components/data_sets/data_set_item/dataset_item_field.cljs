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
    [webchange.editor.common.actions.action-form :refer [action-form]]
    [webchange.editor.events :as events]
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

(defn- generate-action-name [] (str "action-" (.now js/Date)))

(defmethod dataset-item-control "action"
  [{:keys [on-change value]}]
  (r/with-let [modal-open (r/atom false)
               selected-scene (r/atom nil)]
              [Modal {:open @modal-open
                      :trigger (r/as-element [Button {:basic    true
                                                      :on-click #(reset! modal-open true)}
                                              (or value "Set Action")])}
               [ModalHeader {} "Edit dataset item action"]
               (let [scenes (re-frame/subscribe [::subs/course-scenes])]
                 [ModalContent {:scrolling true}
                  [FormSelect {:placeholder "Select scene"
                               :options     (get-options-from-plain-list @scenes)
                               :on-change   #(reset! selected-scene (.-value %2))}]
                  [Divider]
                  (if-not @selected-scene
                    [action-placeholder]
                    (let [_ (re-frame/dispatch [::events/select-current-scene @selected-scene])
                          _ (re-frame/dispatch [::events/add-new-scene-action (generate-action-name) nil])]
                      "Action form"))
                  ])
               [ModalActions {}
                [Button {:basic true
                         :on-click #(reset! modal-open false)} "Cancel"]
                [Button {:primary true
                         :on-click #(do (reset! modal-open false)
                                        (println "Save data"))} "Save"]]
               ]))

;
; Unknown type
;

(defmethod dataset-item-control :default
  [_]
  [Label {:color      "red"
          :horizontal true}
   "Unsupported type"])