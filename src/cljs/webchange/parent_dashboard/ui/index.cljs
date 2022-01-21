(ns webchange.parent-dashboard.ui.index
  (:require
    [webchange.ui-framework.components.index :as ui]))

(defn- wrap-component
  [component class-name]
  (let [prefixed-class-name (str "pd-" class-name)]
    (fn [props]
      [component (assoc props :class-name prefixed-class-name)])))

(def input (wrap-component ui/input "input"))
(def select (wrap-component ui/select "select"))
