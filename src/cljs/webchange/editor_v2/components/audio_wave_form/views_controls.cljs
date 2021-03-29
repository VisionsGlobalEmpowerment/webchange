(ns webchange.editor-v2.components.audio-wave-form.views-controls
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn float-control
  [ws region]
  (r/with-let [state (r/atom "pause")]
    (let [_ (when-not (nil? @ws) (.on @ws "pause" #(reset! state "pause")))
          handle-play (fn [event]
                        (.stopPropagation event)
                        (if (:region @region) (-> @region :region .play)
                                              (.play @ws)
                                              )
                        (reset! state "play"))
          handle-pause (fn [event]
                         (.stopPropagation event)
                         (.stop @ws)
                         (reset! state "pause"))]
      (if (= @state "pause")
        [icon-button {:icon     "play"
                      :color    "primary"
                      :on-click handle-play}]
        [icon-button {:icon       "stop"
                      :color      "secondary"
                      :class-name "stop-button"
                      :on-click   handle-pause}]))))
