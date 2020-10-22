(ns webchange.course.skills
  (:require
    [camel-snake-kebab.core :refer [->kebab-case]]))

(def levels {:pre-k        "Pre-K"
             :kindergraten "Kindergraten"
             :grade-1      "Grade 1"
             :grade-2      "Grade 2"})

(def subjects {:english-native "English Literacy for Native English Speakers"
               :english-second "English Literacy for Second Language Speakers"
               :spanish-native "Spanish Literacy for Native Spanish Speakers"
               :spanish-second "Spanish Literacy for Second Language Speakers"})

(def strands {:foundational-literacy "Foundational Literacy"
              :listening             "Listening"
              :reading               "Reading"
              :speaking              "Speaking"
              :writing               "Writing"})

(def topics {:phonemic-awareness                                           {:name   "Phonemic Awareness"
                                                                            :strand :foundational-literacy}
             :punctuation                                                  {:name   "Punctuation"
                                                                            :strand :foundational-literacy}
             :listening-skills                                             {:name   "Listening Skills"
                                                                            :strand :listening}
             :phonemic-awareness-2                                         {:name   "Phonemic Awareness"
                                                                            :strand :listening}
             :vocabulary                                                   {:name   "Vocabulary"
                                                                            :strand :listening}
             :print-concepts                                               {:name   "Print Concepts"
                                                                            :strand :reading}
             :print-conveys-meaning                                        {:name   "Print Conveys Meaning"
                                                                            :strand :reading}
             :reading-stories-other-literature                             {:name   "Reading Stories & Other Literature"
                                                                            :strand :reading}
             :symbol-object-and-letter-recognition-with-accuracy-and-speed {:name   "Symbol, object and letter recognition with accuracy and speed"
                                                                            :strand :reading}
             :oral-language-skills                                         {:name   "Oral Language Skills"
                                                                            :strand :speaking}
             :vocabulary-2                                                 {:name   "Vocabulary"
                                                                            :strand :speaking}
             :illustrative-expression                                      {:name   "Illustrative Expression"
                                                                            :strand :writing}
             :letter-formation                                             {:name   "Letter Formation"
                                                                            :strand :writing}
             :grammar                                                      {:name   "Grammar"
                                                                            :strand :writing}})

(def skills [{:id    1
              :name  "Demonstrate understanding of initial sounds in words (such as mop begins with the /m/ sound)"
              :grade "Pre-K"
              :topic :phonemic-awareness
              :tags  ["letter sounds"]}
             {:id    2
              :name  "Understand that words are made up of one or more syllables"
              :grade "Pre-K"
              :topic :phonemic-awareness
              :tags  ["syllables"]}
             {:id    3
              :name  "Recognize rhyming words and alliterations (matching and producing words to the same beginning sounds)"
              :grade "Pre-K"
              :topic :phonemic-awareness
              :tags  ["rhyming" "alliteration"]}
             {:id    4
              :name  "Listen to onset-rime activities blending beginning sounds with ending sounds to make words (p-ad, br-ick)"
              :grade "Pre-K"
              :topic :phonemic-awareness
              :tags  ["onset and rime" "blending"]}
             {:id    5
              :name  "Understand the difference between a question and a statement"
              :grade "Pre-K"
              :topic :punctuation
              :tags  ["reading comprehension" "asking questions" "research skills" "sentence structure"]}
             {:id    6
              :name  "Remember spoken information for a short period of time"
              :grade "Pre-K"
              :topic :listening-skills
              :tags  ["listening comprehension"]}])

(defn get-skills
  []
  {:levels   levels
   :subjects subjects
   :skills   skills
   :topics   topics
   :strands  strands})
