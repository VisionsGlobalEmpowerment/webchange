(ns webchange.parent-dashboard.ui.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as ui]))

(defn- wrap-component
  [component class-name]
  (let [prefixed-class-name (str "pd-" class-name)]
    (fn [props]
      (into [component (assoc props :class-name prefixed-class-name)]
            (-> (r/current-component) (r/children))))))

(def input (wrap-component ui/input "input"))
(def select (wrap-component ui/select "select"))
(def dialog (wrap-component ui/dialog "dialog"))
