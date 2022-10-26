(ns webchange.class.core
  (:require
    [camel-snake-kebab.core :refer [->snake_case_keyword]]
    [clojure.tools.logging :as log]
    [java-time :as jt]
    [webchange.accounts.core :as accounts]
    [webchange.course.core :as courses]
    [webchange.db.core :as db]
    [webchange.events :as e]))

(defn get-classes [school-id]
  (let [classes (db/get-classes {:school_id school-id})]
    {:classes classes}))

(defn get-class [id]
  (let [{:keys [course-slug] :as class} (db/get-class {:id id})
        course-info (courses/get-course-info course-slug)]
    (when (some? class)
      (assoc class :course-info course-info))))

(defn with-user
  [{user-id :user-id :as item}]
  (let [user (-> (db/get-user {:id user-id})
                 accounts/visible-user)]
    (assoc item :user user)))

(defn with-student-by-user
  [{user-id :user-id :as item}]
  (let [student (-> (db/get-student-by-user {:user_id user-id})
                    accounts/visible-student)]
    (assoc item :student student)))

(defn with-class
  [{class-id :class-id :as item}]
  (let [class (db/get-class {:id class-id})]
    (assoc item :class class)))

(defn prepare-students
  [students]
  (->> students
       (map with-user)
       (map with-class)
       (map accounts/visible-student)))

(defn get-students-by-class [class-id]
  (let [students (-> (db/get-students-by-class {:class_id class-id})
                     prepare-students)]
    {:class-id class-id
     :students students}))

(defn get-students-by-school
  [school-id]
  (-> (db/get-students-by-school {:school_id school-id})
      prepare-students))

(defn get-students-unassigned []
  (let [students (-> (db/get-students-unassigned)
                     prepare-students)]
    {:students students}))

