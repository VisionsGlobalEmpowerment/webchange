(ns webchange.interpreter.utils.i18n
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]))

(def translations
  {"spanish" {"play" "juega"
              "next" "sigue"
              "watch" "mira"
              "skip" "omitir"}})
(defn t
  [text]
  (let [current-course @(re-frame/subscribe [::subs/current-course])
        translated (get-in translations [current-course (clojure.string/lower-case text)])]
    (or translated text)))
