(ns webchange.game-changer.steps.welcome-message.views
  (:require
   [cljs-react-material-ui.reagent :as ui]))

(defn welcome-form
  [{:keys [actions]}]
  [:div.headers-wrapper
   [ui/typography {:variant "h4"}
    "Let's build learning activities!"]
   [ui/typography {:variant   "body1"
                   :class-name "sub-header"}
    "You can choose from a variety of templates"]
   (for [{:keys [id text handler props] :or {props {}}} actions]
     ^{:key id}
     [ui/button
      (merge {:on-click handler
              :size       "large"
              :color      "primary"
              :class-name "primary-button"})
      "Sounds good, I'm ready!"])])
