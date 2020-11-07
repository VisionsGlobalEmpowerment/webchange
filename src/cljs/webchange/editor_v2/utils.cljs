(ns webchange.editor-v2.utils
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [clojure.string :as s]))

(defn str->caption
  [str]
  (if-not (nil? str)
    (-> str
        name
        (->Camel_Snake_Case)
        (s/replace "_" " "))
    str))

(defn keyword->caption
  [key-word]
  (if-not (nil? key-word)
    (-> key-word clojure.core/name str->caption)
    key-word))
