(ns webchange.dashboard.classes.views
  (:require
    [webchange.dashboard.classes.views-list-menu :as list-menu]
    [webchange.dashboard.classes.views-dashboard :as dashboard]))

(defn classes-list-menu
  [& args]
  (apply list-menu/classes-list-menu args))

(defn classes-dashboard
  [& args]
  (apply dashboard/classes-dashboard args))
