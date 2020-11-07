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

(def skills-tags {:alliteration                   "alliteration"
                  :answering-questions            "answering questions"
                  :antonyms                       "antonyms"
                  :asking-questions               "asking questions"
                  :blending                       "blending"
                  :cause-and-effect               "cause and effect"
                  :combining-phones-to-form-words "combining phones to form words"
                  :comprehension                  "comprehension"
                  :concepts-of-print              "concepts of print"
                  :context-clues                  "context clues"
                  :conversation-skills            "conversation skills"
                  :elements-of-a-story            "elements of a story"
                  :express-ideas-verbally         "express ideas verbally"
                  :following-directions           "following directions"
                  :future-tense                   "future tense"
                  :illustrations                  "illustrations"
                  :inferencing                    "inferencing"
                  :informative-writing            "informative writing"
                  :letter-formation               "letter formation"
                  :letter-names                   "letter names"
                  :letter-sounds                  "letter sounds"
                  :listening-comprehension        "listening comprehension"
                  :listening-skills               "listening skills"
                  :main-idea-and-details          "main idea and details"
                  :making-predictions             "making predictions"
                  :onset-and-rime                 "onset and rime"
                  :opinion-writing                "opinion writing"
                  :past-tense                     "past tense"
                  :purpose-for-literacy           "purpose for literacy"
                  :reading-comprehension          "reading comprehension"
                  :rhyming                        "rhyming"
                  :sentence-structure             "sentence structure"
                  :sentence-types                 "sentence types"
                  :sequencing                     "sequencing"
                  :sight-words                    "sight words"
                  :summarizing                    "summarizing"
                  :syllables                      "syllables"
                  :synonyms                       "synonyms"
                  :vocabulary                     "vocabulary"
                  :word-categories                "word categories"
                  :word-relationships             "word relationships"})

