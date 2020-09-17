(ns webchange.editor-v2.translator.translator-form.state.concepts
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.concepts-utils :refer [get-concepts-audio-assets]]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

;; Subs

(re-frame/reg-sub
  ::scene-versions
  (fn [db]
    (get-in db [:editor :scene-versions])))

(defn course-datasets
  [db]
  (get-in db [:editor :course-datasets]))

(re-frame/reg-sub
  ::course-datasets
  course-datasets)

(defn- get-concept-scheme
  [course-datasets]
  (some (fn [dataset]
          (and (= "concepts" (:name dataset))
               dataset))
        course-datasets))

(defn concept-scheme
  [db]
  (let [course-datasets (course-datasets db)]
    (get-concept-scheme course-datasets)))

(re-frame/reg-sub
  ::concept-scheme
  (fn []
    [(re-frame/subscribe [::course-datasets])])
  (fn [[course-datasets]]
    (get-concept-scheme course-datasets)))

(defn current-concept
  [db]
  (let [current-concept-id (get-in db (path-to-db [:concepts :current-concept]))]
    (get-in db (path-to-db [:concepts :data current-concept-id]))))

(re-frame/reg-sub
  ::current-concept
  current-concept)

(defn edited-concepts
  [db]
  (get-in db (path-to-db [:concepts :edited-concepts])))

(re-frame/reg-sub
  ::edited-concepts
  edited-concepts)

(re-frame/reg-sub
  ::current-concept-data
  (fn []
    [(re-frame/subscribe [::current-concept])])
  (fn [[current-concept]]
    (:data current-concept)))

(defn concepts-data
  [db]
  (get-in db (path-to-db [:concepts :data])))

(defn current-dataset-concept
  [db]
  (get-in db (path-to-db [:current-dataset-concept])))

(re-frame/reg-sub
  ::concepts-data
  concepts-data)

(defn- get-concepts-list
  [concepts-data]
  (->> concepts-data (vals) (sort-by :name)))

(defn concepts-list
  [db]
  (-> db concepts-data get-concepts-list))

(re-frame/reg-sub
  ::concepts-list
  (fn []
    [(re-frame/subscribe [::concepts-data])])
  (fn [[concepts-data]]
    (get-concepts-list concepts-data)))

(defn concepts-audios
  [db]
  (let [scheme (concept-scheme db)
        list (concepts-list db)
        scene-id (translator-form.scene/scene-id db)]
    (get-concepts-audio-assets scheme list scene-id)))

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [concepts (editor-subs/course-dataset-items db)
          dataset-concept (editor-subs/dataset-concept db)
          current-concept (->> concepts (vals) (sort-by :name) (first))]
      {:db         (-> db
                       (assoc-in (path-to-db [:concepts :data]) concepts)
                       (assoc-in (path-to-db [:current-dataset-concept]) dataset-concept))
       :dispatch-n (list [::set-current-concept (:id current-concept)]
                         [::set-edited-concepts []])})))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ concept-id]]
    {:db (assoc-in db (path-to-db [:concepts :current-concept]) concept-id)}))

(re-frame/reg-event-fx
  ::reset-current-concept
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::set-current-concept nil])}))

(re-frame/reg-event-fx
  ::set-edited-concepts
  (fn [{:keys [db]} [_ edited-concepts]]
    {:db (assoc-in db (path-to-db [:concepts :edited-concepts]) edited-concepts)}))

(re-frame/reg-event-fx
  ::add-edited-concepts
  (fn [{:keys [db]} [_ concept-id]]
    (let [current-list (edited-concepts db)
          updated-list (-> current-list
                           (conj concept-id)
                           (distinct))]
      {:dispatch-n (list [::set-edited-concepts updated-list])})))

(re-frame/reg-event-fx
  ::update-current-concept
  (fn [{:keys [db]} [_ action-path data-patch]]
    (let [current-concept (current-concept db)
          action-data (get-in current-concept (concat [:data] action-path))
          updated-data (merge action-data data-patch)]
      {:db (assoc-in db (path-to-db (concat [:concepts :data] [(:id current-concept) :data] action-path)) updated-data)
       :dispatch-n (list [::add-edited-concepts (:id current-concept)])})))
