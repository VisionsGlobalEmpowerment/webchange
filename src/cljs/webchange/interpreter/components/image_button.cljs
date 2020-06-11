(ns webchange.interpreter.components.image-button
  (:require
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]))

(def skins {:close    "/raw/img/ui/close_button_01.png"
            :next     "/raw/img/ui/next_button_01.png"
            :reload   "/raw/img/ui/reload_button_01.png"
            :settings "/raw/img/ui/settings_button_01.png"})

(defn- get-skin
  [skin]
  (->> skin
       (keyword)
       (get skins)
       (get-data-as-url)))

(defn image-button
  [{:keys [x y skin on-click]}]
  [kimage
   {:src (get-skin skin)
    :x        x
    :y        y
    :on-click on-click
    :on-tap   on-click}])
