(ns webchange.state.state-fonts
  (:require
    [re-frame.core :as re-frame]))

(def font-families ["AbrilFatface"
                    "Lexend Deca"
                    "Pacifico"
                    "Roboto"
                    "Staatliches"])

(def font-sizes [8 9 10 11 12 14 18 24 30 36 48 60 72 96])

(re-frame/reg-sub
  ::font-family-options
  (fn [_]
    (->> font-families
         (map (fn [font-family]
                {:text  font-family
                 :value font-family})))))

(re-frame/reg-sub
  ::font-size-options
  (fn [_]
    (->> font-sizes
         (map (fn [font-size]
                {:text  font-size
                 :value font-size})))))
