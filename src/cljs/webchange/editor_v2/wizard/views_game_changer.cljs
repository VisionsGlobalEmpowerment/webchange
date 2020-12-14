(ns webchange.editor-v2.wizard.views-game-changer
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.views-game-changer-skills :refer [skills]]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]))

(defn- simple-activity-info
  []
  (r/with-let [default-skill 4
               default-template 5
               data (r/atom {:lang "english" :skills [default-skill] :name "Activity" :template-id default-template})]
    (let [templates (->> @(re-frame/subscribe [::state-activity/templates])
                         (filter #(not (some? (:lesson-sets %)))))
          current-template (->> templates (filter #(= (:template-id @data) (:id %))) first)]
      [ui/grid {:container   true
                :justify     "center"
                :spacing     24
                :align-items "center"}
       [ui/grid {:item true :xs 8}
        [ui/card {:style {:margin      "12px"
                          :flex-shrink "0"}}
         [ui/card-content
          [ui/grid {:container   true
                    :justify     "center"
                    :spacing     24
                    :align-items "center"}
           [ui/grid {:item true :xs 12}
            [ui/typography {:variant "h4"} "Game Changer"]]
           [ui/grid {:item true :xs 12}
            [ui/text-field {:label         "Name"
                            :full-width    true
                            :variant       "outlined"
                            :default-value (:name @data)
                            :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]
           [ui/grid {:item true :xs 12}
            [skills data]]
           [ui/grid {:item true :xs 12}
            [ui/form-control {:style {:margin-top "-8px"}}
             [ui/input-label "Template"]
             [ui/select {:value     (or (:template-id @data) "")
                         :on-change #(swap! data assoc :template-id (-> % .-target .-value))
                         :variant   "outlined"
                         :style     {:min-width "150px"}}
              (for [template templates]
                ^{:key (:id template)}
                [ui/menu-item {:value (:id template)} (:name template)])]]]
           [ui/grid {:item true :xs 12}
            [template {:template current-template
                       :data     data}]]]]
         [ui/card-actions
          [ui/button {:color    "secondary"
                      :style    {:margin-left "auto"}
                      :on-click #(re-frame/dispatch [::state-activity/create-simple-activity @data])}
           "Save"]]]]])))

(defn game-changer-panel
  []
  (re-frame/dispatch [::state-activity/load-templates])
  (re-frame/dispatch [::state-activity/load-skills])
  [layout {:title "Create Activity"}
   [simple-activity-info]])
