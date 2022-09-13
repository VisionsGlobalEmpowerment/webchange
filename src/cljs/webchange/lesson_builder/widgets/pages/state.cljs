(ns webchange.lesson-builder.widgets.pages.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state-flipbook :as state]))

(defn set-last
  [coll x]
  (-> (pop coll)
      (conj x)))

(re-frame/reg-sub
  ::stages
  :<- [::state/activity-stages-filtered]
  (fn [activity-stages]
    (let [last-stage (last activity-stages)
          add-page-data {:title "Add"
                         :id    :add}]
      (if (-> last-stage :right-page nil?)
        (->> (assoc last-stage :right-page add-page-data)
             (set-last activity-stages))
        (conj activity-stages {:id        :new-stage
                               :left-page add-page-data})))))

(re-frame/reg-event-fx
  ::add-page
  (fn [{:keys [_]} [_]]
    (print "::add-page")
    {}))
