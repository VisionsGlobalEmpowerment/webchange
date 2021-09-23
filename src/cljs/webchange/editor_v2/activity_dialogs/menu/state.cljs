(ns webchange.editor-v2.activity-dialogs.menu.state
  (:require
   [re-frame.core :as re-frame]
   [webchange.editor-v2.activity-dialogs.form.state :as parent-state]
   [webchange.editor-v2.translator.translator-form.state.scene :as state-translator]
   [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:menu])
       (parent-state/path-to-db)))

(def sections {:delay          {:name "Delay"
                                :icon "delay"}
               :effects        {:name "Effects"
                                :icon "effect"}
               :phrase         {:name "Phrase Action"
                                :icon "add"}
               :text-animation {:name "Text Animation Action"
                                :icon "text-animation"}
               :voice-over     {:name "Voice-over"
                                :icon "mic"}
               :translate      {:name "Translate"
                                :icon "effect"}})
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
             (get-section :effects)
             (get-section :translate)]
      (some? selected-action) (conj (get-section :delay))
      (and (some? selected-action)
           (some #{(:type selected-action)} [:phrase :text-animation])) (conj (get-section :voice-over)))))

;;

(defn get-selected-action-data
  [db]
  (let [actions-data (state-translator/actions-data db)
        {:keys [path]} (parent-state/get-selected-action db)]
    (get-in actions-data path)))

(re-frame/reg-sub
  ::selected-action-data
  (fn []
    [(re-frame/subscribe [::state-translator/actions-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::parent-state/selected-action])])
  (fn [[actions-data concept-data {:keys [path source]}]]
    (when (some? path)
      (if (= source :concept)
        (get-in (:data concept-data) path)
        (get-in actions-data path)))))
