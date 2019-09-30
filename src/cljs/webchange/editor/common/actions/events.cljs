(ns webchange.editor.common.actions.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]
    [webchange.editor.core :as editor]
    [webchange.common.anim :refer [animations]]))

(re-frame/reg-event-fx
  ::reset-form-data
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :action-form)}))

(re-frame/reg-event-fx
  ::set-form-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:editor :action-form] {:data data :path [] :breadcrumb []})}))

(re-frame/reg-event-fx
  ::select-action-data-path
  (fn [{:keys [db]} [_ step]]
    (let [path (get-in db [:editor :action-form :path])]
      {:db (-> db
               (update-in [:editor :action-form :breadcrumb] conj path)
               (assoc-in [:editor :action-form :path] (-> path (concat [:data step]) vec)))})))

(re-frame/reg-event-fx
  ::select-action-options-path
  (fn [{:keys [db]} [_ step]]
    (let [path (get-in db [:editor :action-form :path])]
      {:db (-> db
               (update-in [:editor :action-form :breadcrumb] conj path)
               (assoc-in [:editor :action-form :path] (-> path (concat [:options step]) vec)))})))

(re-frame/reg-event-fx
  ::select-action-path-prev
  (fn [{:keys [db]} _]
    (let [breadcrumb (get-in db [:editor :action-form :breadcrumb])]
      (if (peek breadcrumb)
        {:db (-> db
                 (assoc-in [:editor :action-form :path] (peek breadcrumb))
                 (assoc-in [:editor :action-form :breadcrumb] (pop breadcrumb)))}))))


(re-frame/reg-event-fx
  ::edit-selected-action
  (fn [{:keys [db]} [_ state]]
    (let [path (get-in db [:editor :action-form :path])
          data-path (vec (concat [:editor :action-form :data] path))]
      {:db (assoc-in db data-path state)
       :dispatch [::select-action-path-prev]})))

(re-frame/reg-event-fx
  ::store-selected-action
  (fn [{:keys [db]} [_ state]]
    (let [path (get-in db [:editor :action-form :path])
          data-path (vec (concat [:editor :action-form :data] path))]
      {:db (assoc-in db data-path state)})))

(defn inner-data-path [db]
  (let [path (get-in db [:editor :action-form :path])]
    (vec (concat [:editor :action-form :data] path [:data]))))

(re-frame/reg-event-fx
  ::selected-action-order-down
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)]
      (if (< (inc index) (count original-data))
        (let [head (subvec original-data 0 index)
              tail (subvec original-data (inc (inc index)))
              v1 (subvec original-data index (inc index))
              v2 (subvec original-data (inc index) (inc (inc index)))
              data (vec (concat head v2 v1 tail))]
          {:db (assoc-in db inner-data-path data)})))))

(re-frame/reg-event-fx
  ::selected-action-order-up
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)]
      (if (> index 0)
        (let [head (subvec original-data 0 (dec index))
              tail (subvec original-data (inc index))
              v1 (subvec original-data (dec index) index)
              v2 (subvec original-data index (inc index))
              data (vec (concat head v2 v1 tail))]
          {:db (assoc-in db inner-data-path data)})))))

(defn insert-into [data position]
  (cond
    (= position 0) (vec (concat [{}] data))
    (= position (count data)) (vec (concat data [{}]))
    :else (let [head (subvec data 0 position)
                tail (subvec data position)]
            (vec (concat head [{}] tail)))))

(defn remove-from [data position]
  (let [head (subvec data 0 position)
        tail (subvec data (inc position))]
    (vec (concat head tail))))

(re-frame/reg-event-fx
  ::selected-action-add-above
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)
          data (insert-into original-data index)]
      {:db (assoc-in db inner-data-path data)})))

(defn inner-option-path [db key]
  (let [path (get-in db [:editor :action-form :path])]
    (vec (concat [:editor :action-form :data] path [:options key]))))

(re-frame/reg-event-fx
  ::selected-action-add-option
  (fn [{:keys [db]} [_ key]]
    (let [inner-option-path (inner-option-path db key)]
      {:db (assoc-in db inner-option-path {})
       :dispatch [::select-action-options-path key]})))

(re-frame/reg-event-fx
  ::selected-action-add-above-action
  (fn [{:keys [db]} [_ index]]
    {:dispatch-n (list [::selected-action-add-above index]
                       [::select-action-data-path index])}))

(re-frame/reg-event-fx
  ::selected-action-add-below
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)
          data (insert-into original-data (inc index))]
      {:db (assoc-in db inner-data-path data)})))

(re-frame/reg-event-fx
  ::selected-action-add-below-action
  (fn [{:keys [db]} [_ index]]
    {:dispatch-n (list [::selected-action-add-below index]
                       [::select-action-data-path (inc index)])}))

(re-frame/reg-event-fx
  ::selected-action-remove-data
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)
          data (remove-from original-data index)]
      {:db (assoc-in db inner-data-path data)})))

(re-frame/reg-event-fx
  ::selected-action-copy-data
  (fn [{:keys [db]} [_ index]]
    (let [inner-data-path (inner-data-path db)
          original-data (get-in db inner-data-path)
          new-element (-> (get original-data index)
                          (dissoc :data))]
      {:db (assoc-in db inner-data-path (conj original-data new-element))
       :dispatch [::select-action-data-path (count original-data)]})))

(re-frame/reg-event-fx
  ::selected-action-remove-option
  (fn [{:keys [db]} [_ key]]
    (let [inner-option-path (inner-option-path db key)]
      {:db (update-in db (pop inner-option-path) dissoc key)})))

(re-frame/reg-event-fx
  ::rename-selected-action-option
  (fn [{:keys [db]} [_ old-key new-key]]
    (let [option-path (-> (inner-option-path db key) pop)
          option (-> db
                     (get-in option-path)
                     old-key)]
      {:db (-> db
               (update-in option-path dissoc key)
               (update-in option-path assoc new-key option))})))
