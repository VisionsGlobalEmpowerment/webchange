(ns webchange.common.background-selector.views-background-selector
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.background-selector.state :as state]
    [webchange.common.background-selector.form.views-form :refer [background-form]]))

(defn- get-init-event
  [{:keys [scene-slug course-slug]}]
  (cond
    (and (some? scene-slug)
         (some? course-slug)) [::state/load-scene {:scene-slug scene-slug :course-slug course-slug}]))

(defn background-selector
  [{:keys [change-on-init? on-change] :as props}]
  (r/with-let [_ (re-frame/dispatch (get-init-event props))]
    (let [{:keys [name data]} @(re-frame/subscribe [::state/background-data])
          handle-change (fn [new-data]
                          (on-change {:name name
                                      :data new-data}))]
      (when (some? data)
        [background-form (merge data
                                {:on-change       handle-change
                                 :change-on-init? change-on-init?})]))))
