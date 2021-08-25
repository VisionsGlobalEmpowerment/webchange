(ns webchange.utils.scene-common-actions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.utils.list :as lists]
    [webchange.utils.scene-common-actions :refer [remove-character remove-image remove-question]]
    [webchange.utils.scene-data :refer [find-action-recursively get-available-actions get-updates-history]]))

;; Remove Image

(defonce uploaded-image-name "uploaded-image-1")
(defonce uploaded-image-src "/upload/NFTPZIBJBISFBZGG.png")
(defonce uploaded-image-related-actions ["show-uploaded-image-1" "hide-uploaded-image-1"])
(defonce remove-image-initial-scene-data
         {:assets        [{:url   "/upload/NCXFSGZIKQCVAKQZ.mp3"
                           :date  1629271789116
                           :lang  nil
                           :size  2
                           :type  "audio"
                           :alias "none"}
                          {:url  uploaded-image-src
                           :size 1
                           :type "image"}]

          :objects       {:credits-page-title {:y           304
                                               :align       "center"
                                               :font-size   64
                                               :fill        "black"
                                               :editable?   {:select true}
                                               :type        "text"
                                               :x           480
                                               :font-family "Lexend Deca"
                                               :text        "my title"}
                          :uploaded-image-1   {:y         736.6666666666666
                                               :editable? {:drag          true
                                                           :select        true
                                                           :show-in-tree? true}
                                               :type      "image"
                                               :src       uploaded-image-src
                                               :alias     "Test image"
                                               :origin    {:type "center-center"}
                                               :x         954.9999999999997
                                               :visible   true
                                               :links     [{:id   "show-uploaded-image-1"
                                                            :type "action"}
                                                           {:id   "hide-uploaded-image-1"
                                                            :type "action"}]}}

          :scene-objects [["layered-background"] ["teacher"] ["book-background"] ["book" "vera-04-girl-1" "uploaded-image-1"]]

          :actions       {:show-uploaded-image-1 {:type       "set-attribute"
                                                  :target     "uploaded-image-1"
                                                  :attr-name  "visible"
                                                  :attr-value true}
                          :hide-uploaded-image-1 {:type       "set-attribute"
                                                  :target     "uploaded-image-1"
                                                  :attr-name  "visible"
                                                  :attr-value false}
                          :dialog-intro          {:data               [{:data [{:type     "empty"
                                                                                :duration 0}
                                                                               {:id   "show-uploaded-image-1"
                                                                                :type "action"}]
                                                                        :type "sequence-data"}
                                                                       {:data [{:type     "empty"
                                                                                :duration 0}
                                                                               {:id     "emotion_sad"
                                                                                :loop   false
                                                                                :type   "add-animation"
                                                                                :track  3
                                                                                :target "teacher"}]
                                                                        :type "sequence-data"}
                                                                       {:data [{:type     "empty"
                                                                                :duration 0}
                                                                               {:id   "hide-uploaded-image-1"
                                                                                :type "action"}]
                                                                        :type "sequence-data"}
                                                                       {:type "parallel"
                                                                        :data [{:data [{:type     "empty"
                                                                                        :duration 0}
                                                                                       {:type        "animation-sequence"
                                                                                        :audio       nil
                                                                                        :phrase-text "New action"}]
                                                                                :type "sequence-data"}
                                                                               {:data [{:type     "empty"
                                                                                        :duration 0}
                                                                                       {:id   "show-uploaded-image-1"
                                                                                        :type "action"}]
                                                                                :type "sequence-data"}]}
                                                                       {:type "parallel"
                                                                        :data [{:data [{:type     "empty"
                                                                                        :duration 0}
                                                                                       {:id   "hide-uploaded-image-1"
                                                                                        :type "action"}]
                                                                                :type "sequence-data"}]}]
                                                  :type               "sequence-data"
                                                  :phrase             "instruction"
                                                  :concept-var        "current-word"
                                                  :editor-type        "dialog"
                                                  :phrase-description "Instruction"}}

          :metadata      {:stage-size        "contain"
                          :history           {:created {:activity-name "Book"
                                                        :characters    [{:name     "teacher"
                                                                         :skeleton "senoravaca"}]
                                                        :cover-title   "my title"
                                                        :name          "Book"
                                                        :cover-layout  "title-top"
                                                        :lang          "English"
                                                        :skills        []
                                                        :illustrators  ["illustrator"]
                                                        :course-name   "my title"
                                                        :cover-image   {:src "/upload/BYBCRIPVTBJJTLAQ.png"}
                                                        :authors       ["author"]
                                                        :template-id   32}
                                              :updated [{:data           {:name "vera"
                                                                          :skin "04 girl"}
                                                         :action         "add-character"
                                                         :common-action? true}
                                                        {:data           {:name  "Test image"
                                                                          :image {:src uploaded-image-src}}
                                                         :action         "add-image"
                                                         :common-action? true}]}

                          :available-actions [{:name   "Hide vera-04-girl-1"
                                               :type   "image"
                                               :links  [{:id   "vera-04-girl-1"
                                                         :type "object"}]
                                               :action "hide-character-vera-04-girl-1"}
                                              {:name   "Show Test image"
                                               :type   "image"
                                               :links  [{:id   "uploaded-image-1"
                                                         :type "object"}]
                                               :action "show-uploaded-image-1"}
                                              {:name   "Hide Test image"
                                               :type   "image"
                                               :links  [{:id   "uploaded-image-1"
                                                         :type "object"}]
                                               :action "hide-uploaded-image-1"}]}})

