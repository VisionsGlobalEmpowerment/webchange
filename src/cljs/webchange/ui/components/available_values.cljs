(ns webchange.ui.components.available-values
  (:require
    [webchange.ui.components.icon.system.index :as system-icons]))

(def color ["blue-1" "blue-2" "green-1" "green-2" "yellow-1" "yellow-2"])
(def icon-system (map first system-icons/data))
