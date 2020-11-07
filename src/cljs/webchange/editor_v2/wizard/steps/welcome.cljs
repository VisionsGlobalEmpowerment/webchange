(ns webchange.editor-v2.wizard.steps.welcome
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn- get-styles
  []
  (let [margin "12px"]
    {:content         {:display         "flex"
                       :flex-direction  "column"
                       :justify-content "space-between"
                       :align-items     "center"}
     :headers-wrapper {:text-align "center"}
     :sub-header      {:margin margin}
     :button          {:margin margin}}))

(defn welcome-form
  [{:keys [steps on-start-click]}]
  (let [styles (get-styles)]
    [ui/grid {:container true
              :spacing   32}
     [ui/grid {:item  true :xs 8
               :style (:content styles)}
      [:div {:style (:headers-wrapper styles)}
       [ui/typography {:variant "h4"}
        "Let's build learning activities!"]
       [ui/typography {:variant "body1"
                       :style   (:sub-header styles)}
        "You can choose from a variety of templates"]]
      [ui/button {:color    "primary"
                  :on-click on-start-click
                  :style    (:button styles)}
       "Sounds good, I'm ready!"]]
     [ui/grid {:item true :xs 4}
      [ui/stepper {:active-step -1
                   :orientation "vertical"}
       (->> steps
            (map (fn [step]
                   ^{:key step}
                   [ui/step [ui/step-label step]])))]]]))

(def data
  {:component  welcome-form})
