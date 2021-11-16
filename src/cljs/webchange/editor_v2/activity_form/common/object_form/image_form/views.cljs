(ns webchange.editor-v2.activity-form.common.object-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.components.scale.views :refer [scale-component]]
    [webchange.editor-v2.activity-form.common.object-form.image-form.state :as state]
    [webchange.common.image-selector.views-modal :refer [with-image-modal]]
    [webchange.ui-framework.components.index :refer [button checkbox file icon-button label]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- preview
  [{:keys [id]}]
  (let [image-src @(re-frame/subscribe [::state/image-src id])]
    [:div.preview
     (when (some? image-src)
       [:img {:src image-src}])]))

(defn- set-image-src
  [id src]
  (re-frame/dispatch [::state/set-image-src id src]))

(defn- select-image-control
  [{:keys [id]}]
  (let [image-tags @(re-frame/subscribe [::state/image-tags id])
        show-select-image? @(re-frame/subscribe [::state/show-select-image-control? id])]
    (when show-select-image?
      [:div.control-block
       [label "Pick image from library:"]
       [with-image-modal {:tags      image-tags
                          :on-change #(set-image-src id %)}
        [button {:variant "contained"
                 :color   "primary"}
         "Select Image"]]])))

(defn- upload-image-control
  [{:keys [id]}]
  (let [show-upload-image? @(re-frame/subscribe [::state/show-upload-image-control? id])
        upload-options @(re-frame/subscribe [::state/upload-options id])
        handle-upload-start #(re-frame/dispatch [::state/upload-start id])
        handle-upload-finish (fn [src]
                               (set-image-src id src)
                               (re-frame/dispatch [::state/upload-finish id]))]
    (when show-upload-image?
      [:div.control-block
       [label "Or upload your own image:"]
       [file {:type            "image"
              :show-icon?      false
              :on-change       handle-upload-finish
              :on-upload-start handle-upload-start
              :upload-options  upload-options}]])))

(defn- image-size
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-image-fit id])
        handle-click #(re-frame/dispatch [::state/set-image-fit id %])]
    [:div.control-block.image-size-block
     [label "Image fit:"]
     [icon-button {:icon     "image-cover"
                   :title    "Cover"
                   :color    (if (= value "cover") "primary" "default")
                   :on-click #(handle-click "cover")}]
     [icon-button {:icon     "image-contain"
                   :title    "Contain"
                   :color    (if (= value "contain") "primary" "default")
                   :on-click #(handle-click "contain")}]
     [icon-button {:icon     "image-no-size"
                   :title    "No fit"
                   :color    (if (nil? value) "primary" "default")
                   :on-click #(handle-click nil)}]]))

(defn- scale
  [{:keys [id]}]
  [scale-component {:id         id
                    :class-name "control-block"}])

(defn- apply-to-all
  [{:keys [id]}]
  (let [show-apply-to-all? @(re-frame/subscribe [::state/show-apply-to-all-control? id])
        tags @(re-frame/subscribe [::state/edit-tags id])]
    (when show-apply-to-all?
      [:div.control-block
       [button {:variant  "contained"
                :color    "primary"
                :on-click #(re-frame/dispatch [::state/apply-to-all id tags])}
        "Apply to all"]])))


(defn- visible
  [{:keys [id]}]
  (let [show-visible? @(re-frame/subscribe [::state/show-visible-control? id])
        checked? @(re-frame/subscribe [::state/current-visible id])
        handle-change (fn [{:keys [checked?]}]
                        (re-frame/dispatch [::state/set-visible id checked?]))]
    (when show-visible?
      [:div.control-block
       [label "Visible:"]
       [checkbox {:checked?  checked?
                  :on-change handle-change}]])))

(defn form
  [{:keys [class-name id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div {:class-name (get-class-name {"image-form" true
                                        class-name   (some? class-name)})}
     [preview {:id id}]
     [:div.controls
      [select-image-control {:id id}]
      [upload-image-control {:id id}]
      [image-size {:id id}]
      [scale {:id id}]
      [apply-to-all {:id id}]
      [visible {:id id}]]]))
