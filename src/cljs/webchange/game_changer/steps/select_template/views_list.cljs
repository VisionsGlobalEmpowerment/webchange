(ns webchange.game-changer.steps.select_template.views-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select_template.state :as state]
    [webchange.game-changer.steps.select_template.views-list-item :refer [template-list-item]]))

(defn templates-list
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])
               handle-click (fn [template]
                              (->> (select-keys template [:id :name :options])
                                   (swap! data assoc :template)))]
    (let [templates @(re-frame/subscribe [::state/templates-list])
          current-template-id (get-in @data [:template :id])]
      [:div.templates-list
       (for [{:keys [id] :as template} templates]
         ^{:key id}
         [template-list-item {:template  template
                              :selected? (= id current-template-id)
                              :on-click  handle-click}])])))
