(ns webchange.templates.utils.question
  (:require
    [webchange.utils.text :as text-utils]))

(def empty-audio {:audio "" :start 0 :duration 0 :animation "color" :fill 0x00B2FF :data []})

(defn- create-answer
  [{:keys [text checked]}]
  {:text       text
   :correct    (if checked true false)
   :chunks     (text-utils/text->chunks text)
   :audio-data empty-audio})

(defn get-assets
  [args]
  (if (get-in args [:img])
    [{:url (get-in args [:img]), :size 10, :type "image"}]
    []))

(defn create
  [args {:keys [suffix action-name next-action-name]}]
  (let [action-name (or action-name (str "question" "-" suffix))
        success (str action-name "-correct-answer")
        success-dialog (str action-name "-correct-answer-dialog")
        fail-answer-dialog (str action-name "-fail-answer-dialog")
        skip (str action-name "-skip-question")]
    {(keyword action-name)        {:type        "show-question"
                                   :description (get-in args [:question])
                                   :data        {:type       "type-1"
                                                 :text       (get-in args [:question])
                                                 :chunks     (text-utils/text->chunks (get-in args [:question]))
                                                 :success    success
                                                 :fail       fail-answer-dialog
                                                 :skip       skip
                                                 :audio-data empty-audio
                                                 :image      (get-in args [:img])
                                                 :answers    {:data (map create-answer (get-in args [:answers]))}}}

     (keyword success)            {:type "sequence-data",
                                   :data [{:type "empty" :duration 500}
                                          {:type "hide-question"}
                                          {:type "action" :id success-dialog}
                                          {:type "action" :id next-action-name}]}

     (keyword skip)               {:type "sequence-data",
                                   :data [{:type "hide-question"}
                                          {:type "action" :id next-action-name}]}

     (keyword success-dialog)     {:type               "sequence-data",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "success dialog", :audio nil}]}],
                                   :phrase-description "success dialog"}

     (keyword fail-answer-dialog) {:type               "sequence-data",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "fail answer dialog", :audio nil}]}],
                                   :phrase-description "fail answer dialog"}}))

(defn create-and-place-before
  [args {:keys [old-action-name new-action-name unique-suffix]}]
  (let [actions (create args {:suffix unique-suffix
                              :action-name old-action-name
                              :next-action-name new-action-name})
        assets (get-assets args)]
    [(keyword old-action-name) actions assets]))
