(ns webchange.book-creator.stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-action.state :as activity-action]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.state.state-flipbook :as flipbook-state]))

(re-frame/reg-sub
  ::page-removable?
  (fn [[_ page-side]]
    [(re-frame/subscribe [::flipbook-state/current-pages page-side])])
  (fn [[current-pages]]
    (:removable? current-pages)))

(re-frame/reg-event-fx
  ::remove-current-stage-page
  (fn [{:keys [db]} [_ page-side]]
    (let [current-stage (stage-state/current-stage db)]
      {:dispatch [::activity-action/call-activity-action {:action "remove-page"
                                                          :data   {:stage     current-stage
                                                                   :page-side page-side}}]})))
