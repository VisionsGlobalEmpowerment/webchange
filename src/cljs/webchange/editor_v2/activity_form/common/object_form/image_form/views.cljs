(ns webchange.editor-v2.activity-form.common.object-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.image-form.state :as state]
    [webchange.common.image-selector.views-modal :refer [with-image-modal]]
    [webchange.ui-framework.components.index :refer [button file label]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn form
  [{:keys [class-name id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    (let [image-src @(re-frame/subscribe [::state/image-src id])
          image-tags @(re-frame/subscribe [::state/image-tags id])
          handle-change (fn [src]
                          (re-frame/dispatch [::state/set-image-src id src]))]
      [:div {:class-name (get-class-name (cond-> {"image-form" true}
                                                 (some? class-name) (assoc :class-name true)))}
       [:div.preview
        (when (some? image-src)
          [:img {:src image-src}])]
       [:div.controls
        [:div.control-block
         [label "Pick image from library:"]
         [with-image-modal {:tags      image-tags
                            :on-change handle-change}
          [button {:variant "contained"
                   :color   "primary"}
           "Select Image"]]]
        [:div.control-block
         [label "Or upload your own image:"]
         [file {:type       "image"
                :show-icon? false
                :on-change  handle-change}]]]])))
