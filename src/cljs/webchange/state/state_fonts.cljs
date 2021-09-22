(ns webchange.state.state-fonts
  (:require
    [re-frame.core :as re-frame]))

(def font-families ["AbrilFatface"
                    "Lexend Deca"
                    "Pacifico"
                    "Roboto"
                    "Staatliches"])

(def font-colors [{:hexcode "#000000" :color "Black"}
                  {:hexcode "#FFFFFF" :color "White"}
                  {:hexcode "#FF0000" :color "Red"}
                  {:hexcode "#FFFF00" :color "Yellow"}
                  {:hexcode "#008000" :color "Green"}])

(def font-sizes [8 9 10 11 12 14 18 24 30 36 48 60 72 96])

(re-frame/reg-sub
  ::font-family-options
  (fn [_]
    (->> font-families
         (map (fn [font-family]
                {:text  font-family
                 :value font-family})))))

(re-frame/reg-sub
 ::font-color-options
 (fn [_]
   (->> font-colors
        (map (fn [font-color]
               {:text    (:color font-color)
                :hexcode (:color font-color)})))))

(re-frame/reg-sub
  ::font-size-options
  (fn [_]
    (->> font-sizes
         (map (fn [font-size]
                {:text  font-size
                 :value font-size})))))
