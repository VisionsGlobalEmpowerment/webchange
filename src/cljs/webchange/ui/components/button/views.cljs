(ns webchange.ui.components.button.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.components.progress.views :refer [circular-progress]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn button
  [{:keys [class-name chip chip-color color disabled? href icon icon-side loading? on-click shape size state target text-align title variant]
    :or   {color      "yellow-1"
           shape      "rectangle"
           disabled?  false
           icon-side  "right"
           loading?   false
           on-click   #()
           size       "medium"
           target     "_blank"
           text-align "center"
           variant    "contained"}}]
  {:pre [(or (nil? chip) (some #{chip} available-values/icon-system))
         (or (nil? chip-color) (some #{chip-color} available-values/color))
         (some #{color} available-values/color)
         (or (nil? icon) (some #{icon} available-values/icon-system))
         (some #{icon-side} ["left" "right"])
         (some #{shape} ["rectangle" "rounded"])
         (some #{text-align} ["left" "center" "right"])]}
  (let [this (r/current-component)
        handle-click (fn []
                       (if (some? href)
                         (js/window.open href target)
                         (on-click)))]
    (into [:button (cond-> {:class-name (get-class-name {"bbs--button"                     true
                                                         "bbs--button--with-loading"       loading?
                                                         (str "bbs--button--color-" color) (some? color)
                                                         (str "bbs--button--shape-" shape) (some? shape)
                                                         (str "bbs--button--state-" state) (some? state)
                                                         class-name                        (some? class-name)})
                            :disabled   disabled?
                            :on-click   handle-click}
                           (some? title) (assoc :title title))]
          (if loading?
            [[circular-progress]]
            (r/children this)))))
