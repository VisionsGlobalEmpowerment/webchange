(ns webchange.fixtures.course-table.course)

(def data {:scene-list {:scene-1 {:name "scene-1"}
                        :scene-2 {:name "scene-2"}}
           :levels     [{:name    "Level 1",
                         :level   1,
                         :scheme  {:lesson     {:lesson-sets ["concepts"]},
                                   :assessment {:lesson-sets ["assessment-1"]}}
                         :lessons [{:name        "Lesson 1",
                                    :type        "lesson",
                                    :lesson      1,
                                    :comment     "Lesson 1 comment"
                                    :activities  [{:activity "scene-1", :time-expected 300}
                                                  {:activity      "scene-2", :time-expected 300,
                                                   :tags-by-score {:intermediate [0 75],
                                                                   :advanced     [75 101]}}],
                                    :lesson-sets {:concepts "ls1"}}]}
                        {:name    "Level 2",
                         :level   2,
                         :scheme  {:lesson     {:lesson-sets ["current-concept" "all-concepts"]},
                                   :assessment {:lesson-sets ["assessment-1"]}}
                         :lessons [{:name        "Lesson 2",
                                    :type        "lesson",
                                    :lesson      2,
                                    :activities  [{:activity "scene-1", :time-expected 300, :only [:beginner]}
                                                  {:activity "scene-2", :time-expected 300}],
                                    :lesson-sets {:current-concept "ls2"}}
                                   {:name        "Assessment",
                                    :type        "assessment",
                                    :lesson      3,
                                    :activities  [{:activity "scene-1", :time-expected 300}],
                                    :lesson-sets {:assessment-1 "assessment1"}}]}]})
