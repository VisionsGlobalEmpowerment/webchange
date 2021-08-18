(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :refer [get-available-effects]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [dialog-phrase-action? effect-action? text-animation-action?]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-translator]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:side-menu])
       (parent-state/path-to-db)))

;; Selected Action Data

(defn get-selected-action-data
  [db]
  (let [actions-data (state-translator/actions-data db)
        {:keys [path]} (parent-state/get-selected-action db)]
    (get-in actions-data path)))

(re-frame/reg-sub
  ::selected-action-data
  (fn []
    [(re-frame/subscribe [::state-translator/actions-data])
     (re-frame/subscribe [::parent-state/selected-action])])
  (fn [[actions-data {:keys [path]}]]
    (when (some? path)
      (get-in actions-data path))))

;; Available Sections

(re-frame/reg-sub
  ::available-effects
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action])
     (re-frame/subscribe [::parent-state/scene-available-actions])])
  (fn [[{:keys [node-data]} scene-available-actions]]
    (-> (get-available-effects node-data)
        (concat scene-available-actions))))

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
    [(re-frame/subscribe [::parent-state/selected-action])
     (re-frame/subscribe [::current-section])])
  (fn [[selected-action current-section]]
    (->> (cond-> [[(get-section :phrase)
                   (get-section :text-animation)
                   (get-section :effects)]]
                 (some? selected-action)
                 (conj (cond-> [(get-section :delay)]
                               ;; Voice-over
                               (some #{(:type selected-action)} [:phrase :text-animation])
                               (conj (get-section :voice-over)))))
         (map (fn [section-group]
                (map (fn [{:keys [id] :as section}]
                       (->> (= id (:id current-section))
                            (assoc section :selected?)))
                     section-group))))))

;; Current Section

(def current-section-path (path-to-db [:current-section]))

(re-frame/reg-sub
  ::current-section-manual-selected
  (fn [db]
    (get-in db current-section-path)))

(re-frame/reg-sub
  ::current-section
  (fn []
    [(re-frame/subscribe [::current-section-manual-selected])
     (re-frame/subscribe [::selected-action-data])])
  (fn [[manual-selection selected-action-data]]
    (cond
      (some? manual-selection) manual-selection
      (dialog-phrase-action? selected-action-data) (get-section :voice-over)
      (effect-action? selected-action-data) (get-section :effects)
      (text-animation-action? selected-action-data) (get-section :text-animation)
      :default (get-section :phrase))))

(re-frame/reg-event-fx
  ::set-current-section
  (fn [{:keys [db]} [_ section-id]]
    {:db (assoc-in db current-section-path section-id)}))
