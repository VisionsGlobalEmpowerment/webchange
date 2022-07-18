(ns webchange.lesson-builder.tools.script.target-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]))

(defn- get-available-options
  [activity-data]
  (->> (utils/get-scene-objects activity-data)
       (filter (fn [[_ {:keys [type]}]]
                 (= type "animation")))
       (map (fn [[object-name {:keys [scene-name]}]]
              {:text  (->> (or scene-name (name object-name))
                           (clojure.string/capitalize))
               :value (name object-name)}))
       (concat [{:text  "Guide"
                 :value "guide"}])))

(re-frame/reg-sub
  ::target-options
  :<- [::state/activity-data]
  (fn [activity-data [_ current-value]]
    (->> (get-available-options activity-data)
         (filter #(not= (:value %) current-value)))))

(re-frame/reg-sub
  ::current-value
  :<- [::state/activity-data]
  (fn [activity-data [_ current-value]]
    (->> (get-available-options activity-data)
         (some #(and (= (:value %) current-value)
                     %)))))
