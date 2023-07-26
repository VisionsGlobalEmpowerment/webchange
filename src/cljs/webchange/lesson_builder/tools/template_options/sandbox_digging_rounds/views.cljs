(ns webchange.lesson-builder.tools.template-options.sandbox-digging-rounds.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- image-key
  [round-idx image-idx]
  (-> (str "image" round-idx "-" image-idx)))

(defn- round-panel
  [round-idx]
  (let [image1-value @(re-frame/subscribe [::state/field-value (image-key round-idx 1)])
        image2-value @(re-frame/subscribe [::state/field-value (image-key round-idx 2)])
        image3-value @(re-frame/subscribe [::state/field-value (image-key round-idx 3)])]
    [:div.round
     [:h3.round-header
      [:div.round-header-label
       [ui/icon {:icon "caret-down"
                 :color "grey-3"}]
       (str "Round " round-idx)]]
     [:<>
      [select-image {:label "Object 1"
                     :value (:src image1-value)
                     :on-change #(re-frame/dispatch [::state/set-field (image-key round-idx 1) {:src (:url %)}])}]
      [select-image {:label "Object 2"
                     :value (:src image2-value)
                     :on-change #(re-frame/dispatch [::state/set-field (image-key round-idx 2) {:src (:url %)}])}]
      [select-image {:label "Object 3"
                     :value (:src image3-value)
                     :on-change #(re-frame/dispatch [::state/set-field (image-key round-idx 3) {:src (:url %)}])}]]]))

(defn field
  [_props]
  (fn [_props]
    [:div.sandbox-digging-rounds
     [:h3.sandbox-digging-rounds-header "Rounds"]
     (for [round-idx [1 2]]
       ^{:key round-idx}
       [round-panel round-idx])]))
