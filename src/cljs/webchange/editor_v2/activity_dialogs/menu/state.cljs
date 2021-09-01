(ns webchange.editor-v2.activity-dialogs.menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state :as parent-state]))

(def sections {:delay          {:name "Delay"
                                :icon "delay"}
               :effects        {:name "Effects"
                                :icon "effect"}
               :phrase         {:name "Phrase Action"
                                :icon "add"}
               :text-animation {:name "Text Animation Action"
                                :icon "text-animation"}
               :voice-over     {:name "Voice-over"
                                :icon "mic"}})
(defn- get-section
  [id]
  (-> (get sections id)
      (assoc :id id)))

(re-frame/reg-sub
  ::available-sections
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action])])
  (fn [[selected-action]]
    (cond-> [(get-section :phrase)
             (get-section :text-animation)
             (get-section :effects)]
            (some? selected-action) (conj (get-section :delay))
            (and (some? selected-action)
                 (some #{(:type selected-action)} [:phrase :text-animation])) (conj (get-section :voice-over)))))