(deftest uploaded-image-removed-correctly
   (let [result-scene-data (remove-image remove-image-initial-scene-data {:name uploaded-image-name})]
     (testing "Image removed from :assets"
       (is (= (some (fn [{:keys [url] :as asset}]
                      (and (= url uploaded-image-src) asset))
                    (:assets result-scene-data))
              nil)))

     (testing "Image removed from :objects"
       (is (not (contains? (:objects result-scene-data) (keyword uploaded-image-name)))))

     (testing "Image removed from :scene-objects"
       (is (= (some (fn [layer]
                      (and (lists/in-list? layer uploaded-image-name) layer))
                    (:scene-objects result-scene-data))
              nil)))

     (testing "Show/hide actions removed from :action"
       (doseq [related-action uploaded-image-related-actions]
         (is (not (contains? (:actions result-scene-data) (keyword related-action))))))

     (testing "Show/hide actions removed from sequences where they are used"
       (is (= (find-action-recursively result-scene-data (fn [{:keys [id type]}]
                                                           (and (= type "action")
                                                                (or (= id "hide-uploaded-image-1")
                                                                    (= id "show-uploaded-image-1")))))
              nil)))

     (testing "Show/hide actions removed from available actions"
       (let [available-actions (get-available-actions result-scene-data)]
         (doseq [related-action uploaded-image-related-actions]
           (is (= (some (fn [{:keys [action] :as available-action}]
                          (and (= action related-action) available-action))
                        available-actions)
                  nil)))))

     (testing "Check overall scene data"
       (let [expected-data {:assets        [{:url   "/upload/NCXFSGZIKQCVAKQZ.mp3"
                                             :date  1629271789116
                                             :lang  nil
                                             :size  2
                                             :type  "audio"
                                             :alias "none"}]

                            :objects       {:credits-page-title {:y           304
                                                                 :align       "center"
                                                                 :font-size   64
                                                                 :fill        "black"
                                                                 :editable?   {:select true}
                                                                 :type        "text"
                                                                 :x           480
                                                                 :font-family "Lexend Deca"
                                                                 :text        "my title"}}

                            :scene-objects [["layered-background"] ["teacher"] ["book-background"] ["book" "vera-04-girl-1"]]

                            :actions       {:dialog-intro {:data               [{:data [{:type     "empty"
                                                                                         :duration 0}
                                                                                        {:id     "emotion_sad"
                                                                                         :loop   false
                                                                                         :type   "add-animation"
                                                                                         :track  3
                                                                                         :target "teacher"}]
                                                                                 :type "sequence-data"}
                                                                                {:type "parallel"
                                                                                 :data [{:data [{:type     "empty"
                                                                                                 :duration 0}
                                                                                                {:type        "animation-sequence"
                                                                                                 :audio       nil
                                                                                                 :phrase-text "New action"}]
                                                                                         :type "sequence-data"}]}]
                                                           :type               "sequence-data"
                                                           :phrase             "instruction"
                                                           :concept-var        "current-word"
                                                           :editor-type        "dialog"
                                                           :phrase-description "Instruction"}}

                            :metadata      {:stage-size        "contain"
                                            :history           {:created {:activity-name "Book"
                                                                          :characters    [{:name     "teacher"
                                                                                           :skeleton "senoravaca"}]
                                                                          :cover-title   "my title"
                                                                          :name          "Book"
                                                                          :cover-layout  "title-top"
                                                                          :lang          "English"
                                                                          :skills        []
                                                                          :illustrators  ["illustrator"]
                                                                          :course-name   "my title"
                                                                          :cover-image   {:src "/upload/BYBCRIPVTBJJTLAQ.png"}
                                                                          :authors       ["author"]
                                                                          :template-id   32}
                                                                :updated [{:data           {:name "vera"
                                                                                            :skin "04 girl"}
                                                                           :action         "add-character"
                                                                           :common-action? true}
                                                                          {:data           {:name  "Test image"
                                                                                            :image {:src uploaded-image-src}}
                                                                           :action         "add-image"
                                                                           :common-action? true}]}

                                            :available-actions [{:name   "Hide vera-04-girl-1"
                                                                 :type   "image"
                                                                 :links  [{:id   "vera-04-girl-1"
                                                                           :type "object"}]
                                                                 :action "hide-character-vera-04-girl-1"}]}}]
         (when-not (= result-scene-data expected-data)
           (print-maps-comparison result-scene-data expected-data))
         (is (= result-scene-data expected-data))))))

;; Remove character

(defonce added-character-name "vera-04-girl-1")
(defonce added-character-related-actions ["show-character-vera-04-girl-1" "hide-character-vera-04-girl-1"])
(defonce remove-character-initial-scene-data
         {:assets        [{:url  "/raw/img/casa/background_casa.png",
                           :size 10,
                           :type "image"}
                          "/raw/img/flipbook/logo_2.png"],
          :actions       {:dialog-intro                  {:data               [{:data [{:type "empty", :duration 0}
                                                                                       {:id     "emotion_sad",
                                                                                        :loop   false,
                                                                                        :type   "add-animation",
                                                                                        :track  3,
                                                                                        :target "teacher"}],
                                                                                :type "sequence-data"}
                                                                               {:data [{:type "empty", :duration 0}
                                                                                       {:end                2.359,
                                                                                        :type               "animation-sequence",
                                                                                        :audio              "/upload/JFTRBBVCQWUBZIFC.mp3",
                                                                                        :start              0.023,
                                                                                        :target             "vera-04-girl-1",
                                                                                        :duration           2.336,
                                                                                        :phrase-text        "Sad",
                                                                                        :phrase-placeholder "Enter phrase text"}],
                                                                                :type "sequence-data"}
                                                                               {:data [{:type "empty", :duration 0}
                                                                                       {:id     "emotion_surprised",
                                                                                        :loop   false,
                                                                                        :type   "add-animation",
                                                                                        :track  3,
                                                                                        :target "teacher"}],
                                                                                :type "sequence-data"}
                                                                               {:data [{:type "empty", :duration 0}
                                                                                       {:end                3.15,
                                                                                        :type               "animation-sequence",
                                                                                        :audio              "/upload/ULNQESXDPOJHUHOG.mp3",
                                                                                        :start              0.006,
                                                                                        :target             "vera-04-girl-1",
                                                                                        :duration           3.144,
                                                                                        :phrase-text        "Surprised",
                                                                                        :phrase-placeholder "Enter phrase text"}],
                                                                                :type "sequence-data"}
                                                                               {:data [{:type "empty", :duration 0}
                                                                                       {:type   "remove-animation",
                                                                                        :track  3,
                                                                                        :target "teacher"}],
                                                                                :type "sequence-data"}

                                                                               {:data [{:type     "empty"
                                                                                        :duration 0}
                                                                                       {:id   "show-character-vera-04-girl-1"
                                                                                        :type "action"}]
                                                                                :type "sequence-data"}
                                                                               {:data [{:data [{:type     "empty"
                                                                                                :duration 0}
                                                                                               {:type        "animation-sequence"
                                                                                                :audio       nil
                                                                                                :target      "vera-02-vera-2-1"
                                                                                                :phrase-text "Test Vare"}]
                                                                                        :type "sequence-data"}
                                                                                       {:data [{:type     "empty"
                                                                                                :duration 0}
                                                                                               {:id   "hide-character-vera-04-girl-1"
                                                                                                :type "action"}]
                                                                                        :type "sequence-data"}]
                                                                                :type "parallel"}

                                                                               ],
                                                          :type               "sequence-data",
                                                          :phrase             "instruction",
                                                          :concept-var        "current-word",
                                                          :editor-type        "dialog",
                                                          :phrase-description "Instruction"},
                          :hide-character-vera-04-girl-1 {:type       "set-attribute",
                                                          :target     "vera-04-girl-1",
                                                          :attr-name  "visible",
                                                          :attr-value false},
                          :show-character-vera-04-girl-1 {:type       "set-attribute",
                                                          :target     "vera-04-girl-1",
                                                          :attr-name  "visible",
                                                          :attr-value true}},
          :objects       {:vera-04-girl-1     {:y          961.6666666666673,
                                               :skin-names {:body    "BODY/AdultTon-04",
                                                            :head    "HEAD/ManHead-01-Ton-01",
                                                            :clothes "CLOTHES/Man-01-Clothes-01"},
                                               :speed      1,
                                               :scale      {:x -0.7, :y 0.7},
                                               :name       "adult",
                                               :start      true,
                                               :editable?  {:drag          true,
                                                            :select        true,
                                                            :show-in-tree? true},
                                               :type       "animation",
                                               :anim       "idle",
                                               :x          909.6666666666671,
                                               :visible    true,
                                               :skin       nil,
                                               :links      [{:id   "show-character-vera-04-girl-1"
                                                             :type "action"}
                                                            {:id   "hide-character-vera-04-girl-1"
                                                             :type "action"}]},
                          :layered-background {:type       "layered-background",
                                               :surface    {:src "/raw/img/casa/surface_casa.png"},
                                               :background {:src "/raw/img/casa/background_casa.png"},
                                               :decoration {:src "/raw/img/casa/decoration_casa.png"}}},
          :metadata      {:available-actions   [{:name   "Show vera-04-girl-1",
                                                 :type   "image",
                                                 :links  [{:id   "vera-04-girl-1",
                                                           :type "object"}],
                                                 :action "show-character-vera-04-girl-1"}
                                                {:name   "Hide vera-04-girl-1",
                                                 :type   "image",
                                                 :links  [{:id   "vera-04-girl-1",
                                                           :type "object"}],
                                                 :action "hide-character-vera-04-girl-1"}
                                                {:name   "Show Test image",
                                                 :type   "image",
                                                 :links  [{:id   "uploaded-image-1",
                                                           :type "object"}],
                                                 :action "show-uploaded-image-1"}
                                                {:name   "Hide Test image",
                                                 :type   "image",
                                                 :links  [{:id   "uploaded-image-1",
                                                           :type "object"}],
                                                 :action "hide-uploaded-image-1"}],
                          :added-character-idx 1},
          :scene-objects [["layered-background"]
                          ["book" "vera-04-girl-1" "uploaded-image-1"]]})

