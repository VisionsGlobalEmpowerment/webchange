(ns webchange.lesson-builder.tools.object-form.video-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.video-form.state :as state]
    [webchange.ui.index :as ui]))

(defn fields
  [{:keys [target]}]
  (re-frame/dispatch [::state/init target])
  (fn [{:keys [class-name target]}]
    (let [value @(re-frame/subscribe [::state/volume target])]
      [ui/input {:value     value
                 :on-change #(re-frame/dispatch [::state/set-volume target %])
                 :type      "range"
                 :step      0.05
                 :min       0
                 :max       1
                 :class-name (ui/get-class-name {class-name (some? class-name)})}])))
