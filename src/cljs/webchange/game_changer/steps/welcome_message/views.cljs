(ns webchange.game-changer.steps.welcome-message.views
  (:require
   [cljs-react-material-ui.reagent :as ui]))

(defn- get-styles
  []
  (let [margin "12px"]
    {:sub-header      {:margin margin}}))

(defn welcome-form
  []
  (let [styles (get-styles)]
    [:div
     [ui/typography {:variant "h4"}
      "Let's build learning activities!"]
     [ui/typography {:variant "body1"
                     :style   (:sub-header styles)}
      "You can choose from a variety of templates"]]))