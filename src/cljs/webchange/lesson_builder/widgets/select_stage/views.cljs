(ns webchange.lesson-builder.widgets.select-stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.select-stage.state :as state]
    [webchange.ui.index :as ui]))

(defn select-stage
  [{:keys [class-name]}]
  (let [has-prev-stage? @(re-frame/subscribe [::state/has-prev-stage?])
        open-next-stage #(re-frame/dispatch [::state/open-next-stage])
        open-prev-stage #(re-frame/dispatch [::state/open-prev-stage])]
    [:div {:class-name (ui/get-class-name {"widget--select-page" true
                                           class-name            (some? class-name)})}
     [ui/button {:icon      "arrow-left"
                 :color     "blue-1"
                 :disabled? (not has-prev-stage?)
                 :on-click  open-prev-stage}]
     [ui/button {:icon     "arrow-right"
                 :color    "blue-1"
                 :on-click open-next-stage}]]))



