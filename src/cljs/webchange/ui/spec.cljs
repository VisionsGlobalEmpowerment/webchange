(ns webchange.ui.spec
  (:require
    [clojure.spec.alpha :as s]))

(def brand-colors ["blue-1" "blue-2" "green-1" "green-2" "yellow-1" "yellow-2"])
(def grey-colors ["grey-0" "grey-1" "grey-2" "grey-3" "grey-4" "grey-5"])

(defn in-colors-collection?
  [color-name collection]
  (->> (some #{color-name} collection)
       (boolean)))

(defn brand-color?
  [color-name]
  (in-colors-collection? color-name brand-colors))

(s/def ::brand-color brand-color?)
