(ns webchange.lesson-builder.tools.object-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.image-form.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn fields
  [target]
  (re-frame/dispatch [::state/init target])
  (fn [target]
    (let [src-value @(re-frame/subscribe [::state/src-value target])
          scale-value @(re-frame/subscribe [::state/scale-value target])
          image-size-value @(re-frame/subscribe [::state/image-size-value target])
          visible-value @(re-frame/subscribe [::state/visible-value target])
          options (or @(re-frame/subscribe [::state/options target])
                      {:scale true
                       :flip true
                       :image-size true})]
      [:div.image-form-fields
       [:div.image-preview
        [ui/image {:src src-value}]]
       [:div.edit-image-fields
        [:div.edit-image-fields-header
         "Edit Image"]
        (when (:flip options)
          [:div.flip-field
           [ui/input-label
            "Flip"]
           [ui/button {:icon "flip"
                       :color "blue-1"
                       :on-click #(re-frame/dispatch [::state/flip target])}]])
        (when (:scale options)
          [:div.scale-field
           [ui/input-label
            "Scale"]
           [ui/input {:value scale-value
                      :type "float"
                      :step 0.05
                      :on-change #(re-frame/dispatch [::state/set-scale target %])}]])
        (when (:image-size options)
          [:div.image-fill-field
           [ui/input-label
            "Image Fill"]
           [:div.image-fill-filed-controls
            [ui/button {:title "Cover"
                        :icon "arrow-left"
                        :color (if (= image-size-value "cover") "blue-1" "grey-3")
                        :on-click #(re-frame/dispatch [::state/set-image-size target "cover"])}]
            [ui/button {:title "Contain"
                        :icon "arrow-right"
                        :color (if (= image-size-value "contain") "blue-1" "grey-3")
                        :on-click #(re-frame/dispatch [::state/set-image-size target "contain"])}]]])
        (when (:visible options)
          [:div.visible-field
           [ui/input-label
            "Visible"]
           [ui/switch {:checked? visible-value
                       :on-change #(re-frame/dispatch [::state/set-visible target %])}]])]
       (when (:apply-to-all options)
         [:div.apply-to-all-field
          [ui/button {:on-click #(re-frame/dispatch [::state/apply-to-all target])}
           "Apply to all"]])
       [:div.change-image-field
        [:div.change-image-field-header
         "Change image"]
        [select-image {:hide-preview? true
                       :on-change #(re-frame/dispatch [::state/set-image-src target (:url %)])}]]])))
