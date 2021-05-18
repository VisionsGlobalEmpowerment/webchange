(ns webchange.editor-v2.layout.components.sandbox.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.create-link :refer [create-link]]
    [webchange.editor-v2.layout.components.sandbox.parse-actions :refer [find-all-actions]]
    [webchange.editor-v2.layout.state :as parent-state]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:sandbox])
       (parent-state/path-to-db)
       (vec)))

;; Data

(def lesson-set-data-path (path-to-db [:data]))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db lesson-set-data-path {})}))

(re-frame/reg-sub
  ::lesson-set-data
  (fn [db]
    ;{:concepts {:item-ids [188 196 208], :dataset-id 4}}
    (get-in db lesson-set-data-path {})))

(re-frame/reg-sub
  ::lesson-sets
  (fn [db]
    (let [scene-data (subs/current-scene-data db)]
      (->> (find-all-actions scene-data {:type ["lesson-var-provider"]})
           (map second)
           (map :from)))))

(re-frame/reg-sub
  ::form-has-params?
  (fn []
    [(re-frame/subscribe [::lesson-sets])])
  (fn [[lesson-sets]]
    (-> lesson-sets empty? not)))

(re-frame/reg-sub
  ::form-params-defined?
  (fn []
    [(re-frame/subscribe [::lesson-sets])
     (re-frame/subscribe [::lesson-set-data])])
  (fn [[lesson-sets lesson-sets-data]]
    (let [lesson-set-defined? (fn [lesson-set]
                                (let [{:keys [dataset-id item-ids]} (->> lesson-set
                                                                         keyword
                                                                         (get lesson-sets-data))]
                                  (and (some? dataset-id)
                                       (-> item-ids empty? not))))]
      (->> (map lesson-set-defined? lesson-sets)
           (every? true?)))))

(re-frame/reg-sub
  ::current-course
  (fn [db]
    (:current-course db)))

(re-frame/reg-sub
  ::current-scene
  (fn [db]
    (:current-scene db)))

(re-frame/reg-sub
  ::link
  (fn []
    [(re-frame/subscribe [::form-params-defined?])
     (re-frame/subscribe [::current-course])
     (re-frame/subscribe [::current-scene])
     (re-frame/subscribe [::lesson-set-data])])
  (fn [[form-params-defined? current-course current-scene lesson-set-data]]
    (if form-params-defined?
      (create-link {:course-slug current-course
                    :scene-slug  current-scene
                    :lessons     lesson-set-data})
      "")))

;; Datasets

(defn- current-dataset-id-path [lesson-set-name] (concat lesson-set-data-path [lesson-set-name :dataset-id]))

(re-frame/reg-sub
  ::current-dataset-id
  (fn [db [_ lesson-set-name]]
    (get-in db (current-dataset-id-path lesson-set-name))))

(re-frame/reg-event-fx
  ::set-current-dataset-id
  (fn [{:keys [db]} [_ lesson-set-name dataset-id]]
    {:db       (assoc-in db (current-dataset-id-path lesson-set-name) dataset-id)
     :dispatch [::reset-current-dataset-items-ids lesson-set-name]}))

(re-frame/reg-sub
  ::datasets-options
  (fn []
    [(re-frame/subscribe [::editor-subs/course-datasets])])
  (fn [[datasets]]
    (->> datasets
         (map (fn [{:keys [id name]}]
                {:text  name
                 :value id})))))

;; Dataset Items

(defn- current-dataset-items-ids-path [lesson-set-name] (concat lesson-set-data-path [lesson-set-name :item-ids]))

(re-frame/reg-sub
  ::current-dataset-items-ids
  (fn [db [_ lesson-set-name]]
    (get-in db (current-dataset-items-ids-path lesson-set-name) [])))

(re-frame/reg-event-fx
  ::set-current-dataset-items-ids
  (fn [{:keys [db]} [_ lesson-set-name dataset-items-ids]]
    {:db (assoc-in db (current-dataset-items-ids-path lesson-set-name) dataset-items-ids)}))

(re-frame/reg-event-fx
  ::reset-current-dataset-items-ids
  (fn [{:keys [db]} [_ lesson-set-name]]
    {:db (assoc-in db (current-dataset-items-ids-path lesson-set-name) [])}))

(re-frame/reg-sub
  ::datasets-items-options
  (fn [[_ lesson-set-name]]
    [(re-frame/subscribe [::current-dataset-id lesson-set-name])
     (re-frame/subscribe [::editor-subs/course-dataset-items])])
  (fn [[current-dataset-id dataset-items]]
    (->> dataset-items
         (map second)
         (filter (fn [{:keys [dataset-id]}] (= dataset-id current-dataset-id)))
         (map (fn [{:keys [id name]}]
                {:text  name
                 :value id})))))
