(ns webchange.lesson-builder.tools.script.target-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.list :refer [sort-by-getters]]))

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

(defn- get-text-animation-targets
  [activity-data]
  (->> (get activity-data :objects [])
       (filter (fn [[_ {:keys [metadata type]}]]
                 (and (= type "text")
                      (get metadata :text-animation-target? true))))
       (sort-by-getters [#(get-in (second %) [:metadata :page-idx] ##Inf)
                         #(get-in (second %) [:metadata :text-idx] ##Inf)
                         #(first %)])
       (map (fn [[object-name {:keys [metadata text]}]]
              {:text        text
               :display-name (:display-name metadata)
               :text-prefix (or (:display-name metadata)
                                (clojure.core/name object-name))
               :value       (clojure.core/name object-name)}))))

(re-frame/reg-sub
  ::target-options
  :<- [::state/activity-data]
  (fn [activity-data [_ type current-value]]
    (cond->> (case type
               :character (get-available-options activity-data)
               :text-animation (get-text-animation-targets activity-data))
             (some? current-value) (filter #(not= (:value %) current-value)))))

(re-frame/reg-sub
  ::current-value-data
  (fn [[_ type]]
    (re-frame/subscribe [::target-options type]))
  (fn [target-options [_ _ current-value]]
    (->> target-options
         (some #(and (= (:value %) current-value)
                     %)))))
