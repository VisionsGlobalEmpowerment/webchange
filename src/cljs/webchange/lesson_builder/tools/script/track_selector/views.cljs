(ns webchange.lesson-builder.tools.script.track-selector.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.block.views :refer [block]]
    [webchange.lesson-builder.tools.script.track-selector.state :as state]
    [webchange.ui.index :as ui]))

(defn track-selector
  []
  (let [current-track @(re-frame/subscribe [::state/current-track])
        track-options @(re-frame/subscribe [::state/available-tracks])
        handle-change #(re-frame/dispatch [::state/set-current-track %])]
    [block {:title      "Choose Sequence"
            :class-name "track-selector"}
     [ui/select {:value     current-track
                 :options   track-options
                 :on-change handle-change
                 :type      "int"}]]))
