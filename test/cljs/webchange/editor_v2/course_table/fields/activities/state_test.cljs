(ns webchange.editor-v2.course-table.fields.activities.state-test
  (:require [cljs.test :refer-macros [deftest testing is use-fixtures]]
            [re-frame.core :as re-frame]
            [day8.re-frame.test :refer-macros [run-test-async wait-for]]
            [webchange.events :as events]
            [webchange.interpreter.core :as ic]
            [webchange.interpreter.events :as ie]
            [webchange.fixtures :as fixtures]
            [webchange.editor-v2.course-table.state.data :as data-state]

            [webchange.editor-v2.course-table.fields.activities.state :as activities-state]
            [webchange.editor-v2.course-table.state.edit-common :as common]
            [webchange.subs :as subs]
            [webchange.editor-v2.course-table.state.selection :as selection-state]
            [webchange.warehouse :as warehouse]))

(use-fixtures :once
              {:before (fn []
                         (doto ic/http-buffer
                           (swap! assoc (ic/course-url "table-course") (fixtures/get-course "table-course"))
                           (swap! assoc (ic/scene-url "table-course" "scene-1") (fixtures/get-scene "table-course" "scene-1"))
                           (swap! assoc (ic/scene-url "table-course" "scene-2") (fixtures/get-scene "table-course" "scene-2"))
                           (swap! assoc (ic/lessons-url "table-course") (fixtures/get-lesson-sets "table-course"))))})

(use-fixtures :each
              {:before (fn []
                         (re-frame/dispatch-sync [::events/initialize-db]))})

(defn- stub-create-activity-placeholder
  [activity]
  (re-frame/reg-event-fx
    ::warehouse/create-activity-placeholder
    (fn [{:keys [db]} [_ _ {:keys [on-success]}]]
      {:dispatch (conj on-success activity)})))

(defn stub-update-course
  []
  (re-frame/reg-event-fx
    ::common/update-course
    (fn [{:keys [_]} [_ course-id course-data]]
      {:dispatch [::common/update-course-success {:data course-data}]})))

(deftest create-activity
  (run-test-async
    (re-frame/dispatch [::data-state/init "table-course"])
    (wait-for [::ie/set-course-lessons-data]
              (re-frame/dispatch [::selection-state/set-selection {:level-idx 0, :lesson-idx 0, :activity-idx 0, :field :activity}])
              (re-frame/dispatch [::activities-state/init {:activity "scene-1"} 0])
              (wait-for [::activities-state/reset-current-activity]
                        (let [component-id 0
                              activity-name "My New Activity"
                              activity-slug "my-new-activity"]
                          (stub-create-activity-placeholder {:name activity-name :scene-slug activity-slug})
                          (stub-update-course)
                          (re-frame/dispatch [::activities-state/create activity-name component-id])
                          (wait-for [::ie/set-course-data]
                                    (let [data @(re-frame/subscribe [::subs/course-data])
                                          scene-list (get data :scene-list)]
                                      (testing "activity added to scene-list"
                                        (is (some #(= activity-name (:name %)) (vals scene-list)))))))))))
