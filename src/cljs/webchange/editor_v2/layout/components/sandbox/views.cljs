(ns webchange.editor-v2.layout.components.sandbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.state :as state]
    [webchange.editor-v2.layout.components.sandbox.state-modal :as state-modal]
    [webchange.editor-v2.layout.components.sandbox.views-modal :refer [share-modal]]
    [webchange.ui-framework.components.index :refer [button with-custom-window]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]))

(defn share-button
  []
  (let [link @(re-frame/subscribe [::state/link])
        has-params? @(re-frame/subscribe [::state/form-has-params?])
        handle-click (fn [] (re-frame/dispatch [::state-modal/open]))
        has-changes? @(re-frame/subscribe [::translator-form/has-changes])
        open-link #(js/window.open link "_blank")
        handle-save (fn []
                      (re-frame/dispatch [::translator-form/save-changes])
                      (open-link))]
    [:div
     (if has-params?
       [button {:on-click handle-click}
        "Preview"]
       [button {:class-name "open-link"
                :on-click #(if has-changes?
                             (with-custom-window {:title   "Confirm"
                                                  :message ["There are some unsaved changes." "Do you want to save them?"]
                                                  :actions [{:text    "Save"
                                                             :handler handle-save
                                                             :props   {:color "primary"}}
                                                            {:text    "Don't save"
                                                             :handler open-link
                                                             :props   {:variant "outlined"}}]})
                             (open-link))}
        "Preview"])
     [share-modal]]))
