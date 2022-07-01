(ns webchange.ui.components.complete-progress.views
  (:require
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(def value-thresholds {:low 30
                       :mid 60})

(defn complete-progress
  [{:keys [caption class-name text title value]}]
  (let [value (-> value (max 0) (min 100))
        completed? (= value 100)
        started? (not= value 0)
        value-group (cond
                      completed? "complete"
                      (not started?) "empty"
                      (> value (:mid value-thresholds)) "high"
                      (> value (:low value-thresholds)) "mid"
                      :default "low")]
    [:div (cond-> {:class-name (get-class-name {"bbs--complete-progress"                           true
                                                (str "bbs--complete-progress--value-" value-group) true
                                                class-name                                         (some? class-name)})}
                  (some? title) (assoc :title title))
     [:div {:class-name (get-class-name {"bbs--complete-progress--value" true})
            :style      {:width (str value "%")}}]
     [:div {:class-name "bbs--complete-progress--data"}
      (when completed?
        [:div.bbs--complete-progress--text-block
         (when (some? caption)
           [:div.bbs--complete-progress--caption caption])
         (when (some? text)
           [:div.bbs--complete-progress--text text])])
      (when (and started? (not completed?))
        [:div {:class-name "bbs--complete-progress--progress"}
         value])
      (when-not started?
        [:div {:class-name "bbs--complete-progress--not-started"}
         "Not Started"])
      (when completed?
        [system-icon {:icon       "check"
                      :class-name "bbs--complete-progress--complete-icon"}])]]))
