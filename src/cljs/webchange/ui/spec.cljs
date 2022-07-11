(ns webchange.ui.spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.components.icon.social.index :as social-icons]
    [webchange.ui.components.icon.system.index :as system-icons]))

;; colors

(def brand-colors ["blue-1" "blue-2" "green-1" "green-2" "yellow-1" "yellow-2"])
(def grey-colors ["grey-0" "grey-1" "grey-2" "grey-3" "grey-4" "grey-5"])

(defn in-colors-collection?
  [color-name collection]
  (->> (some #{color-name} collection)
       (boolean)))

(defn brand-color?
  [color-name]
  (in-colors-collection? color-name brand-colors))

;; icons

(defn in-icons-collection?
  [icon-name icons]
  (->> (map first icons)
       (some #{icon-name})
       (boolean)))

(defn navigation-icon?
  [icon-name]
  (in-icons-collection? icon-name navigation-icons/data))

(defn social-icon?
  [icon-name]
  (in-icons-collection? icon-name social-icons/data))

(defn system-icon?
  [icon-name]
  (in-icons-collection? icon-name system-icons/data))

;; --- Specs ---

;; colors
(s/def ::brand-color brand-color?)
;; icons

(s/def ::general-icon (s/or :navigation navigation-icon?
                            :social social-icon?
                            :system system-icon?))
(s/def ::navigation-icon navigation-icon?)
(s/def ::social-icon social-icon?)
(s/def ::system-icon system-icon?)
