(ns webchange.templates.utils.question
  (:require
    [webchange.utils.text :as text-utils]))

(defn create
  [args {:keys [old-action-name new-action-name unique-suffix]}]
  (let [
        success (str "correct-answer-question" "-" unique-suffix)
        success-dialog (str "correct-answer-dialog" "-" unique-suffix)
        fail-answer-dialog (str "fail-answer-dialog" "-" unique-suffix)
        fail (str "fail-answer-question" "-" unique-suffix)
        skip (str "skip-question" "-" unique-suffix)]
    [(keyword old-action-name)
     {(keyword old-action-name)    {
                                    :type        "show-question"
                                    :description (get-in args [:question])
                                    :data        {
                                                  :type       "type-1"
                                                  :text       (get-in args [:question])
                                                  :chunks     (text-utils/text->chunks (get-in args [:question]))
                                                  :success    success
                                                  :fail       fail
                                                  :skip       skip
                                                  :audio-data {
                                                               :audio     ""
                                                               :start     0,
                                                               :duration  0,
                                                               :animation "color",
                                                               :fill      0x00B2FF
                                                               :data      []}
                                                  :image      (get-in args [:img])
                                                  :answers    {:data (map (fn [{:keys [text checked]}]
                                                                            {:text       text
                                                                             :correct    (if checked true false)
                                                                             :chunks     (text-utils/text->chunks text)
                                                                             :audio-data {
                                                                                          :audio     ""
                                                                                          :start     0,
                                                                                          :duration  0,
                                                                                          :animation "color",
                                                                                          :fill      0x00B2FF
                                                                                          :data      []}
                                                                             }
                                                                            ) (get-in args [:answers]))}
                                                  }
                                    }
      (keyword fail)               {:type "sequence-data",
                                    :data [{:type "action" :id fail-answer-dialog}]}
      (keyword success)            {:type "sequence-data",
                                    :data [
                                           {:type "empty" :duration 500}
                                           {:type "hide-question"}
                                           {:type "action" :id success-dialog}
                                           {:type "action" :id new-action-name}
                                           ],
                                    }
      (keyword skip)               {:type "sequence-data",
                                    :data [
                                           {:type "hide-question"}
                                           {:type "action" :id new-action-name}
                                           ],
                                    }
      (keyword success-dialog)     {:type               "sequence-data",
                                    :data               [{:type "sequence-data"
                                                          :data [{:type "empty" :duration 0}
                                                                 {:type "animation-sequence", :phrase-text "success dialog", :audio nil}]}],
                                    :phrase-description "success dialog"}
      (keyword fail-answer-dialog) {:type               "sequence-data",
                                    :data               [{:type "sequence-data"
                                                          :data [{:type "empty" :duration 0}
                                                                 {:type "animation-sequence", :phrase-text "fail answer dialog", :audio nil}]}],
                                    :phrase-description "fail answer dialog"}}
     (if (get-in args [:img])
       [{:url (get-in args [:img]), :size 10, :type "image"}]
       [])
     ]))