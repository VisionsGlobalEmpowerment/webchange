(ns webchange.editor-v2.lessons.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.events :as ee]
    [webchange.editor-v2.lessons.subs :as lessons-subs]))

(defn- first-dataset-id
  [db]
  (-> db
      (get-in [:editor :course-datasets])
      first
      :id))

(defn- lesson-set-name->id
  [db name]
  (-> (lessons-subs/get-lesson-set db name) :id))

(defn- prepare-lesson-set
  [{items :items}]
  {:items (map #(select-keys % [:id]) items)})

(defn- lesson-set->edit-request
  [db lesson-set]
  (let [id (lesson-set-name->id db (:name lesson-set))]
    [::edit-lesson-set id (prepare-lesson-set lesson-set)]))

(defn- lesson-set->add-request
  [db lesson-set]
  (let [first-dataset-id (first-dataset-id db)]
    [::add-lesson-set first-dataset-id (:name lesson-set) (prepare-lesson-set lesson-set)]))

(defn- lesson-set-exist?
  [db name]
  (boolean (lesson-set-name->id db name)))

(defn- lesson-set->request
  [db lesson-set]
  (if (lesson-set-exist? db (:name lesson-set))
    (lesson-set->edit-request db lesson-set)
    (lesson-set->add-request db lesson-set)))

(defn- level-index
  [course level-id]
  (->> course
       :levels
       (keep-indexed #(if (= (:level %2) level-id) %1))
       first))

(defn- lesson-index
  [level lesson-id]
  (->> level
       :lessons
       (keep-indexed #(if (= (:lesson %2) lesson-id) %1))
       first))

(defn- set-lesson
  [course level-id lesson-id lesson]
  (let [level-idx (level-index course level-id)
        level (get-in course [:levels level-idx])
        lesson-idx (lesson-index level lesson-id)]
    (if (and level-idx lesson-idx)
      (assoc-in course [:levels level-idx :lessons lesson-idx] lesson)
      course)))

(defn- add-lesson
  [course level-id lesson]
  (let [level-idx (level-index course level-id)]
    (if level-idx
      (-> course
          (update-in [:levels level-idx :lessons] vec)
          (update-in [:levels level-idx :lessons] conj lesson))
      course)))

(defn- name-for-lesson-set
  [type level-id lesson-id]
  (str (name type) "-" level-id "-" lesson-id))

(defn- with-names
  [lesson-sets level-id lesson-id]
  (->> lesson-sets
       (map (fn [[type lesson-set]]
              (if (nil? (:name lesson-set))
                [type (assoc lesson-set :name (name-for-lesson-set type level-id lesson-id))]
                [type lesson-set])))
       (into {})))

(defn- next-lesson-id
  [course level-id]
  (let [level-idx (level-index course level-id)
        last-lesson-id (->> (get-in course [:levels level-idx :lessons])
                            (map :lesson)
                            (reduce max)
                            int)]
    (inc last-lesson-id)))

(re-frame/reg-event-fx
  ::add-lesson
  (fn [{:keys [db]} [_ course-id level-id {lesson-sets :lesson-sets :as data}]]
    (let [lesson-id (next-lesson-id (:course-data db) level-id)
          lesson-sets (with-names lesson-sets level-id lesson-id)
          lesson (assoc data
                   :lesson-sets (zipmap (keys lesson-sets) (map :name (vals lesson-sets)))
                   :lesson lesson-id)
          course (add-lesson (:course-data db) level-id lesson)]
      {:db (-> db
               (assoc-in [:loading :edit-lesson] true)
               (assoc :course-data course))
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id)
                    :params          {:course course}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::edit-lesson-success course-id]
                    :on-failure      [:api-request-error :edit-lesson]}
       :dispatch-n (map #(lesson-set->request db (second %)) lesson-sets)})))

(re-frame/reg-event-fx
  ::edit-lesson
  (fn [{:keys [db]} [_ course-id level-id lesson-id {lesson-sets :lesson-sets :as data}]]
    (let [lesson-sets (with-names lesson-sets level-id lesson-id)
          lesson (assoc data :lesson-sets (zipmap (keys lesson-sets) (map :name (vals lesson-sets))))
          course (-> db
                     (get :course-data)
                     (set-lesson level-id lesson-id lesson))]
      {:db (-> db
               (assoc-in [:loading :edit-lesson] true)
               (assoc :course-data course))
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id)
                    :params          {:course course}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::edit-lesson-success course-id]
                    :on-failure      [:api-request-error :edit-lesson]}
       :dispatch-n (map #(lesson-set->request db (second %)) lesson-sets)})))

(re-frame/reg-event-fx
  ::edit-lesson-success
  (fn [{:keys [db]} [_ course-id]]
    {:dispatch-n (list [:complete-request :edit-lesson] [::ee/load-lesson-sets course-id])
     :redirect [:course-editor :id course-id]}))

(re-frame/reg-event-fx
  ::edit-lesson-set
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db [:loading :edit-lesson-set] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/lesson-sets/" id)
                  :params          {:data data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-lesson-set-success]
                  :on-failure      [:api-request-error :edit-lesson-set]}}))

(re-frame/reg-event-fx
  ::edit-lesson-set-success
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [:complete-request :edit-lesson-set] [::ee/load-lesson-sets course-id])})))

(re-frame/reg-event-fx
  ::add-lesson-set
  (fn [{:keys [db]} [_ dataset-id name data]]
    {:db (assoc-in db [:loading :add-lesson-set] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/lesson-sets")
                  :params          {:dataset-id dataset-id :name name :data data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::add-lesson-set-success]
                  :on-failure      [:api-request-error :add-lesson-set]}}))

(re-frame/reg-event-fx
  ::add-lesson-set-success
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [:complete-request :add-lesson-set] [::ee/load-lesson-sets course-id])})))