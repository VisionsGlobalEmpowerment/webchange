(ns webchange.templates.library.categorize.synonyms.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)
(def all-vars-in-actions round-1-template/all-vars-in-actions)

(def template (round-1-template/get-template
                {:boxes      [{:name     "chilly"
                               :position {:x 980 :y 763}
                               :src      "/raw/img/categorize-synonyms/chilly.png"}
                              {:name     "kid"
                               :position {:x 1274 :y 763}
                               :src      "/raw/img/categorize-synonyms/kid.png"}
                              {:name     "big"
                               :position {:x 393 :y 763}
                               :src      "/raw/img/categorize-synonyms/big.png"}
                              {:name     "trash"
                               :position {:x 1568 :y 763}
                               :src      "/raw/img/categorize-synonyms/trash.png"}
                              {:name     "afraid"
                               :position {:x 686 :y 763}
                               :src      "/raw/img/categorize-synonyms/afraid.png"}
                              {:name     "glad"
                               :position {:x 99 :y 763}
                               :src      "/raw/img/categorize-synonyms/glad.png"}]
                 :items      [{:target         "chilly"
                               :position       {:x 701 :y 89}
                               :src            "/raw/img/categorize-synonyms/cold.png"
                               :pick-dialog    {:name   "cold-item"
                                                :phrase "cold"}
                               :correct-dialog {:name   "cold-correct"
                                                :phrase "cold correct"}}
                              {:target         "kid"
                               :position       {:x 863 :y 243}
                               :src            "/raw/img/categorize-synonyms/child.png"
                               :pick-dialog    {:name   "child-item"
                                                :phrase "child"}
                               :correct-dialog {:name   "child-correct"
                                                :phrase "child correct"}}
                              {:target         "big"
                               :position       {:x 448 :y 256}
                               :src            "/raw/img/categorize-synonyms/large.png"
                               :pick-dialog    {:name   "large-item"
                                                :phrase "large"}
                               :correct-dialog {:name   "large-correct"
                                                :phrase "large correct"}}
                              {:target         "trash"
                               :position       {:x 1035 :y 383}
                               :src            "/raw/img/categorize-synonyms/garbage.png"
                               :pick-dialog    {:name   "garbage-item"
                                                :phrase "garbage"}
                               :correct-dialog {:name   "garbage-correct"
                                                :phrase "garbage correct"}}
                              {:target         "afraid"
                               :position       {:x 1121 :y 105}
                               :src            "/raw/img/categorize-synonyms/scared.png"
                               :pick-dialog    {:name   "scared-item"
                                                :phrase "scared"}
                               :correct-dialog {:name   "scared-correct"
                                                :phrase "scared correct"}}
                              {:target         "glad"
                               :position       {:x 701 :y 393}
                               :src            "/raw/img/categorize-synonyms/happy.png"
                               :pick-dialog    {:name   "happy-item"
                                                :phrase "happy"}
                               :correct-dialog {:name   "happy-correct"
                                                :phrase "happy correct"}}]
                 :background {:src "/raw/img/categorize-synonyms/background.png"}}))
