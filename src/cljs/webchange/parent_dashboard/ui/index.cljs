(ns webchange.parent-dashboard.ui.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as ui]))

(defn- wrap-component
  [component class-name]
  (let [prefixed-class-name (str "pd-" class-name)]
    (fn [{:keys [class-name] :as props}]
      (into [component (assoc props :class-name (str class-name " " prefixed-class-name))]
            (-> (r/current-component) (r/children))))))

(def button (wrap-component ui/button "button"))
(def circular-progress (wrap-component ui/circular-progress "circular-progress"))
(def date (wrap-component ui/date "date"))
(def icon-button (wrap-component ui/icon-button "icon-button"))
(def input (wrap-component ui/input "input"))
(def select (wrap-component ui/select "select"))
(def dialog (wrap-component ui/dialog "dialog"))