(deftest added-character-removed-correctly
   (let [result-scene-data (remove-character remove-character-initial-scene-data {:name added-character-name})]
     (testing "Character removed from :objects"
       (is (not (contains? (:objects result-scene-data)
                           (keyword added-character-name)))))

     (testing "Character removed from :scene-objects"
       (is (= (some (fn [layer]
                      (and (lists/in-list? layer added-character-name) layer))
                    (:scene-objects result-scene-data))
              nil)))

     (testing "Show/hide character actions removed from available-actions"
       (let [available-actions (get-available-actions result-scene-data)]
         (doseq [related-action added-character-related-actions]
           (is (= (some (fn [{:keys [action] :as available-action}]
                          (and (= action related-action) available-action))
                        available-actions)
                  nil)))))

     (testing "Show/hide character actions removed from :actions"
       (doseq [related-action added-character-related-actions]
         (is (not (contains? (:actions result-scene-data)
                             (keyword related-action))))))

     (testing "Character as target removed from animation sequences"
       (is (= (find-action-recursively result-scene-data (fn [{:keys [target]}]
                                                           (= target added-character-name)))
              nil)))

     (testing "Check overall scene data"
       (let [expected-data {:assets        [{:url  "/raw/img/casa/background_casa.png",
                                             :size 10,
                                             :type "image"}
                                            "/raw/img/flipbook/logo_2.png"],
                            :actions       {:dialog-intro {:data               [{:data [{:type "empty", :duration 0}
                                                                                        {:id     "emotion_sad",
                                                                                         :loop   false,
                                                                                         :type   "add-animation",
                                                                                         :track  3,
                                                                                         :target "teacher"}],
                                                                                 :type "sequence-data"}
                                                                                {:data [{:type "empty", :duration 0}
                                                                                        {:end                2.359,
                                                                                         :type               "animation-sequence",
                                                                                         :audio              "/upload/JFTRBBVCQWUBZIFC.mp3",
                                                                                         :start              0.023,
                                                                                         :target             nil,
                                                                                         :duration           2.336,
                                                                                         :phrase-text        "Sad",
                                                                                         :phrase-placeholder "Enter phrase text"}],
                                                                                 :type "sequence-data"}
                                                                                {:data [{:type "empty", :duration 0}
                                                                                        {:id     "emotion_surprised",
                                                                                         :loop   false,
                                                                                         :type   "add-animation",
                                                                                         :track  3,
                                                                                         :target "teacher"}],
                                                                                 :type "sequence-data"}
                                                                                {:data [{:type "empty", :duration 0}
                                                                                        {:end                3.15,
                                                                                         :type               "animation-sequence",
                                                                                         :audio              "/upload/ULNQESXDPOJHUHOG.mp3",
                                                                                         :start              0.006,
                                                                                         :target             nil,
                                                                                         :duration           3.144,
                                                                                         :phrase-text        "Surprised",
                                                                                         :phrase-placeholder "Enter phrase text"}],
                                                                                 :type "sequence-data"}
                                                                                {:data [{:type "empty", :duration 0}
                                                                                        {:type   "remove-animation",
                                                                                         :track  3,
                                                                                         :target "teacher"}],
                                                                                 :type "sequence-data"}

                                                                                {:data [{:data [{:type     "empty"
                                                                                                 :duration 0}
                                                                                                {:type        "animation-sequence"
                                                                                                 :audio       nil
                                                                                                 :target      "vera-02-vera-2-1"
                                                                                                 :phrase-text "Test Vare"}]
                                                                                         :type "sequence-data"}]
                                                                                 :type "parallel"}

                                                                                ],
                                                           :type               "sequence-data",
                                                           :phrase             "instruction",
                                                           :concept-var        "current-word",
                                                           :editor-type        "dialog",
                                                           :phrase-description "Instruction"}},
                            :objects       {:layered-background {:type       "layered-background",
                                                                 :surface    {:src "/raw/img/casa/surface_casa.png"},
                                                                 :background {:src "/raw/img/casa/background_casa.png"},
                                                                 :decoration {:src "/raw/img/casa/decoration_casa.png"}}},
                            :metadata      {:available-actions   [{:name   "Show Test image",
                                                                   :type   "image",
                                                                   :links  [{:id   "uploaded-image-1",
                                                                             :type "object"}],
                                                                   :action "show-uploaded-image-1"}
                                                                  {:name   "Hide Test image",
                                                                   :type   "image",
                                                                   :links  [{:id   "uploaded-image-1",
                                                                             :type "object"}],
                                                                   :action "hide-uploaded-image-1"}],
                                            :added-character-idx 1},
                            :scene-objects [["layered-background"]
                                            ["book" "uploaded-image-1"]]}]
         (when-not (= result-scene-data expected-data)
           (print-maps-comparison result-scene-data expected-data))
         (is (= result-scene-data expected-data))))))

