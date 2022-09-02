(ns webchange.ui.components.button.views
  (:require
    [clojure.spec.alpha :as s]
    [reagent.core :as r]
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.components.icon.views :refer [general-icon]]
    [webchange.ui.components.progress.views :refer [circular-progress]]
    [webchange.ui.spec :as ui-spec]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(s/def ::button-icon (s/or :empty nil? :defined ::ui-spec/general-icon))

(defn button
  [{:keys [class-name
           chip
           chip-color
           color
           disabled?
           href
           icon
           icon-side
           loading?
           on-click
           shape
           state
           target
           text
           text-align
           title
           variant]
    :or   {color      "yellow-1"
           shape      "rectangle"
           disabled?  false
           icon-side  "right"
           loading?   false
           target     "_blank"
           text-align "center"}}]
  {:pre [(or (nil? chip) (some #{chip} available-values/icon-system))
         (or (nil? chip-color) (some #{chip-color} available-values/color))
         (some #{color} available-values/color)
         (s/valid? ::button-icon icon)
         (some #{icon-side} ["left" "right"])
         (some #{shape} ["rectangle" "rounded"])
         (some #{text-align} ["left" "center" "right"])]}
  (let [color (if disabled? "grey-4" color)
        children (->> (r/current-component)
                      (r/children))
        icon-button? (and (nil? text) (empty? children))
        show-icon? (and (some? icon)
                        (not loading?))
        shape (if icon-button? "rounded" shape)
        handle-click (fn [event]
                       (if (some? href)
                         (js/window.open href target)
                         (when (fn? on-click)
                           (on-click event))))]
    (into [:button (cond-> {:class-name (get-class-name {"bbs--button"                               true
                                                         "bbs--button--icon-button"                  icon-button?
                                                         "bbs--button--text-icon-button"             (and show-icon? (not icon-button?))
                                                         "bbs--button--with-loading"                 loading?
                                                         "bbs--button--clickable"                    (some? on-click)
                                                         (str "bbs--button--color-" color)           (some? color)
                                                         (str "bbs--button--icon-side-" icon-side)   (some? icon-side)
                                                         (str "bbs--button--shape-" shape)           (some? shape)
                                                         (str "bbs--button--state-" state)           (some? state)
                                                         (str "bbs--button--text-align-" text-align) (some? text-align)
                                                         (str "bbs--button--variant-" variant)       (some? variant)
                                                         class-name                                  (some? class-name)})
                            :disabled   (or disabled? loading?)
                            :on-click   handle-click}
                           (some? title) (assoc :title title))
           (when show-icon?
             [general-icon {:icon       icon
                            :class-name "bbs--button--icon"}])
           (when (some? chip)
             [general-icon {:icon       chip
                            :class-name (get-class-name {"bbs--button--chip"                          true
                                                         (str "bbs--button--chip--color-" chip-color) (some? chip-color)})}])]
          (if loading?
            [[circular-progress]]
            [(into [:div.bbs--button--text] (or text children))]))))
