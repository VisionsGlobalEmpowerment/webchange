(ns webchange.editor-v2.components.audio-wave-form.views-controls
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn- get-styles
  []
  {:container {:position "relative"
               :z-index  10}
   :button    {:position "absolute"
               :bottom   -8
               :right    0}})

(defn float-control
  [ws region]
  (r/with-let [state (r/atom "pause")]
              (let [_ (when-not (nil? @ws) (.on @ws "pause" #(reset! state "pause")))
                    styles (get-styles)
                    is-paused? (= @state "pause")
                    is-playing? (= @state "play")
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
                [:div {:style (:container styles)}
                 (when is-paused?
                   [fab {:color    "default"
                         :size     "small"
                         :style    (:button styles)
                         :on-click handle-play}
                    [ic/play-arrow]])
                 (when is-playing?
                   [fab {:color    "default"
                         :size     "small"
                         :style    (:button styles)
                         :on-click handle-pause}
                    [ic/pause]])])))