;; Remove question

(def remove-question-initial-scene-data {:assets        [{:url "/raw/img/casa/background.jpg", :size 10, :type "image"}
                                                         {:url "/images/questions/option1.png", :size 1, :type "image"}
                                                         {:url "/images/questions/option2.png", :size 1, :type "image"}
                                                         {:url "/images/questions/question.png", :size 1, :type "image"}],
                                         :actions       {:question-0-task-dialog                           {:data               [{:data [{:type "empty", :duration 0}
                                                                                                                                         {:data        [],
                                                                                                                                          :type        "text-animation",
                                                                                                                                          :audio       nil,
                                                                                                                                          :target      "question-0-task-text",
                                                                                                                                          :animation   "bounce",
                                                                                                                                          :phrase-text "Question placeholder"}],
                                                                                                                                  :type "sequence-data"}],
                                                                                                            :tags               ["question-action"],
                                                                                                            :type               "sequence-data",
                                                                                                            :editor-type        "dialog",
                                                                                                            :phrase-description "Question text"},
                                                         :question-0-options-option-0-substrate-inactivate {:id     "default",
                                                                                                            :tags   ["inactivate-options-question-0-question-id"
                                                                                                                     "inactivate-option-option-1-question-0-question-id"],
                                                                                                            :type   "state",
                                                                                                            :target "question-0-options-option-0-substrate"},
                                                         :question-0-task-voice-over-click                 {:data [{:tag  "activate-voice-over-task-question-0-question-id",
                                                                                                                    :type "parallel-by-tag"}
                                                                                                                   {:id "question-0-task-dialog", :type "action"}
                                                                                                                   {:tag  "inactivate-voice-over-task-question-0-question-id",
                                                                                                                    :type "parallel-by-tag"}],
                                                                                                            :type "sequence-data"},
                                                         :question-0-options-option-1-substrate-inactivate {:id     "default",
                                                                                                            :tags   ["inactivate-options-question-0-question-id"
                                                                                                                     "inactivate-option-option-2-question-0-question-id"],
                                                                                                            :type   "state",
                                                                                                            :target "question-0-options-option-1-substrate"},
                                                         :dialog-main                                      {:data               [{:data [{:type     "empty"
                                                                                                                                          :duration 0}
                                                                                                                                         {:id   "question-0"
                                                                                                                                          :type "action"}]
                                                                                                                                  :type "sequence-data"}
                                                                                                                                 {:data [{:data [{:type     "empty"
                                                                                                                                                  :duration 0}
                                                                                                                                                 {:type        "animation-sequence"
                                                                                                                                                  :audio       nil
                                                                                                                                                  :phrase-text "New action"}]
                                                                                                                                          :type "sequence-data"}
                                                                                                                                         {:data [{:type     "empty"
                                                                                                                                                  :duration 0} {:id   "question-0"
                                                                                                                                                                :type "action"}]
                                                                                                                                          :type "sequence-data"}]
                                                                                                                                  :type "parallel"}]
                                                                                                            :type               "sequence-data"
                                                                                                            :phrase             "Main"
                                                                                                            :editor-type        "dialog"
                                                                                                            :phrase-description "Main"},
                                                         :placeholder                                      {:type "empty", :duration 200},
                                                         :script                                           {:data   [{:type "start-activity"} {:id "dialog-main", :type "action"}],
                                                                                                            :type   "workflow",
                                                                                                            :on-end "finish"},
                                                         :question-0-options-option-0-button-activate      {:data [{:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-0-button-background"}
                                                                                                                   {:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-0-button-icon"}],
                                                                                                            :tags ["activate-voice-over-option-1-question-0-question-id"],
                                                                                                            :type "sequence-data"},
                                                         :question-0-option-voice-over-voice-over-option-1 {:data               [{:data [{:type "empty", :duration 0}
                                                                                                                                         {:type        "animation-sequence",
                                                                                                                                          :audio       nil,
                                                                                                                                          :phrase-text "Option 1"}],
                                                                                                                                  :type "sequence-data"}],
                                                                                                            :type               "sequence-data",
                                                                                                            :editor-type        "dialog",
                                                                                                            :phrase-description "Option \"Option 1\" voice-over"},
                                                         :stop-activity                                    {:type "stop-activity"},
                                                         :question-0-options-option-1-substrate-activate   {:id     "active",
                                                                                                            :tags   ["activate-option-option-2-question-0-question-id"],
                                                                                                            :type   "state",
                                                                                                            :target "question-0-options-option-1-substrate"},
                                                         :question-0-hide                                  {:type "set-attribute", :target "question-0", :attr-name "visible", :attr-value false},
                                                         :question-0-options-option-0-substrate-activate   {:id     "active",
                                                                                                            :tags   ["activate-option-option-1-question-0-question-id"],
                                                                                                            :type   "state",
                                                                                                            :target "question-0-options-option-0-substrate"},
                                                         :question-0-options-option-1-button-inactivate    {:data [{:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-1-button-background"}
                                                                                                                   {:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-1-button-icon"}],
                                                                                                            :tags ["inactivate-voice-overs-question-0-question-id"
                                                                                                                   "inactivate-voice-over-option-2-question-0-question-id"],
                                                                                                            :type "sequence-data"},
                                                         :question-0-option-voice-over-voice-over-option-2 {:data               [{:data [{:type "empty", :duration 0}
                                                                                                                                         {:type        "animation-sequence",
                                                                                                                                          :audio       nil,
                                                                                                                                          :phrase-text "Option 2"}],
                                                                                                                                  :type "sequence-data"}],
                                                                                                            :type               "sequence-data",
                                                                                                            :editor-type        "dialog",
                                                                                                            :phrase-description "Option \"Option 2\" voice-over"},
                                                         :question-0-check-answers-correct-answer-dialog   {:data               [{:data [{:type "empty", :duration 0}
                                                                                                                                         {:type        "animation-sequence",
                                                                                                                                          :audio       nil,
                                                                                                                                          :phrase-text ""}],
                                                                                                                                  :type "sequence-data"}],
                                                                                                            :type               "sequence-data",
                                                                                                            :editor-type        "dialog",
                                                                                                            :phrase-description "Correct answer"},
                                                         :finish                                           {:type "finish-activity"},
                                                         :question-0-option-click-handler                  {:data [{:id          "question-0-question-id",
                                                                                                                    :type        "question-pick",
                                                                                                                    :from-params [{:param-property "value", :action-property "value"}]}
                                                                                                                   {:id          "question-0-question-id",
                                                                                                                    :fail        {:type        "parallel-by-tag",
                                                                                                                                  :from-params [{:template        "inactivate-option-%-question-0-question-id",
                                                                                                                                                 :param-property  "value",
                                                                                                                                                 :action-property "tag"}]},
                                                                                                                    :type        "question-test",
                                                                                                                    :success     {:type        "parallel-by-tag",
                                                                                                                                  :from-params [{:template        "activate-option-%-question-0-question-id",
                                                                                                                                                 :param-property  "value",
                                                                                                                                                 :action-property "tag"}]},
                                                                                                                    :from-params [{:param-property "value", :action-property "value"}]}
                                                                                                                   {:type    "test-value",
                                                                                                                    :value1  "one",
                                                                                                                    :value2  "one",
                                                                                                                    :success "question-0-check-answers"}],
                                                                                                            :type "sequence-data"},
                                                         :question-0-check-answers-wrong-answer            {:data [{:type "empty", :duration 500}
                                                                                                                   {:id   "question-0-check-answers-wrong-answer-dialog",
                                                                                                                    :type "action"}],
                                                                                                            :type "sequence-data"},
                                                         :question-0-task-text-group-button-activate       {:data [{:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-task-text-group-button-background"}
                                                                                                                   {:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-task-text-group-button-icon"}],
                                                                                                            :tags ["activate-voice-over-task-question-0-question-id"],
                                                                                                            :type "sequence-data"},
                                                         :question-0-option-voice-over                     {:data [{:type        "parallel-by-tag",
                                                                                                                    :from-params [{:template        "activate-voice-over-%-question-0-question-id",
                                                                                                                                   :param-property  "value",
                                                                                                                                   :action-property "tag"}]}
                                                                                                                   {:type        "case",
                                                                                                                    :options     {:option-1 {:id   "question-0-option-voice-over-voice-over-option-1",
                                                                                                                                             :type "action"},
                                                                                                                                  :option-2 {:id   "question-0-option-voice-over-voice-over-option-2",
                                                                                                                                             :type "action"}},
                                                                                                                    :from-params [{:param-property "value", :action-property "value"}]}
                                                                                                                   {:type        "parallel-by-tag",
                                                                                                                    :from-params [{:template        "inactivate-voice-over-%-question-0-question-id",
                                                                                                                                   :param-property  "value",
                                                                                                                                   :action-property "tag"}]}],
                                                                                                            :type "sequence-data"},
                                                         :question-0-check-answers-correct-answer          {:data [{:id   "question-0-check-answers-correct-answer-dialog",
                                                                                                                    :type "action"}
                                                                                                                   {:id "question-0-hide", :type "action"}
                                                                                                                   {:tag "question-0-question-id", :type "finish-flows"}],
                                                                                                            :type "sequence-data"},
                                                         :question-0-show                                  {:type "set-attribute", :target "question-0", :attr-name "visible", :attr-value true},
                                                         :question-0-check-answers-wrong-answer-dialog     {:data               [{:data [{:type "empty", :duration 0}
                                                                                                                                         {:type        "animation-sequence",
                                                                                                                                          :audio       nil,
                                                                                                                                          :phrase-text ""}],
                                                                                                                                  :type "sequence-data"}],
                                                                                                            :type               "sequence-data",
                                                                                                            :editor-type        "dialog",
                                                                                                            :phrase-description "Wrong answer"},
                                                         :question-0-task-text-group-button-inactivate     {:data [{:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-task-text-group-button-background"}
                                                                                                                   {:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-task-text-group-button-icon"}],
                                                                                                            :tags ["inactivate-voice-overs-question-0-question-id"
                                                                                                                   "inactivate-voice-over-task-question-0-question-id"],
                                                                                                            :type "sequence-data"},
                                                         :question-0-options-option-0-button-inactivate    {:data [{:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-0-button-background"}
                                                                                                                   {:id     "default",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-0-button-icon"}],
                                                                                                            :tags ["inactivate-voice-overs-question-0-question-id"
                                                                                                                   "inactivate-voice-over-option-1-question-0-question-id"],
                                                                                                            :type "sequence-data"},
                                                         :question-0-check-answers                         {:data [{:id      "question-0-question-id",
                                                                                                                    :fail    "question-0-check-answers-wrong-answer",
                                                                                                                    :type    "question-check",
                                                                                                                    :answer  [],
                                                                                                                    :success "question-0-check-answers-correct-answer"}
                                                                                                                   {:id "question-0-question-id", :type "question-reset"}
                                                                                                                   {:tag "inactivate-options-question-0-question-id", :type "parallel-by-tag"}],
                                                                                                            :type "sequence-data"},
                                                         :question-0                                       {:data                [{:id "question-0-show", :type "action"} {:id "question-0-task-dialog", :type "action"}],
                                                                                                            :tags                ["question-0-question-id"],
                                                                                                            :type                "sequence-data",
                                                                                                            :description         "-- Description --",
                                                                                                            :workflow-user-input true},
                                                         :question-0-options-option-1-button-activate      {:data [{:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-1-button-background"}
                                                                                                                   {:id     "active",
                                                                                                                    :type   "state",
                                                                                                                    :target "question-0-options-option-1-button-icon"}],
                                                                                                            :tags ["activate-voice-over-option-2-question-0-question-id"],
                                                                                                            :type "sequence-data"}},
                                         :objects       {:question-0-options-option-0-text              {:y              443.75,
                                                                                                         :vertical-align "middle",
                                                                                                         :font-size      48,
                                                                                                         :word-wrap      true,
                                                                                                         :width          189,
                                                                                                         :editable?      {:select true},
                                                                                                         :type           "text",
                                                                                                         :actions        {:click {:id         "question-0-option-click-handler",
                                                                                                                                  :on         "click",
                                                                                                                                  :type       "action",
                                                                                                                                  :params     {:value "option-1"},
                                                                                                                                  :unique-tag "question-action"}},
                                                                                                         :x              110,
                                                                                                         :text           "Option 1"},
                                                         :question-0-options-option-0-button-icon       {:y          24,
                                                                                                         :states     {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                                                                         :fill       "#000000",
                                                                                                         :width      41,
                                                                                                         :type       "svg-path",
                                                                                                         :x          19.5,
                                                                                                         :scene-name "letter-tutorial-trace",
                                                                                                         :height     32,
                                                                                                         :data       "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
                                                         :question-0-options-option-1-image-image       {:y             186.875,
                                                                                                         :width         279,
                                                                                                         :editable?     {:select true},
                                                                                                         :type          "image",
                                                                                                         :src           "/images/questions/option2.png",
                                                                                                         :image-size    "contain",
                                                                                                         :origin        {:type "center-center"},
                                                                                                         :x             149.5,
                                                                                                         :border-radius 0,
                                                                                                         :height        353.75},
                                                         :question-0-options-option-1-button            {:x        0,
                                                                                                         :y        403.75,
                                                                                                         :type     "group",
                                                                                                         :actions  {:click {:id         "question-0-option-voice-over",
                                                                                                                            :on         "click",
                                                                                                                            :type       "action",
                                                                                                                            :params     {:value "option-2"},
                                                                                                                            :unique-tag "question-action"}},
                                                                                                         :children ["question-0-options-option-1-button-background"
                                                                                                                    "question-0-options-option-1-button-icon"]},
                                                         :question-0-background                         {:x           0,
                                                                                                         :y           412.5286478227654,
                                                                                                         :fill        16753409,
                                                                                                         :type        "rectangle",
                                                                                                         :width       1920,
                                                                                                         :height      667.4713521772346,
                                                                                                         :object-name "question-0-background"},
                                                         :question-0-options-option-1-text              {:y              443.75,
                                                                                                         :vertical-align "middle",
                                                                                                         :font-size      48,
                                                                                                         :word-wrap      true,
                                                                                                         :width          189,
                                                                                                         :editable?      {:select true},
                                                                                                         :type           "text",
                                                                                                         :actions        {:click {:id         "question-0-option-click-handler",
                                                                                                                                  :on         "click",
                                                                                                                                  :type       "action",
                                                                                                                                  :params     {:value "option-2"},
                                                                                                                                  :unique-tag "question-action"}},
                                                                                                         :x              110,
                                                                                                         :text           "Option 2"},
                                                         :background                                    {:src "/raw/img/casa/background.jpg", :type "background"},
                                                         :question-0-options-option-0-image             {:x           0,
                                                                                                         :y           0,
                                                                                                         :type        "group",
                                                                                                         :children    ["question-0-options-option-0-image-image"],
                                                                                                         :object-name "question-0-options-option-0-image"},
                                                         :question-0-task-text                          {:y              0,
                                                                                                         :vertical-align "top",
                                                                                                         :font-size      48,
                                                                                                         :word-wrap      true,
                                                                                                         :width          956.6157372039727,
                                                                                                         :editable?      {:select true},
                                                                                                         :type           "text",
                                                                                                         :x              110,
                                                                                                         :text           "Question placeholder"},
                                                         :question-0-task-text-group-button-background  {:x             0,
                                                                                                         :y             0,
                                                                                                         :fill          16777215,
                                                                                                         :type          "rectangle",
                                                                                                         :width         80,
                                                                                                         :height        80,
                                                                                                         :states        {:active {:fill 45823}, :default {:fill 16777215}},
                                                                                                         :border-radius 40},
                                                         :question-0-options                            {:x        0,
                                                                                                         :y        412.5286478227654,
                                                                                                         :type     "group",
                                                                                                         :children ["question-0-options-option-0" "question-0-options-option-1"]},
                                                         :question-0-options-option-1                   {:x           990,
                                                                                                         :y           73.73567608861731,
                                                                                                         :type        "group",
                                                                                                         :children    ["question-0-options-option-1-substrate"
                                                                                                                       "question-0-options-option-1-button"
                                                                                                                       "question-0-options-option-1-image"
                                                                                                                       "question-0-options-option-1-text"],
                                                                                                         :object-name "question-0-options-option-1"},
                                                         :question-0-task-image                         {:x          366.69213139801366,
                                                                                                         :y          206.2643239113827,
                                                                                                         :src        "/images/questions/question.png",
                                                                                                         :type       "image",
                                                                                                         :origin     {:type "center-center"},
                                                                                                         :editable?  {:select true},
                                                                                                         :max-width  613.3842627960273,
                                                                                                         :max-height 292.5286478227654},
                                                         :question-0-options-option-0-button            {:x        0,
                                                                                                         :y        403.75,
                                                                                                         :type     "group",
                                                                                                         :actions  {:click {:id         "question-0-option-voice-over",
                                                                                                                            :on         "click",
                                                                                                                            :type       "action",
                                                                                                                            :params     {:value "option-1"},
                                                                                                                            :unique-tag "question-action"}},
                                                                                                         :children ["question-0-options-option-0-button-background"
                                                                                                                    "question-0-options-option-0-button-icon"]},
                                                         :question-0-substrate                          {:x           0,
                                                                                                         :y           0,
                                                                                                         :fill        16777215,
                                                                                                         :type        "rectangle",
                                                                                                         :width       1920,
                                                                                                         :height      1080,
                                                                                                         :object-name "question-0-substrate"},
                                                         :question-0-options-option-0-substrate         {:y             0,
                                                                                                         :states        {:active {:border-color 45823}, :default {:border-color 0}},
                                                                                                         :fill          16777215,
                                                                                                         :width         299,
                                                                                                         :type          "rectangle",
                                                                                                         :actions       {:click {:id         "question-0-option-click-handler",
                                                                                                                                 :on         "click",
                                                                                                                                 :type       "action",
                                                                                                                                 :params     {:value "option-1"},
                                                                                                                                 :unique-tag "question-action"}},
                                                                                                         :border-width  2,
                                                                                                         :border-color  0,
                                                                                                         :x             0,
                                                                                                         :border-radius 20,
                                                                                                         :height        373.75},
                                                         :question-0-options-option-0-image-image       {:y             186.875,
                                                                                                         :width         279,
                                                                                                         :editable?     {:select true},
                                                                                                         :type          "image",
                                                                                                         :src           "/images/questions/option1.png",
                                                                                                         :image-size    "contain",
                                                                                                         :origin        {:type "center-center"},
                                                                                                         :x             149.5,
                                                                                                         :border-radius 0,
                                                                                                         :height        353.75},
                                                         :question-0-options-option-0-button-background {:x             0,
                                                                                                         :y             0,
                                                                                                         :fill          16777215,
                                                                                                         :type          "rectangle",
                                                                                                         :width         80,
                                                                                                         :height        80,
                                                                                                         :states        {:active {:fill 45823}, :default {:fill 16777215}},
                                                                                                         :border-radius 40},
                                                         :question-0-task-text-group-button-icon        {:y          24,
                                                                                                         :states     {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                                                                         :fill       "#000000",
                                                                                                         :width      41,
                                                                                                         :type       "svg-path",
                                                                                                         :x          19.5,
                                                                                                         :scene-name "letter-tutorial-trace",
                                                                                                         :height     32,
                                                                                                         :data       "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
                                                         :child                                         {:y          960,
                                                                                                         :skin-names {:body    "BODY/ChildTon-01",
                                                                                                                      :head    "HEAD/Head-Girl-01-Ton-01",
                                                                                                                      :clothes "CLOTHES/Girl-01-Clothes-01"},
                                                                                                         :speed      1,
                                                                                                         :scale      {:x 0.5, :y 0.5},
                                                                                                         :name       "child",
                                                                                                         :width      342,
                                                                                                         :start      true,
                                                                                                         :editable?  true,
                                                                                                         :type       "animation",
                                                                                                         :anim       "idle",
                                                                                                         :meshes     true,
                                                                                                         :x          176,
                                                                                                         :scene-name "child",
                                                                                                         :height     691},
                                                         :question-0-options-option-1-button-background {:x             0,
                                                                                                         :y             0,
                                                                                                         :fill          16777215,
                                                                                                         :type          "rectangle",
                                                                                                         :width         80,
                                                                                                         :height        80,
                                                                                                         :states        {:active {:fill 45823}, :default {:fill 16777215}},
                                                                                                         :border-radius 40},
                                                         :question-0-options-option-1-substrate         {:y             0,
                                                                                                         :states        {:active {:border-color 45823}, :default {:border-color 0}},
                                                                                                         :fill          16777215,
                                                                                                         :width         299,
                                                                                                         :type          "rectangle",
                                                                                                         :actions       {:click {:id         "question-0-option-click-handler",
                                                                                                                                 :on         "click",
                                                                                                                                 :type       "action",
                                                                                                                                 :params     {:value "option-2"},
                                                                                                                                 :unique-tag "question-action"}},
                                                                                                         :border-width  2,
                                                                                                         :border-color  0,
                                                                                                         :x             0,
                                                                                                         :border-radius 20,
                                                                                                         :height        373.75},
                                                         :question-0-options-option-1-button-icon       {:y          24,
                                                                                                         :states     {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                                                                         :fill       "#000000",
                                                                                                         :width      41,
                                                                                                         :type       "svg-path",
                                                                                                         :x          19.5,
                                                                                                         :scene-name "letter-tutorial-trace",
                                                                                                         :height     32,
                                                                                                         :data       "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
                                                         :question-0-task-text-group                    {:x        793.3842627960273,
                                                                                                         :y        60,
                                                                                                         :type     "group",
                                                                                                         :children ["question-0-task-text-group-button" "question-0-task-text"]},
                                                         :question-0                                    {:x         0,
                                                                                                         :y         0,
                                                                                                         :type      "group",
                                                                                                         :alias     "Test question",
                                                                                                         :visible   false,
                                                                                                         :children  ["question-0-substrate"
                                                                                                                     "question-0-background"
                                                                                                                     "question-0-task-text-group"
                                                                                                                     "question-0-options"
                                                                                                                     "question-0-task-image"],
                                                                                                         :metadata  {:assets  ["/images/questions/option1.png"
                                                                                                                               "/images/questions/option2.png"
                                                                                                                               "/images/questions/question.png"],
                                                                                                                     :actions ["question-0-task-dialog"
                                                                                                                               "question-0-options-option-0-substrate-inactivate"
                                                                                                                               "question-0-task-voice-over-click"
                                                                                                                               "question-0-options-option-1-substrate-inactivate"
                                                                                                                               "question-0-options-option-0-button-activate"
                                                                                                                               "question-0-option-voice-over-voice-over-option-1"
                                                                                                                               "question-0-options-option-1-substrate-activate"
                                                                                                                               "question-0-hide"
                                                                                                                               "question-0-options-option-0-substrate-activate"
                                                                                                                               "question-0-options-option-1-button-inactivate"
                                                                                                                               "question-0-option-voice-over-voice-over-option-2"
                                                                                                                               "question-0-check-answers-correct-answer-dialog"
                                                                                                                               "question-0-option-click-handler"
                                                                                                                               "question-0-check-answers-wrong-answer"
                                                                                                                               "question-0-task-text-group-button-activate"
                                                                                                                               "question-0-option-voice-over"
                                                                                                                               "question-0-check-answers-correct-answer"
                                                                                                                               "question-0-show"
                                                                                                                               "question-0-check-answers-wrong-answer-dialog"
                                                                                                                               "question-0-task-text-group-button-inactivate"
                                                                                                                               "question-0-options-option-0-button-inactivate"
                                                                                                                               "question-0-check-answers"
                                                                                                                               "question-0"
                                                                                                                               "question-0-options-option-1-button-activate"],
                                                                                                                     :objects ["question-0-options-option-0-text"
                                                                                                                               "question-0-options-option-0-button-icon"
                                                                                                                               "question-0-options-option-1-image-image"
                                                                                                                               "question-0-options-option-1-button"
                                                                                                                               "question-0-background"
                                                                                                                               "question-0-options-option-1-text"
                                                                                                                               "question-0-options-option-0-image"
                                                                                                                               "question-0-task-text"
                                                                                                                               "question-0-task-text-group-button-background"
                                                                                                                               "question-0-options"
                                                                                                                               "question-0-options-option-1"
                                                                                                                               "question-0-task-image"
                                                                                                                               "question-0-options-option-0-button"
                                                                                                                               "question-0-substrate"
                                                                                                                               "question-0-options-option-0-substrate"
                                                                                                                               "question-0-options-option-0-image-image"
                                                                                                                               "question-0-options-option-0-button-background"
                                                                                                                               "question-0-task-text-group-button-icon"
                                                                                                                               "question-0-options-option-1-button-background"
                                                                                                                               "question-0-options-option-1-substrate"
                                                                                                                               "question-0-options-option-1-button-icon"
                                                                                                                               "question-0-task-text-group"
                                                                                                                               "question-0"
                                                                                                                               "question-0-options-option-0"
                                                                                                                               "question-0-task-text-group-button"
                                                                                                                               "question-0-options-option-1-image"]},
                                                                                                         :editable? {:show-in-tree? true}},
                                                         :question-0-options-option-0                   {:x           631,
                                                                                                         :y           73.73567608861731,
                                                                                                         :type        "group",
                                                                                                         :children    ["question-0-options-option-0-substrate"
                                                                                                                       "question-0-options-option-0-button"
                                                                                                                       "question-0-options-option-0-image"
                                                                                                                       "question-0-options-option-0-text"],
                                                                                                         :object-name "question-0-options-option-0"},
                                                         :question-0-task-text-group-button             {:x        0,
                                                                                                         :y        0,
                                                                                                         :type     "group",
                                                                                                         :actions  {:click {:id         "question-0-task-voice-over-click",
                                                                                                                            :on         "click",
                                                                                                                            :type       "action",
                                                                                                                            :unique-tag "question-action"}},
                                                                                                         :children ["question-0-task-text-group-button-background"
                                                                                                                    "question-0-task-text-group-button-icon"]},
                                                         :question-0-options-option-1-image             {:x           0,
                                                                                                         :y           0,
                                                                                                         :type        "group",
                                                                                                         :children    ["question-0-options-option-1-image-image"],
                                                                                                         :object-name "question-0-options-option-1-image"}},
                                         :metadata      {:template-name     "Conversation",
                                                         :tracks            [{:nodes [{:type "dialog", :action-id "dialog-main"}], :title "Sequence"}
                                                                             {:nodes       [{:type "dialog", :action-id "question-0-task-dialog"}
                                                                                            {:type "dialog", :action-id "question-0-check-answers-correct-answer-dialog"}
                                                                                            {:type "dialog", :action-id "question-0-check-answers-wrong-answer-dialog"}
                                                                                            {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-1"}
                                                                                            {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-2"}],
                                                                              :title       "Test question",
                                                                              :question-id "question-0"}],
                                                         :history           {:created {:lang          "English",
                                                                                       :skills        [],
                                                                                       :characters    [{:name "child", :skeleton "child"}],
                                                                                       :template-id   26,
                                                                                       :activity-name "Conversation"},
                                                                             :updated [{:data   {:question-page-object {:answers-number  "one",
                                                                                                                        :layout          "vertical",
                                                                                                                        :task-type       "text-image",
                                                                                                                        :question-type   "multiple-choice-image",
                                                                                                                        :alias           "Test question",
                                                                                                                        :options-number  2,
                                                                                                                        :mark-options    ["thumbs-up" "thumbs-down"],
                                                                                                                        :option-label    "audio-text",
                                                                                                                        :correct-answers []}},
                                                                                        :action "add-question-object"}]},
                                                         :actions           {:add-dialog          {:title   "Add dialogue",
                                                                                                   :options {:dialog {:type        "string",
                                                                                                                      :label       "Dialog name",
                                                                                                                      :description "Dialog name",
                                                                                                                      :placeholder "(ex. Conversation about ball)"}}},
                                                                             :add-question-object {:title   "Add question",
                                                                                                   :options {:question-page-object {:type          "question-object",
                                                                                                                                    :label         "Question",
                                                                                                                                    :max-answers   4,
                                                                                                                                    :answers-label "Answers"}}}},
                                                         :template-version  nil,
                                                         :autostart         true,
                                                         :unique-suffix     0,
                                                         :template-id       26,
                                                         :available-actions [{:name "Ask Test question", :type "question", :action "question-0"}]},
                                         :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "script"}},
                                         :scene-objects [["background"] ["child"] ["question-0"]]})

(def question-name "question-0")
(def question-resources ["/images/questions/option1.png"
                         "/images/questions/option2.png"
                         "/images/questions/question.png"])

(deftest added-question-removed-correctly
  (let [result-scene-data (remove-question remove-question-initial-scene-data {:name question-name})]
    (testing "Question removed from :assets"
      (is (= (some (fn [{:keys [url]}]
                     (some #{url} question-resources))
                   (:assets result-scene-data))
             nil)))

    (testing "Check overall scene data"
      (let [expected-data {:assets        [{:url  "/raw/img/casa/background.jpg"
                                            :size 10
                                            :type "image"}]
                           :actions       {:finish        {:type "finish-activity"}
                                           :script        {:data   [{:type "start-activity"} {:id "dialog-main", :type "action"}],
                                                           :type   "workflow",
                                                           :on-end "finish"}
                                           :dialog-main   {:data               [{:data [{:data [{:type     "empty"
                                                                                                 :duration 0}
                                                                                                {:type        "animation-sequence"
                                                                                                 :audio       nil
                                                                                                 :phrase-text "New action"}]
                                                                                         :type "sequence-data"}]
                                                                                 :type "parallel"}]
                                                           :type               "sequence-data"
                                                           :phrase             "Main"
                                                           :editor-type        "dialog"
                                                           :phrase-description "Main"}
                                           :placeholder   {:type     "empty"
                                                           :duration 200}
                                           :stop-activity {:type "stop-activity"}}
                           :objects       {:child      {:y          960
                                                        :skin-names {:body    "BODY/ChildTon-01"
                                                                     :head    "HEAD/Head-Girl-01-Ton-01"
                                                                     :clothes "CLOTHES/Girl-01-Clothes-01"}
                                                        :speed      1
                                                        :scale      {:x 0.5
                                                                     :y 0.5}
                                                        :name       "child"
                                                        :width      342
                                                        :start      true
                                                        :editable?  true
                                                        :type       "animation"
                                                        :anim       "idle"
                                                        :meshes     true
                                                        :x          176
                                                        :scene-name "child"
                                                        :height     691}
                                           :background {:src  "/raw/img/casa/background.jpg"
                                                        :type "background"}}
                           :metadata      {:template-name     "Conversation"
                                           :tracks            [{:nodes [{:type      "dialog"
                                                                         :action-id "dialog-main"}]
                                                                :title "Sequence"}]
                                           :history           {:created {:lang          "English"
                                                                         :skills        []
                                                                         :characters    [{:name     "child"
                                                                                          :skeleton "child"}]
                                                                         :template-id   26
                                                                         :activity-name "Conversation"}
                                                               :updated [{:data   {:question-page-object {:answers-number  "one",
                                                                                                          :layout          "vertical",
                                                                                                          :task-type       "text-image",
                                                                                                          :question-type   "multiple-choice-image",
                                                                                                          :alias           "Test question",
                                                                                                          :options-number  2,
                                                                                                          :mark-options    ["thumbs-up" "thumbs-down"],
                                                                                                          :option-label    "audio-text",
                                                                                                          :correct-answers []}},
                                                                          :action "add-question-object"}]}
                                           :actions           {:add-dialog          {:title   "Add dialogue"
                                                                                     :options {:dialog {:type        "string"
                                                                                                        :label       "Dialog name"
                                                                                                        :description "Dialog name"
                                                                                                        :placeholder "(ex. Conversation about ball)"}}}
                                                               :add-question-object {:title   "Add question"
                                                                                     :options {:question-page-object {:type          "question-object"
                                                                                                                      :label         "Question"
                                                                                                                      :max-answers   4
                                                                                                                      :answers-label "Answers"}}}}
                                           :template-version  nil
                                           :available-actions []
                                           :autostart         true
                                           :unique-suffix     0
                                           :template-id       26}
                           :triggers      {:back  {:on     "back"
                                                   :action "stop-activity"}
                                           :start {:on     "start"
                                                   :action "script"}}
                           :scene-objects [["background"] ["child"]]}]
        (when-not (= result-scene-data expected-data)
          (print-maps-comparison result-scene-data expected-data))
        (is (= result-scene-data expected-data))))))
