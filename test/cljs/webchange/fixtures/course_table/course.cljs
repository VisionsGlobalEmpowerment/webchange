(ns webchange.fixtures.course-table.course)

(def data {:scene-list {:home {:name "scene-1"}}
           :levels     [{:name    "Level 1",
                         :level   1,
                         :lessons [{:name        "Lesson 1",
                                    :type        "lesson",
                                    :lesson      1,
                                    :activities  [{:activity "scene-1", :time-expected 300}
                                                  {:activity "scene-2", :time-expected 300}],
                                    :lesson-sets {:concepts "ls1"}}]}
                        {:name    "Level 2",
                         :level   2,
                         :lessons [{:name        "Lesson 1",
                                    :type        "lesson",
                                    :lesson      2,
                                    :activities  [{:activity "scene-1", :time-expected 300}
                                                  {:activity "scene-2", :time-expected 300}],
                                    :lesson-sets {:concepts "ls2"}}]}]})
