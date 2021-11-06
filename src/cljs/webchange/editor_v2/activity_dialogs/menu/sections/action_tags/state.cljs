(ns webchange.editor-v2.activity-dialogs.menu.sections.action-tags.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state :as state-dialog]
    [webchange.editor-v2.activity-dialogs.menu.state :as parent-state]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.utils.scene-action-data :refer [action-tags]]))

(def available-tags [{:text  "Not skippable action"
                      :value (:unskippable-action action-tags)}])

(defn- selected-action-data->current-tags
  [selected-action-data]
  (->> (get selected-action-data :tags [])
       (map (fn [tag-value]
              {:text  (or (some (fn [{:keys [text value]}] (and (= value tag-value) text)) available-tags))
               :value tag-value}))))

(defn- get-current-tags
  [db]
  (let [selected-action-data (parent-state/get-selected-action-data db)]
    (selected-action-data->current-tags selected-action-data)))

(re-frame/reg-sub
  ::current-tags
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action-data])])
  (fn [[selected-action-data]]
    (selected-action-data->current-tags selected-action-data)))

(re-frame/reg-sub
  ::available-tags
  (fn []
    [(re-frame/subscribe [::current-tags])])
  (fn [[current-tags]]
    (->> available-tags
         (filter (fn [available-tag]
                   (->> current-tags
                        (some (fn [current-tag]
                                (= (:value available-tag) (:value current-tag))))
                        (not)))))))

(re-frame/reg-event-fx
  ::add-tag
  (fn [{:keys [db]} [_ value]]
    (let [current-tags (->> (get-current-tags db)
                            (map :value))
          new-tags (conj current-tags value)]
      {:dispatch [::set-current-action-tags new-tags]})))

(re-frame/reg-event-fx
  ::remove-tag
  (fn [{:keys [db]} [_ value]]
    (let [current-tags (->> (get-current-tags db)
                            (map :value))
          new-tags (remove #(= % value) current-tags)]
      {:dispatch [::set-current-action-tags new-tags]})))

(re-frame/reg-event-fx
  ::set-current-action-tags
  (fn [{:keys [db]} [_ tags]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-action-by-path {:action-path path
                                                         :action-type source
                                                         :data-patch  {:tags tags}}]})))
