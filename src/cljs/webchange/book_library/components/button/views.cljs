(ns webchange.book-library.components.button.views
  (:require
    [webchange.book-library.layout.icons.index :refer [icons]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn button
  [{:keys [active? button-class-name icon on-click title]}]
  [:div {:class-name (get-class-name {"book-library-button" true
                                      "active"              active?})}
   [:button {:on-click   on-click
             :title      title
             :class-name (get-class-name {button-class-name (some? button-class-name)})}
    (get icons icon)]])
