(ns webchange.interpreter.object-data.navigation-param
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.utils.find-exit :refer [find-path]]
    [webchange.subs :as subs]
    [webchange.student-dashboard.subs :as student-dashboard-subs]))

(defn- lock-object [o]
  (let [o (-> o
              (assoc :actions {})
              (dissoc :highlight)
              (assoc :filter "grayscale"))]
    o))

(defn- get-lesson-based-open-activity-names []
  (let [{:keys [id]} @(re-frame/subscribe [::student-dashboard-subs/next-activity])
        finished-level-lesson-activities @(re-frame/subscribe [::student-dashboard-subs/finished-level-lesson-activities])
        activities (conj finished-level-lesson-activities id)]
    activities))

(defn- get-activity-based-open-activity []
  (let [{:keys [id]} @(re-frame/subscribe [::student-dashboard-subs/next-activity])
        finished-activities (set (map #(:id %) @(re-frame/subscribe [::student-dashboard-subs/finished-activities])))
        activities (conj finished-activities id)]
    activities))

(defn with-navigation-params [scene-id object-name o]
  (if (some? scene-id)
    (let [navigation-mode @(re-frame/subscribe [::subs/navigation-mode])
          activity-names (if (= navigation-mode :lesson) (get-lesson-based-open-activity-names) (get-activity-based-open-activity))
          scene-list @(re-frame/subscribe [::subs/scene-list])
          all-activities (set (flatten (map #(find-path scene-id % scene-list) activity-names)))
          outs (set (flatten (map #(:name %) (:outs ((keyword scene-id) scene-list)))))]
      (if (contains? outs object-name)
        (if (contains? all-activities object-name) o (lock-object o))
        o))
    o))
