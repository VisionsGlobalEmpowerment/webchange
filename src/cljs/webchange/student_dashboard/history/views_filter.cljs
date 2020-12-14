(ns webchange.student-dashboard.history.views-filter
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn- get-property-values
  [list property]
  (->> list
       (map #(get % property))
       (distinct)
       (sort)))

(defn- property-select
  [{:keys [values filter key title]}]
  (let [default-value "any"
        handle-change (fn [event]
                        (let [value (-> event .-target .-value)]
                          (if (= value default-value)
                            (swap! filter dissoc key)
                            (swap! filter assoc key value))))]
    [ui/form-control {:full-width true
                      :style      {:margin-top "-8px"}}
     [ui/input-label title]
     [ui/select {:value     (get @filter key "")
                 :variant   "outlined"
                 :on-change handle-change}
      (for [value (conj (vec values) default-value)]
        ^{:key value}
        [ui/menu-item {:value value} value])]]))

(defn history-filter
  [{:keys [data filter]}]
  (let [levels (get-property-values data :level)
        lessons (get-property-values data :lesson)
        activities (get-property-values data :activity)]
    [ui/grid {:container   true
              :spacing     24
              :justify     "flex-start"
              :align-items "flex-end"
              :style       {:margin-bottom "16px"}}
     [ui/grid {:item true :xs 2}
      [property-select {:values levels
                        :title  "Level"
                        :filter filter
                        :key    :level}]]
     [ui/grid {:item true :xs 2}
      [property-select {:values lessons
                        :title  "Lesson"
                        :filter filter
                        :key    :lesson}]]
     [ui/grid {:item true :xs 2}
      [property-select {:values activities
                        :title  "Activity"
                        :filter filter
                        :key    :activity}]]
     [ui/grid {:item true :xs 2}
      [ui/button {:on-click #(reset! filter {})
                  :style    {:margin-bottom "2px"}}
       "Reset"]]]))