(def skills-data {:foundational-literacy
                  {:name   "Foundational Literacy"
                   :topics {:alphabet-knowledge
                            {:name   "Alphabet Knowledge"
                             :skills [{:id    1
                                       :name  "Identify letters and associate correct sounds with letters, including a minimum of ten letters, preferably including letters in the child’s name."
                                       :abbr  "CO.RW.P.2.4.d"
                                       :grade :pre-k
                                       :tags  [:letter-names]}]}
                            :communication
                            {:name   "Communication"
                             :skills [{:id    2
                                       :name  "Demonstrate understanding of a variety of question types, such as \"Yes/No?\" or \"Who/What/When/Where?\" or \"How/ Why?\""
                                       :abbr  "Headstart.Goal P-LC 2.b"
                                       :grade :pre-k
                                       :tags  [:asking-questions]}]}
                            :conventions-of-print
                            {:name   "Conventions of Print"
                             :skills [{:id    3
                                       :name  "Understand that written words are made up of a group of individual letters."
                                       :abbr  "Headstart.Goal P-LIT 2.b"
                                       :grade :pre-k
                                       :tags  [:combining-phones-to-form-words]}]}
                            :phonemic-awareness
                            {:name   "Phonemic Awareness"
                             :skills [{:id    4
                                       :name  "Produce the beginning sound in a spoken word, such as \"Dog begins with /d/.\""
                                       :abbr  "Headstart.Goal P-LIT 1.b"
                                       :grade :pre-k
                                       :tags  [:letter-sounds]}
                                      {:id    5
                                       :name  "Know the sounds associated with several letters."
                                       :abbr  "Headstart.Goal P-LIT 3.b"
                                       :grade :pre-k
                                       :tags  [:letter-sounds]}
                                      {:id    6
                                       :name  "Begins to recognize individual syllables within spoken words (e.g., cup-cake, base-ball)"
                                       :abbr  "NY.PK.ELAL.2.b"
                                       :grade :pre-k
                                       :tags  [:syllables]}
                                      {:id    7
                                       :name  "Orally blend the onsets, rimes, and phonemes of words and orally delete the onsets of words, with the support of pictures or objects.*"
                                       :abbr  "CA.R.2.2"
                                       :grade :pre-k
                                       :tags  [:onset-and-rime :blending]}]}
                            :phonological-awareness
                            {:name   "Phonological Awareness"
                             :skills [{:id    8
                                       :name  "Provide one or more words that rhyme with a single given target, such as \"What rhymes with log?\""
                                       :abbr  "Headstart.Goal P-LIT 1.a"
                                       :grade :pre-k
                                       :tags  [:rhyming]}
                                      {:id    9
                                       :name  "Provide a word that fits with a group of words sharing an initial sound, with adult support, such as \"Sock, Sara, and song all start with the /s/ sound. What else starts with the /s/ sound?\""
                                       :abbr  "Headstart.Goal P-LIT 1.c"
                                       :grade :pre-k
                                       :tags  [:alliteration]}]}}}
                  :listening
                  {:name   "Listening"
                   :topics {:communication
                            {:name   "Communication"
                             :skills [{:id    10
                                       :name  "Show an understanding of talk related to the past or future."
                                       :abbr  "Headstart.Goal P-LC 2.d"
                                       :grade :pre-k
                                       :tags  [:past-tense :future-tense :listening-comprehension]}]}
                            :listening-skills
                            {:name   "Listening Skills"
                             :skills [{:id    11
                                       :name  "Show an ability to recall (in order) multiple step directions."
                                       :abbr  "Headstart.Goal P-LC 2.a"
                                       :grade :pre-k
                                       :tags  [:following-directions :listening-comprehension]}]}
                            :phonemic-awareness
                            {:name   "Phonemic Awareness"
                             :skills [{:id    12
                                       :name  "Recognize patterns of sounds in songs, storytelling, and poetry through interactions and meaningful experiences."
                                       :abbr  "CO.RW.P.2.2.d"
                                       :grade :pre-k
                                       :tags  [:listening-comprehension :rhyming]}]}
                            :reading-comprehension
                            {:name   "Reading Comprehension"
                             :skills [{:id    13
                                       :name  "Identify characters and main events in books and stories."
                                       :abbr  "Headstart.Goal P-LIT 4.c"
                                       :grade :pre-k
                                       :tags  [:reading-comprehension :elements-of-a-story :listening-comprehension]}
                                      {:id    14
                                       :name  "Re-tell or act out a story that was read, putting events in the appropriate sequence, and demonstrating more sophisticated understanding of how events relate, such as cause and effect relationships."
                                       :abbr  "Headstart.Goal P-LIT 4.a"
                                       :grade :pre-k
                                       :tags  [:reading-comprehension :listening-comprehension :sequencing :cause-and-effect]}
                                      {:id    15
                                       :name  "Answer questions about details of a story with increasingly specific information, such as when asked \"Who was Mary?\" responds \"She was the girl who was riding the horse and then got hurt.\""
                                       :abbr  "Headstart.Goal P-LIT 5.a"
                                       :grade :pre-k
                                       :tags  [:reading-comprehension :listening-comprehension :answering-questions]}
                                      {:id    16
                                       :name  "Answer increasingly complex inferential questions that require making predictions based on multiple pieces of information from the story; inferring characters' feelings or intentions; or providing evaluations of judgments that are grounded in the text."
                                       :abbr  "Headstart.Goal P-LIT 5.b"
                                       :grade :pre-k
                                       :tags  [:reading-comprehension :listening-comprehension :making-predictions :inferencing]}
                                      {:id    17
                                       :name  "Provide a summary of a story, highlighting a number of the key ideas in the story and how they relate."
                                       :abbr  "Headstart.Goal P-LIT 5.c"
                                       :grade :pre-k
                                       :tags  [:reading-comprehension :listening-comprehension :summarizing :main-idea-and-details]}]}}}
                  :reading
                  {:name   "Reading"
                   :topics {:functions-of-print
                            {:name   "Functions of Print"
                             :skills [{:id    18
                                       :name  "Recognize print in everyday life, such as numbers, letters, one’s name, words, and familiar logos and signs."
                                       :abbr  "CO.RW.P.2.3.a"
                                       :grade :pre-k
                                       :tags  [:purpose-for-literacy]}
                                      {:id    19
                                       :name  "Understand that print conveys meaning."
                                       :abbr  "CO.RW.P.2.3.b"
                                       :grade :pre-k
                                       :tags  [:purpose-for-literacy]}
                                      {:id    20
                                       :name  "Understand that print is organized differently for different purposes, such as a note, list, or storybook."
                                       :abbr  "Headstart.Goal P-LIT 2.a"
                                       :grade :pre-k
                                       :tags  [:purpose-for-literacy]}]}
                            :print-concepts
                            {:name   "Print Concepts"
                             :skills [{:id    21
                                       :name  "Recognize how books are read, such as front-to-back, left to right, and one page at a time, and recognize basic characteristics, such as title, author, and illustrator."
                                       :abbr  "CO.RW.P.2.1.b and Headstart.Goal P-LIT 2.d"
                                       :grade :pre-k
                                       :tags  [:concepts-of-print]}]}
                            :reading-comprehension
                            {:name   "Reading Comprehension"
                             :skills [{:id    22
                                       :name  "Describes the relationship between illustrations and the text (e.g., what person, place, thing or idea in the text an illustration depicts)"
                                       :abbr  "NY.PK.ELAL.11 "
                                       :grade :pre-k
                                       :tags  [:illustrations]}]}
                            :vocabulary
                            {:name   "Vocabulary"
                             :skills [{:id    23
                                       :name  "Shows recognition of and/or familiarity with key domain-specific words heard during reading or discussions."
                                       :abbr  "Headstart.Goal P-LC 6.b"
                                       :grade :pre-k
                                       :tags  [:vocabulary :comprehension]}]}
                            :word-recognition
                            {:name   "Word Recognition"
                             :skills [{:id    24
                                       :name  "Recognize own name in print"
                                       :abbr  "CA.R.3.1"
                                       :grade :pre-k
                                       :tags  [:sight-words]}]}}}
                  :speaking
                  {:name   "Speaking"
                   :topics {:oral-language-skills
                            {:name   "Oral Language Skills"
                             :skills [{:id    25
                                       :name  "Repeats simple familiar rhymes or sings favorite songs."
                                       :abbr  "Headstart.Goal IT-LC 9.a"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally]}
                                      {:id    26
                                       :name  "Typically, use complete sentences of more than five words with complex structures, such as sentences involving sequence and causal relations."
                                       :abbr  "Headstart.Goal P-LC 5.b"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally :sentence-structure]}
                                      {:id    27
                                       :name  "Tell fictional or personal stories using a sequence of at least two or three connected events."
                                       :abbr  "Headstart.Goal P-LIT 4.b"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally :sequencing]}
                                      {:id    28
                                       :name  "Can produce and organize multiple sentences on a topic, such as giving directions or telling a story, including information about the past or present or things not physically present, and answer a variety of question types."
                                       :abbr  "Headstart.Goal P-LC 5.c"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally :sequencing]}
                                      {:id    29
                                       :name  "Maintain multi-turn conversations with adults, other children, and within larger groups by responding in increasingly sophisticated ways, such as asking related questions or expressing agreement."
                                       :abbr  "Headstart.Goal P-LC 4.a"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally :conversation-skills :listening-skills]}
                                      {:id    30
                                       :name  "With increasing independence, match the tone and volume of expression to the content and social situation, such as by using a whisper to tell a secret."
                                       :abbr  "Headstart.Goal P-LC 4.b"
                                       :grade :pre-k
                                       :tags  [:express-ideas-verbally :conversation-skills :listening-skills]}]}
                            :vocabulary
                            {:name   "Vocabulary"
                             :skills [{:id    31
                                       :name  "With multiple exposures, use new domain-specific vocabulary during activities, such as using the word \"cocoon\" when learning about the life-cycle of caterpillars, or \"cylinder\" when learning about 3-D shapes."
                                       :abbr  "Headstart.Goal P-LC 6.c"
                                       :grade :pre-k
                                       :tags  [:vocabulary :express-ideas-verbally]}]}}}
                  :writing
                  {:name   "Writing"
                   :topics {:illustrative-expression
                            {:name   "Illustrative Expression"
                             :skills [{:id    32
                                       :name  "Create a variety of written products that may or may not phonetically relate to intended messages."
                                       :abbr  "Headstart.Goal P-LIT 6.a"
                                       :grade :pre-k
                                       :tags  [:informative-writing :opinion-writing :illustrations]}]}
                            :letter-formation
                            {:name   "Letter Formation"
                             :skills [{:id    33
                                       :name  "Show an interest in copying simple words posted in the classroom."
                                       :abbr  "Headstart.Goal P-LIT 6.b"
                                       :grade :pre-k
                                       :tags  [:letter-formation]}
                                      {:id    34
                                       :name  "Attempt to independently write some words using invented spelling, such as K for kite."
                                       :abbr  "Headstart.Goal P-LIT 6.c"
                                       :grade :pre-k
                                       :tags  [:letter-formation]}
                                      {:id    35
                                       :name  "Write first name correctly or close to correctly."
                                       :abbr  "Headstart.Goal P-LIT 6.d"
                                       :grade :pre-k
                                       :tags  [:letter-formation]}]}}}
                  :language-usage
                  {:name   "Language Usage"
                   :topics {:vocabulary
                            {:name   "Vocabulary"
                             :skills [{:id    36
                                       :name  "Categorize words or objects, such as sorting a hard hat, machines, and tools into the construction group, or giving many examples of farm animals."
                                       :abbr  "Headstart.Goal P-LC 7.a"
                                       :grade :pre-k
                                       :tags  [:word-categories]}
                                      {:id    37
                                       :name  "Identifies key common antonyms, such as black/white or up/down. Identifies one or two synonyms for very familiar words, such as glad or happy."
                                       :abbr  "Headstart.Goal P-LC 7.d"
                                       :grade :pre-k
                                       :tags  [:word-relationships :synonyms :antonyms]}
                                      {:id    38
                                       :name  "With support, form guesses about the meaning of new words from context clues."
                                       :abbr  "Headstart.Goal P-LC 6.d"
                                       :grade :pre-k
                                       :tags  [:context-clues]}
                                      {:id    39
                                       :name  "Identify shared characteristics among people, places, things, or actions, such as identifying that both cats and dogs are furry and have four legs."
                                       :abbr  "Headstart.Goal P-LC 7.c"
                                       :grade :pre-k
                                       :tags  [:word-relationships]}]}
                            :writing-mechanics
                            {:name   "Writing Mechanics"
                             :skills [{:id    40
                                       :name  "Show understanding of a variety of sentence types, such as multi-clause, cause-effect, sequential order, or if-then."
                                       :abbr  "Headstart.Goal P-LC 2.c"
                                       :grade :pre-k
                                       :tags  [:sentence-types]}]}}}})

