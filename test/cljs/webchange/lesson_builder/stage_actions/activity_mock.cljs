(ns webchange.lesson-builder.stage-actions.activity-mock)

(def data {:assets        []
           :objects       {:background {:type "background"
                                        :src  "/raw/img/park/slide/background2.jpg"}}
           :scene-objects [["background"]]
           :actions       {:intro {:type "sequence-data"
                                   :data [{:type "sequence-data"
                                           :data [{:type     "empty"
                                                   :duration 1000}
                                                  {:type   "animation-sequence"
                                                   :target "guide"}]}]}}
           :triggers      {}
           :metadata      {}})
