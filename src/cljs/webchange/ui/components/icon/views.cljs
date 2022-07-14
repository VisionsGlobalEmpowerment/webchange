(ns webchange.ui.components.icon.views
  (:require
    [webchange.ui.components.icon.flags.index :as flags-icons]
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.components.icon.social.index :as social-icons]
    [webchange.ui.components.icon.system.index :as system-icons]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- icon-component
  [{:keys [icon on-click rotate? title class-name draggable color type]}]
  (let [svg-data (-> (case type
                       "flag" flags-icons/data
                       "navigation" navigation-icons/data
                       "social" social-icons/data
                       "system" system-icons/data)
                     (get icon))]
    [:div (cond-> {:class-name (get-class-name {"bbs--icon"                     true
                                                (str "bbs--icon--" icon)        true
                                                (str "bbs--icon-type--" type)   true
                                                (str "bbs--icon--color-" color) (some? color)
                                                "bbs--icon--rotating"           rotate?
                                                class-name                      (some? class-name)})}
                  (fn? on-click) (assoc :on-click on-click)
                  (some? title) (assoc :title title)
                  (some? draggable) (assoc :draggable draggable))
     svg-data]))

(defn flag-icon
  [props]
  [icon-component (assoc props :type "flag")])

(defn navigation-icon
  [props]
  [icon-component (assoc props :type "navigation")])

(defn system-icon
  [props]
  [icon-component (assoc props :type "system")])

(defn social-icon
  [props]
  [icon-component (assoc props :type "social")])

(defn general-icon
  [{:keys [icon] :as props}]
  (let [type (cond
               (contains? system-icons/data icon) "system"
               (contains? navigation-icons/data icon) "navigation"
               (contains? social-icons/data icon) "social"
               (contains? system-icons/data icon) "system"
               (contains? flags-icons/data icon) "flag")]
    [icon-component (assoc props :type type)]))
