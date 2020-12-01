(ns webchange.fixtures.course-table.lesson-sets)

(def data {:items       [{:id         2882
                          :name       "a"
                          :dataset-id 92
                          :data       {}}
                         {:id         2883
                          :name       "b"
                          :dataset-id 92
                          :data       {}}
                         {:id         2884
                          :name       "c"
                          :dataset-id 92
                          :data       {}}]
           :lesson-sets [{:id         382
                          :name       "ls1"
                          :dataset-id 92
                          :data       {:items [{:id 2882}
                                               {:id 2883}]}}
                         {:id         382
                          :name       "ls2"
                          :dataset-id 92
                          :data       {:items [{:id 2883}
                                               {:id 2884}]}}
                         {:id         382
                          :name       "assessment1"
                          :dataset-id 92
                          :data       {:items [{:id 2882}
                                               {:id 2883}
                                               {:id 2884}]}}]})
