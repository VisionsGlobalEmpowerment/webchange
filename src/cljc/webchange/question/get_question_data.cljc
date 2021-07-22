(ns webchange.question.get-question-data)

(def default-task-text "Who do you think the main character, or most important character is going to be in this book?")

(def default-options [{:img   "/images/questions/option1.png"
                       :text  "Option 1"
                       :value "option-1"}
                      {:img   "/images/questions/option2.png"
                       :text  "Option 2"
                       :value "option-2"}
                      {:img   "/images/questions/option3.png"
                       :text  "Option 3"
                       :value "option-3"}
                      {:img   "/images/questions/option4.png"
                       :text  "Option 4"
                       :value "option-4"}])

(defn form->question-data
  [{:keys [alias answers-number correct-answers options-number task-type]}]
  {:alias           (or alias "New question")
   :question-type   "multiple-choice-image"
   :layout          "horizontal"
   :task            {:type task-type
                     :text default-task-text
                     :img  "/images/questions/question.png"}
   :options         {:label "audio-text"
                     :data  (take options-number default-options)}
   :answers-number  answers-number
   :correct-answers (->> correct-answers
                         (map #(nth default-options %))
                         (map :value)
                         (doall))})

;{:alias                 "Q: Who is the main character?"
;                              :type                  "question"
;
;                              :question-type         "multiple-choice-image"
;                              :layout                "horizontal"
;
;                              :task                  {:type "text-image"
;                                                      :text "Who do you think the main character, or most important character is going to be in this book?"
;                                                      :img  "/images/questions/question.png"}
;
;                              :options               {:label "audio-text"
;                                                      :data  [{:img   "/images/questions/option1.png"
;                                                               :text  "Cow"
;                                                               :value "cow"}
;                                                              {:img     "/images/questions/option2.png"
;                                                               :text    "Deer"
;                                                               :correct true
;                                                               :value   "deer"}
;                                                              {:img   "/images/questions/option3.png"
;                                                               :text  "Fox"
;                                                               :value "fox"}
;                                                              {:img   "/images/questions/option4.png"
;                                                               :text  "Skunk"
;                                                               :value "skunk"}]}
;
;                              :correct-answers-count "one"
;
;                              :editable?             {:show-in-tree? true}}
