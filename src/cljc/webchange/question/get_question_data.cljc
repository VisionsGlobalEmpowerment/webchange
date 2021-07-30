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

(defn form->question-data
  [{:keys [alias answers-number correct-answers layout option-label options-number task-type]}]
  {:alias           (or alias "New question")
   :question-type   "multiple-choice-image"
   :layout          (or layout "horizontal")
   :task            {:type task-type
                     :text default-task-text
                     :img  "/images/questions/question.png"}
   :options         {:label option-label
                     :data  (take options-number default-options)}
   :answers-number  answers-number
   :correct-answers (->> correct-answers
                         (map #(nth default-options %))
                         (map :value)
                         (doall))})
