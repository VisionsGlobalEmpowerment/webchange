(ns webchange.editor-v2.activity-form.common.object-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.image-form.state :as state]
    [webchange.common.image-selector.views-modal :refer [with-image-modal]]
    [webchange.ui-framework.components.index :refer [button file label]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    (let [image-tag @(re-frame/subscribe [::state/image-tag id])
          handle-change (fn [src]
                          (re-frame/dispatch [::state/set-image-src id src]))]
      [:div
       [:div.control-block
        [label "Pick image from library:"]
        [with-image-modal {:tag       image-tag
                           :on-change handle-change}
         [button {:variant "contained"
                  :color   "primary"}
          "Select Image"]]]
       [:div.control-block
        [label "Or upload your own image:"]
        [file {:type       "image"
               :show-icon? false
               :on-change  handle-change}]]])))
