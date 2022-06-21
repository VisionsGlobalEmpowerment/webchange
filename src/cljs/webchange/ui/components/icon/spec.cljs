(ns webchange.ui.components.icon.spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.components.icon.system.index :as system-icons]))

(defn in-icons-collection?
  [icon-name icons]
  (->> (map first icons)
       (some #{icon-name})
       (boolean)))

(defn navigation-icon?
  [icon-name]
  (in-icons-collection? icon-name navigation-icons/data))

(defn system-icon?
  [icon-name]
  (in-icons-collection? icon-name system-icons/data))

(s/def ::navigation-icon navigation-icon?)
(s/def ::system-icon system-icon?)
