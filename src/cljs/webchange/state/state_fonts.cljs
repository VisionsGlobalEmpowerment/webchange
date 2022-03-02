(ns webchange.state.state-fonts
  (:require
    [re-frame.core :as re-frame]))

(def font-families ["AbrilFatface"
                    "Lexend Deca"
                    "Pacifico"
                    "Roboto"
                    "Tabschool"])

(def font-colors [{:hex "#000000" :name "Black"}
                  {:hex "#FFFFFF" :name "White"}
                  {:hex "#FF0000" :name "Red"}
                  {:hex "#FFFF00" :name "Yellow"}
                  {:hex "#008000" :name "Green"}])

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
    (map (fn [{:keys [hex name]}]
           {:text  name
            :value hex})
         font-colors)))

(re-frame/reg-sub
  ::font-size-options
  (fn [_]
    (->> font-sizes
         (map (fn [font-size]
                {:text  font-size
                 :value font-size})))))
