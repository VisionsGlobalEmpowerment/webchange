(ns webchange.ui-framework.test-page.views-icon-buttons
  (:require
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.icon.index :refer [icons]]
    [webchange.ui-framework.test-page.utils :refer [group group-body group-header group-sub-header]]))

(defn icon-buttons-group
  []
  [group
   [group-header "Icon Buttons"]
   [group-body
    [:div.icon-buttons-list
     (for [icon-name (-> icons keys sort)]
       ^{:key icon-name}
       [:div.icon-buttons-list-item
        [:span icon-name]
        [:div.icon-button-wrapper
         [icon-button {:icon  icon-name
                       :color "primary"}]]])]]

   [group-sub-header "Colors"]
   [group-body
    [:div.icon-buttons-list
     [:div.icon-buttons-list-item
      [:span "default"]
      [:div.icon-button-wrapper
       [icon-button {:icon  "play"
                     :color "default"}]]]
     [:div.icon-buttons-list-item
      [:span "primary"]
      [:div.icon-button-wrapper
       [icon-button {:icon  "play"
                     :color "primary"}]]]
     [:div.icon-buttons-list-item
      [:span "secondary"]
      [:div.icon-button-wrapper
       [icon-button {:icon  "play"
                     :color "secondary"}]]]]]

   [group-sub-header "Sizes"]
   [group-body
    [:div.icon-buttons-list
     [:div.icon-buttons-list-item
      [:span "default"]
      [:div.icon-button-wrapper
       [icon-button {:icon  "stop"
                     :color "primary"
                     :size  "default"}]]]

     [:div.icon-buttons-list-item
      [:span "big"]
      [:div.icon-button-wrapper
       [icon-button {:icon  "stop"
                     :color "primary"
                     :size  "big"}]]]]]])