(defn get-student [id]
  (if-let [student (db/get-student {:id id})]
    (-> student
        (with-class)
        (#(assoc % :date-of-birth (-> % :date-of-birth str)))
        with-user)))

(defn create-class!
  [data]
  (let [prepared-data (db/transform-keys-one-level ->snake_case_keyword data)
        [{id :id}] (db/create-class! prepared-data)]
    (e/dispatch {:type :classes/created :school-id (:school-id data) :class-id id})
    [true {:id id}]))

(defn update-class!
  [id data]
  (let [current-data (get-class id)
        prepared-data (merge current-data
                             (-> (db/transform-keys-one-level ->snake_case_keyword data)
                                 (assoc :id id)))]
    (db/update-class! prepared-data)
    [true {:id   id
           :data data}]))

(defn delete-class!
  [id]
  (db/delete-class! {:id id})
  [true {:id id}])

(defn- prepare-student-data
  [data]
  (let [date-of-birth (when (and (string? (:date-of-birth data))
                                 (not (empty? (:date-of-birth data))))
                        (jt/local-date "yyyy-MM-dd" (:date-of-birth data)))
        transform #(db/transform-keys-one-level ->snake_case_keyword %)
        optional-fields (->> [:class-id :last-name :gender :date-of-birth]
                             (map #(vector % nil))
                             (into {})
                             (transform))]
    (->> (assoc data :date-of-birth date-of-birth)
         (transform)
         (merge optional-fields))))

(defn create-student!
  [data]
  (let [prepared-data (prepare-student-data data)
        [{id :id}] (db/create-student! prepared-data)]
    (e/dispatch {:type :students/created :school-id (:school-id data) :class-id (:class-id data)})
    [true {:id id}]))

(defn update-student!
  [id {class-id :class-id :as data}]
  (let [{user-id :user-id} (db/get-student {:id id})
        prepared-data (-> data
                          (assoc :id id)
                          (prepare-student-data))]
    (db/update-student! prepared-data)
    (db/update-course-stat-class! {:user_id user-id :class_id class-id})))

(defn assign-student-to-class
  ([student-id class-id]
   (assign-student-to-class student-id class-id {}))
  ([student-id class-id {:keys [batched?]
                         :or   {batched? false}}]
   (let [student (-> (get-student student-id)
                     (assoc :class-id class-id))]
     (update-student! student-id student)
     (when-not batched?
       (e/dispatch {:type :students/assigned-to-class :class-id class-id})
       {:class (get-class class-id)}))))

(defn assign-students-to-class
  [students-ids class-id]
  (doseq [student-id students-ids]
    (assign-student-to-class student-id class-id {:batched? true}))
  (e/dispatch {:type :students/assigned-to-class :class-id class-id})
  {:class (get-class class-id)})

(defn update-student-access-code!
  [id data]
  (let [prepared-data (-> (db/transform-keys-one-level ->snake_case_keyword data)
                          (assoc :id id))]
    (db/update-student-access-code! prepared-data)))

(defn unassign-student!
  [id]
  (let [{:keys [class-id user-id]} (db/get-student {:id id})]
    (db/unassign-student! {:id id})
    (db/unassign-course-stat! {:user_id user-id})
    (when class-id
      (e/dispatch {:type :students/removed-from-class :student-id id :class-id class-id})))
  [true {:id id}])

(defn delete-student!
  [id]
  (let [{user-id :user-id} (db/get-student {:id id})]
    (db/delete-student! {:user_id user-id})
    (db/delete-course-stat! {:user_id user-id}))
  [true {:id id}])

(defn get-current-school [] (db/get-first-school))

(defn generate-code [] (apply str (take 4 (repeatedly #(rand-int 10)))))

(defn code-unique? [school-id code]
  (-> (db/access-code-exists? {:school_id school-id :access_code code}) :result not))

(defn next-code [school-id]
  (loop [i 0
         code (generate-code)]
    (cond
      (code-unique? school-id code) [true {:school-id school-id :access-code code}]
      (> i 100) [false {:errors {:form "Unable to create code"}}]
      :else (recur (inc i) (generate-code)))))

(defn teachers-by-school
  [school-id]
  (let [teachers (db/teachers-by-school {:school_id school-id})]
    (map with-user teachers)))

(defn teachers-by-class
  [class-id]
  (let [teachers (db/teachers-by-class {:class_id class-id})]
    (map with-user teachers)))

(defn assign-teacher-to-class
  ([teacher-id class-id]
   (assign-teacher-to-class teacher-id class-id {}))
  ([teacher-id class-id {:keys [batched?]
                         :or   {batched? false}}]
   (db/assign-teacher-to-class! {:class_id   class-id
                                 :teacher_id teacher-id})
   (when-not batched?
     (e/dispatch {:type :teachers/assigned-to-class :class-id class-id})
     {:class (get-class class-id)})))

(defn get-teacher
  [teacher-id]
  (-> (db/get-teacher {:id teacher-id})
      (with-user)))

(defn remove-teacher-from-class
  [teacher-id class-id]
  (db/remove-teacher-from-class! {:class_id   class-id
                                  :teacher_id teacher-id})
  (e/dispatch {:type :teachers/removed-from-class :class-id class-id})
  (get-teacher teacher-id))

(defn assign-teachers-to-class
  [teachers-ids class-id]
  (doseq [teacher-id teachers-ids]
    (assign-teacher-to-class teacher-id class-id {:batched? true}))
  (e/dispatch {:type :teachers/assigned-to-class :class-id class-id})
  {:class (get-class class-id)})

(defn create-teacher!
  [data]
  (let [prepared-data (db/transform-keys-one-level ->snake_case_keyword data)
        [{id :id}] (db/create-teacher! prepared-data)]
    (e/dispatch {:type :teachers/created :school-id (:school-id data) :teacher-id id})
    {:id id}))

(defn edit-teacher!
  [teacher-id data]
  (let [prepared-data (db/transform-keys-one-level ->snake_case_keyword data)]
    (db/edit-teacher! (assoc prepared-data :id teacher-id))
    {:id   teacher-id
     :data (get-teacher teacher-id)}))

(defn archive-class!
  [class-id]
  (db/archive-class! {:id class-id})
  (db/remove-teachers-from-class! {:class-id class-id})
  (db/remove-students-from-class! {:class-id class-id})
  (e/dispatch {:type :classes/archived :class-id class-id})
  (db/get-class {:id class-id}))

(defn archive-teacher!
  [teacher-id]
  (let [classes (db/classes-by-teacher {:teacher_id teacher-id})]
    (db/remove-teacher-from-classes! {:teacher_id teacher-id})
    (doseq [{class-id :id} classes]
      (e/dispatch {:type :teachers/removed-from-class :teacher-id teacher-id :class-id class-id})))
  (db/archive-teacher! {:id teacher-id})
  (e/dispatch {:type :teachers/archived :teacher-id teacher-id})
  (db/get-teacher {:id teacher-id}))

(defn archive-student!
  [student-id]
  (let [{class-id :class-id} (db/get-student {:id student-id})]
    (db/unassign-student! {:id student-id})
    (when class-id
      (e/dispatch {:type :students/removed-from-class :student-id student-id :class-id class-id})))
  (db/archive-student! {:id student-id})
  (e/dispatch {:type :students/archived :student-id student-id})
  (db/get-student {:id student-id}))
