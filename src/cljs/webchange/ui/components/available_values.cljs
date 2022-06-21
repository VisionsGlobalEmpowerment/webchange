(ns webchange.ui.components.available-values
  (:require
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.components.icon.system.index :as system-icons]))

(def color ["transparent" "blue-1" "blue-2" "green-1" "green-2" "yellow-1" "yellow-2"
            "grey-0" "grey-1" "grey-2" "grey-3" "grey-4" "grey-5"])
(def icon-navigation (map first navigation-icons/data))
(def icon-system (map first system-icons/data))
