(ns webchange.lesson-builder.tools.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.animation-form.state :as state]
    [webchange.lesson-builder.widgets.choose-character.views :refer [choose-character]]
    [webchange.ui.index :as ui]))

(defn fields
  [{:keys [target]}]
  (re-frame/dispatch [::state/init target])
  (fn [{:keys [class-name target]}]
    (let [scale-value @(re-frame/subscribe [::state/scale-value target])
          character-value @(re-frame/subscribe [::state/character-value target])]
      [:div {:class-name (ui/get-class-name {"animation-form-fields" true
                                             class-name              (some? class-name)})}
       [choose-character {:on-change     #(re-frame/dispatch [::state/set-character target %])
                          :default-value character-value}]
       [:div.additional-fields
        [:div.flip-field
         [ui/input-label
          "Flip"]
         [ui/button {:icon     "flip"
                     :color    "blue-1"
                     :on-click #(re-frame/dispatch [::state/flip target])}]]
        [:div.scale-field
         [ui/input-label
          "Scale"]
         [ui/input {:value     scale-value
                    :type      "float"
                    :step      0.05
                    :on-change #(re-frame/dispatch [::state/set-scale target %])}]]]])))