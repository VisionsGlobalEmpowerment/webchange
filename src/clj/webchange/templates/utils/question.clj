(ns webchange.templates.utils.question
  (:require
    [webchange.utils.text :as text-utils]))

(def empty-audio {:audio "" :start 0 :duration 0 :animation "color" :fill 0x00B2FF :data []})

(defn- create-answer
  [{:keys [text checked img]}]
  (cond-> {:correct    (if checked true false)
           :audio-data empty-audio}
          img (assoc :image img)
          text (assoc :text text :chunks (text-utils/text->chunks text))))

(defn get-assets
  [{:keys [img answers] :as args}]
  (let [image-from-question (if img
                              [{:url img, :size 10, :type "image"}] [])]
   (vec (reduce (fn [result {:keys [img]}]
              (if img (conj result {:url img, :size 10, :type "image"}) result))
            image-from-question
            answers))))

(defn create
  [{:keys [question-type question question-screenshot answers img] :as args} {:keys [suffix action-name next-action-name]}]
  (let [action-name (or action-name (str "question" "-" suffix))
        success (str action-name "-correct-answer")
        success-dialog (str action-name "-correct-answer-dialog")
        fail-answer-dialog (str action-name "-fail-answer-dialog")]
    {(keyword action-name)        {:type        "show-question"
                                   :description question
                                   :continue-flow (nil? next-action-name)
                                   :data        {:type        (if question-type question-type "type-1")
                                                 :text        question
                                                 :chunks      (text-utils/text->chunks question)
                                                 :success     success
                                                 :fail        fail-answer-dialog
                                                 :audio-data  empty-audio
                                                 :image       img
                                                 :screenshot? question-screenshot
                                                 :answers     {:data (map create-answer answers)}}}

     (keyword success)            {:type "sequence-data",
                                   :data [{:type "empty" :duration 500}
                                          {:type "action" :id success-dialog}
                                          {:type "hide-question"}
                                          {:type "empty" :duration 2000}
                                          (if next-action-name
                                            {:type "action" :id next-action-name}
                                            {:type "empty" :duration 100})]}

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
  (let [actions (create args {:suffix           unique-suffix
                              :action-name      old-action-name
                              :next-action-name new-action-name})
        assets (get-assets args)]
    [(keyword old-action-name) actions assets]))
