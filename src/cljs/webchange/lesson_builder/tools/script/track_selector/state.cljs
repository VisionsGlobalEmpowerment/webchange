(ns webchange.lesson-builder.tools.script.track-selector.state
  (:require
    [clojure.set :refer [difference]]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.utils.scene-data :as utils]))

(defn- available-tracks
  [activity-data]
  (let [untracked-actions (script-state/collect-untracked-actions activity-data)]
    (->> (cond-> (utils/get-tracks activity-data)
                 (seq untracked-actions) (conj {:title "Untracked"
                                                :value nil}))
         (map-indexed (fn [idx {:keys [title default]}]
                        {:text  title
                         :default default
                         :value idx}))
         (sort-by :text))))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} _]
    (let [tracks (available-tracks activity-data)
          default-idx (->> tracks (filter #(get % :default)) first :value)
          main-script-idx (->> tracks (filter #(= "Main Script" (:text %))) first :value)]
      {:dispatch [::script-state/set-current-track (or default-idx main-script-idx 0)]})))

(re-frame/reg-event-fx
  ::set-current-track
  (fn [_ [_ value]]
    {:dispatch [::script-state/set-current-track value]}))

(re-frame/reg-sub
  ::available-tracks
  :<- [::state/activity-data]
  (fn [activity-data]
    (available-tracks activity-data)))

(re-frame/reg-sub
  ::current-track
  :<- [::script-state/current-track]
  :<- [::available-tracks]
  (fn [[current-track available-tracks]]
    (->> available-tracks
         (some (fn [{:keys [text value]}]
                 (and (= value current-track) text))))))

(re-frame/reg-sub
  ::track-options
  :<- [::available-tracks]
  :<- [::script-state/current-track]
  (fn [[available-tracks current-track]]
    (filter (fn [{:keys [value]}]
              (not= value current-track))
            available-tracks)))
