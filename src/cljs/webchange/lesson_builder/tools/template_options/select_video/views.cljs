(ns webchange.lesson-builder.tools.template-options.select-video.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.select-video.state :as state]
    [webchange.ui.index :as ui]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [label] :as props}]
    (let [video-options @(re-frame/subscribe [::state/video-options])
          video-value @(re-frame/subscribe [::state/selected-video])
          select-video #(re-frame/dispatch [::state/select-video %])]
      [:div.select-book
       [ui/select {:label label
                   :value video-value
                   :required? true
                   :options video-options
                   :on-change select-video}]])))
