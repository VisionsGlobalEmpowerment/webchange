(ns webchange.interpreter.object-data.navigation-param
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.utils.find-exit :refer [find-path]]
    [webchange.subs :as subs]
    [webchange.interpreter.subs :as interpreter-subs]
    [webchange.student-dashboard-v2.subs :as student-dashboard-subs]))

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
  (let [scene-list @(re-frame/subscribe [::interpreter-subs/navigation-scene-list])
        navigation-mode @(re-frame/subscribe [::subs/navigation-mode])
        scene-outs (->> scene-id keyword (get scene-list) :outs)
        out-objects (-> (map :object scene-outs) set)
        is-out? (contains? out-objects object-name)
        out-names (->> scene-outs
                       (filter #(= object-name (:object %)))
                       (map :name)
                       set)
        activity-names (if (= navigation-mode :lesson) (get-lesson-based-open-activity-names) (get-activity-based-open-activity))
        all-activities (set (flatten (map #(find-path scene-id % scene-list) activity-names)))
        is-locked? (empty? (clojure.set/intersection out-names all-activities))]
    (if (and is-out? is-locked?)
      (lock-object o)
      o)))
