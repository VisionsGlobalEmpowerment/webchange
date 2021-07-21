(ns webchange.question.get-question-data)

(def default-options [{:img     "/images/questions/option1.png"
                       :text    "Cow"
                       :value   "cow"
                       :correct true}
                      {:img   "/images/questions/option2.png"
                       :text  "Deer"
                       :value "deer"}
                      {:img   "/images/questions/option3.png"
                       :text  "Fox"
                       :value "fox"}
                      {:img   "/images/questions/option4.png"
                       :text  "Skunk"
                       :value "skunk"}])

(defn form->question-data
  [{:keys [options-number]}]
  {:alias                 "Q: Who is the main character?"
   :question-type         "multiple-choice-image"
   :layout                "horizontal"
   :task                  {:type "text-image"
                           :text "Who do you think the main character, or most important character is going to be in this book?"
                           :img  "/images/questions/question.png"}
   :options               {:label "audio-text"
                           :data  (take options-number default-options)}
   :correct-answers-count "one"})

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
