(ns webchange.ui.components.button.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.components.icon.views :refer [general-icon]]
    [webchange.ui.components.progress.views :refer [circular-progress]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn button
  [{:keys [class-name chip chip-color color disabled? href icon icon-side loading? on-click shape state target text-align title variant]
    :or   {color      "yellow-1"
           shape      "rectangle"
           disabled?  false
           icon-side  "right"
           loading?   false
           on-click   #()
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
  (let [children (->> (r/current-component)
                      (r/children))
        icon-button? (empty? children)
        handle-click (fn []
                       (if (some? href)
                         (js/window.open href target)
                         (on-click)))]
    (into [:button (cond-> {:class-name (get-class-name {"bbs--button"                     true
                                                         "bbs--button--icon-button"        icon-button?
                                                         "bbs--button--with-loading"       loading?
                                                         (str "bbs--button--color-" color) (some? color)
                                                         (str "bbs--button--shape-" shape) (some? shape)
                                                         (str "bbs--button--state-" state) (some? state)
                                                         class-name                        (some? class-name)})
                            :disabled   disabled?
                            :on-click   handle-click}
                           (some? title) (assoc :title title))
           (when (and (some? icon)
                      (not loading?))
             [general-icon {:icon       icon
                            :class-name "bbs--button--icon"}])
           (when (some? chip)
             [general-icon {:icon       chip
                            :class-name (get-class-name {"bbs--button--chip"                          true
                                                         (str "bbs--button--chip--color-" chip-color) (some? chip-color)})}])]
          (if loading?
            [[circular-progress]]
            children))))
