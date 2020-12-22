(ns webchange.student-dashboard.assessments.views-assessments-list
  (:require
    [webchange.student-dashboard.assessments.views-assessments-list-item :refer [assessments-list-item]]))

(defn prepare-data
  [{:keys [image] :as data}]
  (-> data
      (assoc :image {:png image})))

(defn- get-styles
  []
  {:list           {:display         "flex"
                    :justify-content "space-between"}
   :list-item      {:flex-grow    1
                    :margin-right "24px"}
   :list-item-last {:flex-grow 1}})

(defn- last?
  [item list]
  (->> (last list)
       (= item)))

(defn assessments-list
  [{:keys [data max-count on-click]}]
  (let [styles (get-styles)
        assessments-list (if-not (nil? max-count)
                           (take-last max-count data)
                           data)]
    [:div {:style (:list styles)}
     (for [{:keys [level lesson activity] :as assessment} assessments-list]
       ^{:key (str level "-" lesson "-" activity)}
       [assessments-list-item (merge (prepare-data assessment)
                                     {:on-click on-click
                                      :style    (if (last? assessment data)
                                                  (:list-item-last styles)
                                                  (:list-item styles))})])]))
