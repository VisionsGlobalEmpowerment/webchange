(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :refer [get-available-effects]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as parent-state]
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
    (get-in actions-data path)))

;; Available Sections

(re-frame/reg-sub
  ::available-effects
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action])
     (re-frame/subscribe [::parent-state/scene-available-actions])])
  (fn [[{:keys [node-data]} scene-available-actions]]
    (-> (get-available-effects node-data)
        (concat scene-available-actions))))

(re-frame/reg-sub
  ::available-sections
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action])
     (re-frame/subscribe [::available-effects])
     (re-frame/subscribe [::current-section])])
  (fn [[selected-action available-effects current-section]]
    (->> (cond-> [[{:id   :phrase
                    :name "Phrase Action"
                    :icon "add"}
                   {:id   :text-animation
                    :name "Text Animation Action"
                    :icon "text-animation"}]]
                 (some? selected-action)
                 (conj (cond-> [{:id   :delay
                                 :name "Delay"
                                 :icon "delay"}]
                               ;; Effects
                               (-> available-effects empty? not)
                               (conj {:id   :effects
                                      :name "Effects"
                                      :icon "effect"})
                               ;; Voice-over
                               (some #{(:type selected-action)} [:phrase :text-animation])
                               (conj {:id   :voice-over
                                      :name "Voice-over"
                                      :icon "mic"}))))
         (map (fn [section-group]
                (map (fn [{:keys [id] :as section}]
                       (->> (= id (:id current-section))
                            (assoc section :selected?)))
                     section-group))))))

;; Current Section

(def current-section-path (path-to-db [:current-section]))

(re-frame/reg-sub
  ::current-section
  (fn [db]
    (get-in db current-section-path)))

(re-frame/reg-event-fx
  ::set-current-section
  (fn [{:keys [db]} [_ section-id]]
    {:db (assoc-in db current-section-path section-id)}))
