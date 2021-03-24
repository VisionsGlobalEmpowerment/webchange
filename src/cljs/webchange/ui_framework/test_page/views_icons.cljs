(ns webchange.ui-framework.test-page.views-icons
  (:require
    [webchange.ui-framework.index :refer [icon]]
    [webchange.ui-framework.components.icon.index :refer [icons]]
    [webchange.ui-framework.test-page.utils :refer [group group-body group-header]]))

(defn icons-group
  []
  [group
   [group-header "Icons"]
   [group-body
    [:div.icons-list
     (for [icon-name (-> icons keys sort)]
       ^{:key icon-name}
       [:div.icons-list-item
        [:span icon-name]
        [:div.icon-wrapper [icon {:icon icon-name}]]])]]])
