(ns webchange.ui.components.icon.views
  (:require
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn navigation-icon
  [{:keys [icon on-click rotate? title class-name draggable color]}]
  [:div (cond-> {:class-name (get-class-name {"bbs--icon"                         true
                                              "bbs--navigation-icon"              true
                                              (str "bbs--navigation-icon--" icon) true
                                              "bbs--icon-rotating"                rotate?
                                              (str "bbs--icon--color-" color)     (some? color)
                                              class-name                          (some? class-name)})}
                (fn? on-click) (assoc :on-click on-click)
                (some? title) (assoc :title title)
                (some? draggable) (assoc :draggable draggable))
   (get navigation-icons/data icon)])
