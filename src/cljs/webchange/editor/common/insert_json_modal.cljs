(ns webchange.editor.common.insert-json-modal
  (:require
    [reagent.core :as r]
    [soda-ash.core :refer [Button
                           Form
                           FormField
                           Modal
                           ModalActions
                           ModalContent
                           ModalHeader
                           TextArea]]))

(defn insert-json-modal
  [{:keys [value text on-change]}]
  (r/with-let [modal-open (r/atom false)
               props (r/atom (.stringify js/JSON value))]
              [Modal {:open    @modal-open
                      :trigger (r/as-element [:div
                                              [Button {:basic    true
                                                       :on-click #(reset! modal-open true)}
                                               (if text text "Set Value")]])}
               [ModalHeader {} "Edit JSON value"]
               [ModalContent {:scrolling true}
                [Form {}
                 [FormField {:inline true}
                  [TextArea {:rows 10
                             :placeholder "JSON value"
                             :on-change     #(reset! props (.-value %2))
                             :value @props}]]]]
               [ModalActions {}
                [Button {:basic    true
                         :on-click #(reset! modal-open false)} "Cancel"]
                [Button {:primary  true
                         :on-click #(do (on-change (.parse js/JSON @props))
                                        (reset! modal-open false))} "Save"]]]))
