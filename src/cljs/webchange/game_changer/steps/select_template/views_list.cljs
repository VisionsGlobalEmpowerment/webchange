(ns webchange.game-changer.steps.select_template.views-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select_template.state :as state]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.game-changer.steps.select_template.views-list-item :refer [template-list-item]]))

(defn templates-list
  [{:keys [data actions]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])
               handle-click (fn [template]
                              (->> (select-keys template [:id :name :options])
                                   (swap! data assoc :template)))]
    (let [templates @(re-frame/subscribe [::state/templates-list])
          tutorial-activity @(re-frame/subscribe [::state/tutorial-activity])
          filtered-templates @(re-frame/subscribe [::state/templates-list-without-tutorial-activity])
          current-template-id (get-in @data [:template :id])]
      [:div.templates-list
       [ui/typography {:variant "h5"}
        "Start with this..."]
       [template-list-item {:template  tutorial-activity
                            :selected? (= (:id tutorial-activity) current-template-id)
                            :on-click  handle-click
                            :actions   actions}]
       [ui/typography {:variant "h5"}
        "Choose from other activities..."]
       (for [{:keys [id] :as template} filtered-templates]
         ^{:key id}
         [template-list-item {:template  template
                              :selected? (= id current-template-id)
                              :on-click  handle-click
                              :actions   actions}])])))
