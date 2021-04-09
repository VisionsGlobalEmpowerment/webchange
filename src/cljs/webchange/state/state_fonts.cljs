(ns webchange.state.state-fonts
  (:require
    [re-frame.core :as re-frame]))

(def font-families ["AbrilFatface"
                    "Lexend Deca"
                    "Pacifico"
                    "Roboto"
                    "Staatliches"])

(re-frame/reg-sub
  ::font-family-options
  (fn [_]
    (->> font-families
         (map (fn [font-family]
                {:text  font-family
                 :value font-family})))))
