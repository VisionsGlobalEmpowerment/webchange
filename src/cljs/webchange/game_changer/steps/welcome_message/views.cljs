(ns webchange.game-changer.steps.welcome-message.views
  (:require
   [cljs-react-material-ui.reagent :as ui]))

(defn- get-styles
  []
  (let [margin "12px"]
    {:headers-wrapper {:position        "relative"
                       :top             "30%"
                       :margin          "0 auto"
                       :text-align      "center"}
     :sub-header      {:margin          margin}
     :button          {:margin          margin}}))

(defn welcome-form
  [{:keys [actions]}]
  (let [styles (get-styles)]
    [:div {:style (:headers-wrapper styles)}
     [ui/typography {:variant "h4"}
      "Let's build learning activities!"]
     [ui/typography {:variant "body1"
                     :style   (:sub-header styles)}
      "You can choose from a variety of templates"]
     (for [{:keys [id text handler props] :or {props {}}} actions]
       ^{:key id}
       [ui/button
        (merge {:on-click handler
                :size     "large"
                :color    "primary"
                :style    (:button styles)})
        "Sounds good, I'm ready!"])]))