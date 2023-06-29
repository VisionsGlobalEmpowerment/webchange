(ns webchange.ui.components.complete-progress.views
  (:require
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(def value-thresholds {:low 30
                       :mid 60})

(defn- not-started
  []
  [:div (cond-> {:class-name (get-class-name {"bbs--complete-progress" true
                                              "bbs--complete-progress--value-empty" true})})
   [:div {:class-name "bbs--complete-progress--data"}
    [:div {:class-name "bbs--complete-progress--not-started"}
     "Not Started"]]])

(defn- not-scored
  [{:keys [completed? caption text value]}]
  (let [value-group (if completed? "complete" "empty")]
    [:div (cond-> {:class-name (get-class-name {"bbs--complete-progress"                           true
                                                (str "bbs--complete-progress--value-" value-group) true})})
     [:div {:class-name (get-class-name {"bbs--complete-progress--value" true})
            :style      {:width (str value "%")}}]
     [:div {:class-name "bbs--complete-progress--data"}
      [:div.bbs--complete-progress--text-block
       (when (some? caption)
         [:div.bbs--complete-progress--caption caption])
       (when (some? text)
         [:div.bbs--complete-progress--text text])]
      (when completed?
        [system-icon {:icon       "check"
                      :class-name "bbs--complete-progress--complete-icon"}])]]))

(defn- scored
  [{:keys [caption class-name text title score]}]
  (let [{:keys [correct incorrect] :or {correct 0 incorrect 0}} score
        value (* 100 (float (/ correct (+ correct incorrect))))
        value-group (cond
                      (> value 99) "completed" 
                      (> value (:mid value-thresholds)) "high"
                      (> value (:low value-thresholds)) "mid"
                      :else "low")]
    [:div (cond-> {:class-name (get-class-name {"bbs--complete-progress"                           true
                                                (str "bbs--complete-progress--value-" value-group) true
                                                class-name                                         (some? class-name)})}
                  (some? title) (assoc :title title))
     [:div {:class-name (get-class-name {"bbs--complete-progress--value" true})
            :style      {:width (str value "%")}}]
     [:div {:class-name "bbs--complete-progress--data"}
      [:div.bbs--complete-progress--text-block
       (when (some? caption)
         [:div.bbs--complete-progress--caption caption])
       (when (some? text)
         [:div.bbs--complete-progress--text text])]
      [:div (str correct "/" (+ correct incorrect))]]]))

(defn complete-progress
  [{:keys [started? score] :as props}]
  (let [scored? (or (:correct score) (:incorrect score))]
    (cond
      (not started?) [not-started]
      (not scored?) [not-scored props]
      :else [scored props])))
