(ns webchange.question.get-question-data)

(def default-task-text "Question placeholder")

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

(def available-mark-options [{:value "thumbs-up"
                              :text  "Thumbs Up"
                              :img   "/images/questions/thumbs_up.png"}
                             {:value "ok"
                              :text  "Ok"
                              :img   "/images/questions/ok.png"}
                             {:value "thumbs-down"
                              :text  "Thumbs Down"
                              :img   "/images/questions/thumbs_down.png"}])

(defn form->question-data
  [{:keys [alias answers-number correct-answers layout mark-options option-label options-number task-type question-type]
    :or   {alias          "New question"
           layout         "horizontal"
           options-number 2
           question-type  "multiple-choice-text"}}]
  (let [marks-question? (= question-type "thumbs-up-n-down")]
    {:alias           alias
     :question-type   question-type
     :layout          layout
     :task            {:type task-type
                       :text default-task-text
                       :img  "/images/questions/question.png"}
     :options         {:label option-label
                       :data  (if marks-question?
                                (filter (fn [{:keys [value]}]
                                          (some #{value} mark-options))
                                        available-mark-options)
                                (take options-number default-options))}
     :answers-number  answers-number
     :correct-answers correct-answers}))
