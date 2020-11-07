(ns webchange.editor-v2.diagram-utils.modes.translation.widget-loop-icon
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.utils-play-audio :refer [play-audios-list]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn- get-styles
  []
  {:loop-icon {:right    "46px"
               :bottom   "0"
               :position "absolute"
               :margin   "10px"
               :width    "36px"
               :height   "36px"}
   })

(defn loop-icon
  [node-data]
  (let [styles (get-styles)
        action-data (:data node-data)
        enabled (:loop action-data)]
    (if enabled
      [fab {:style    (:loop-icon styles)
            :size     "small"}
       [ic/refresh]])))