(defn- get-topic-key
  [strand-key topic-key]
  (-> (str (clojure.core/name strand-key)
           "--"
           (clojure.core/name topic-key))
      (keyword)))

(def strands (->> skills-data
                  (map (fn [[strand-key {:keys [name]}]] [strand-key name]))
                  (into {})))

(def topics (->> skills-data
                 (map (fn [[strand-key {:keys [topics]}]]
                        (->> topics
                             (map (fn [[topic-key {:keys [name]}]]
                                    [(get-topic-key strand-key topic-key) {:name   name
                                                                           :strand strand-key}]))
                             (into {}))))
                 (remove empty?)
                 (apply merge)))

(def skills (->> skills-data
                 (map (fn [[strand-key {:keys [topics]}]]
                        (->> topics
                             (map (fn [[topic-key {:keys [skills]}]]
                                    (->> skills
                                         (map (fn [{:keys [grade tags] :as skill}]
                                                (-> skill
                                                    (assoc :grade (get levels grade))
                                                    (assoc :tags (map #(get skills-tags %) tags))
                                                    (assoc :topic (get-topic-key strand-key topic-key)))))))))))
                 (flatten)))

(defn get-skills
  []
  {:levels   levels
   :subjects subjects
   :skills   skills
   :topics   topics
   :strands  strands})
