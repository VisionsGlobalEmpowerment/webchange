(ns webchange.ui.components.message
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  [type]
  (let [color (case type
                "warn" (get-in-theme [:palette :warning :default])
                (get-in-theme [:palette :border :default]))]
    {:container {:padding      "8px"
                 :border       "solid 1px"
                 :border-color color
                 :display      "flex"}
     :icon      {:color        color
                 :margin-right "8px"}
     :message   {:display  "inline-block"
                 :position "relative"
                 :top      "2px"}}))

(defn message
  [{:keys [type message]}]
  (let [styles (get-styles type)]
    [ui/paper {:style (:container styles)}
     [ic/report {:style (:icon styles)}]
     [ui/typography {:variant "body1"
                     :style   (:message styles)}
      message]]))
