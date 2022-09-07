(ns webchange.lesson-builder.widgets.select-stage.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state-flipbook :as state]
    [webchange.ui.index :as ui]))

(defn select-stage
  [{:keys [class-name]}]
  (let [has-next-stage? @(re-frame/subscribe [::state/has-next-stage?])
        has-prev-stage? @(re-frame/subscribe [::state/has-prev-stage?])
        open-next-stage #(re-frame/dispatch [::state/show-next-stage])
        open-prev-stage #(re-frame/dispatch [::state/show-prev-stage])]
    [:div {:class-name (ui/get-class-name {"widget--select-page" true
                                           class-name            (some? class-name)})}
     [ui/button {:icon      "arrow-left"
                 :color     "blue-1"
                 :disabled? (not has-prev-stage?)
                 :on-click  open-prev-stage}]
     [ui/button {:icon      "arrow-right"
                 :color     "blue-1"
                 :disabled? (not has-next-stage?)
                 :on-click  open-next-stage}]]))
