(ns webchange.templates.library.categorize.synonyms.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)

(def template (round-1-template/get-template
                {:boxes           [{:position {:x 980 :y 763}
                                    :src      "/raw/img/categorize-synonyms/chilly.png"}
                                   {:position {:x 1274 :y 763}
                                    :src      "/raw/img/categorize-synonyms/kid.png"}
                                   {:position {:x 393 :y 763}
                                    :src      "/raw/img/categorize-synonyms/big.png"}
                                   {:position {:x 1568 :y 763}
                                    :src      "/raw/img/categorize-synonyms/trash.png"}
                                   {:position {:x 686 :y 763}
                                    :src      "/raw/img/categorize-synonyms/afraid.png"}
                                   {:position {:x 99 :y 763}
                                    :src      "/raw/img/categorize-synonyms/glad.png"}]
                 :items           [{:position       {:x 701 :y 89}
                                    :src            "/raw/img/categorize-synonyms/cold.png"
                                    :pick-dialog    {:name   "cold-item"
                                                     :phrase "cold"}
                                    :correct-dialog {:name   "cold-correct"
                                                     :phrase "cold correct"}}
                                   {:position       {:x 863 :y 243}
                                    :src            "/raw/img/categorize-synonyms/child.png"
                                    :pick-dialog    {:name   "child-item"
                                                     :phrase "child"}
                                    :correct-dialog {:name   "child-correct"
                                                     :phrase "child correct"}}
                                   {:position       {:x 448 :y 256}
                                    :src            "/raw/img/categorize-synonyms/large.png"
                                    :pick-dialog    {:name   "large-item"
                                                     :phrase "large"}
                                    :correct-dialog {:name   "large-correct"
                                                     :phrase "large correct"}}
                                   {:position       {:x 1035 :y 383}
                                    :src            "/raw/img/categorize-synonyms/garbage.png"
                                    :pick-dialog    {:name   "garbage-item"
                                                     :phrase "garbage"}
                                    :correct-dialog {:name   "garbage-correct"
                                                     :phrase "garbage correct"}}
                                   {:position       {:x 1121 :y 105}
                                    :src            "/raw/img/categorize-synonyms/scared.png"
                                    :pick-dialog    {:name   "scared-item"
                                                     :phrase "scared"}
                                    :correct-dialog {:name   "scared-correct"
                                                     :phrase "scared correct"}}
                                   {:position       {:x 701 :y 393}
                                    :src            "/raw/img/categorize-synonyms/happy.png"
                                    :pick-dialog    {:name   "happy-item"
                                                     :phrase "happy"}
                                    :correct-dialog {:name   "happy-correct"
                                                     :phrase "happy correct"}}]
                 :background      {:src "/raw/img/categorize-synonyms/background.png"}
                 :generic-dialogs {:intro          {:name               "intro"
                                                    :prompt             "Start dialog"
                                                    :phrase             "intro"
                                                    :phrase-description "Introduce task"}
                                   :correct-answer {:name               "correct-answer"
                                                    :phrase             "correct-answer"
                                                    :phrase-description "correct answer"
                                                    :prompt             "Correct answer"}
                                   :wrong-answer   {:name   "wrong-answer"
                                                    :prompt "Wrong answer"}
                                   :finish-dialog  {:name   "finish-dialog"
                                                    :prompt "Finish dialog"}}}))
