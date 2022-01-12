(ns webchange.dev-templates.categorize-colors
  (:require [webchange.dev-templates :as t]
            [webchange.templates.core :as templates]
            [webchange.course.core :as core]))

(defn- copy-activity
  [course-name-source activity-name]
  (let [scene-data (core/get-scene-latest-version course-name-source activity-name)
        course-name-source-new (-> (t/create-test-course) :slug)]
    (core/save-scene! course-name-source-new activity-name scene-data t/user-id)
    [course-name-source-new
     (str "http://localhost:3000/courses/" course-name-source-new "/editor-v2/" activity-name)
     (str "http://localhost:3000/s/" course-name-source-new "/" activity-name)]))

(declare saved-data)

(comment
  "/courses/test-course-english-mbcakkhf/editor-v2/categorize-colors-2"

  (def test-course-slug (-> (t/create-test-course) :slug))
  (def scene-slug "test-activity")

  (copy-activity "english" "categorize-colors-2")

  (def test-course-slug "test-course-english-blkulfyh")
  (def scene-slug "categorize-colors-2")
  (core/update-activity-template! test-course-slug scene-slug t/user-id)

  (core/get-scene-latest-version test-course-slug scene-slug)

  (core/save-scene! test-course-slug scene-slug saved-data t/user-id)

  (-> (core/get-scene-latest-version test-course-slug scene-slug)
      (get-in [:objects])
      (select-keys [:question-0-task-image :question-1-question-id--task-image
                    :question-0--task-image :question-1--task-image]))


  (let [data {:activity-name "Categorize - colors"
              :template-id   22
              :lang          "English"
              :skills        []}
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)
        [_ {scene-slug :scene-slug}] (core/create-scene! activity metadata test-course-slug scene-slug [] t/user-id)]
    (str "/courses/" test-course-slug "/editor-v2/" scene-slug)))

(def saved-data
  {:assets [{:url "/raw/img/categorize/01.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/yellow_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/blue_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/red_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/blue_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/question.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/red_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/yellow_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/background.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/yellow_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/blue_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/red_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/purple_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/orange_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/green_box.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/purple_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/orange_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/green_crayons.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/purple_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/orange_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/green_table.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/yellow_box_small.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/blue_box_small.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/red_box_small.png", :size 1, :type "image"}
          {:url "/raw/img/categorize/background-3.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/yellow_box_small.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/blue_box_small.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/red_box_small.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
          {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}
          {:url "/upload/VBIMTMHQXEWJOVXK.mp3",
           :date 1626469028218,
           :size 299,
           :type "audio",
           :alias "librarian",
           :target nil}
          {:url "/upload/SIXWHSICTJUWRKRH.mp3", :date 1626469366510, :size 20, :type "audio", :alias "correct sound"}
          {:url "/upload/JYNSFQQINNQMSQCP.mp3", :date 1626469406442, :size 10, :type "audio", :alias "incorrect sound"}
          {:url "/upload/CAABQYMYFJYJARUS.mp3",
           :date 1626469457069,
           :size 4,
           :type "audio",
           :alias "chime to signal new round"}
          {:url "/upload/UGMZCSAYDMJPTISA.mp3",
           :date 1626469520123,
           :size 181,
           :type "audio",
           :alias "colors game voice"}
          {:url "/upload/LGFWFEKYFMZRKXYS.mp3", :size 10, :type "audio"}
          {:url "/upload/IYPUFFYJAQHCFWTA.mp3",
           :date 1626469748660,
           :size 1,
           :type "audio",
           :alias "snap into place sound"}
          {:url "/upload/RECUYDHQKEHUDJMY.mp3",
           :date 1626733491288,
           :size 22,
           :type "audio",
           :alias "librarian blue color"}
          {:url "/images/questions/question.png", :size 1, :type "image"}
          {:url "/upload/TCKFMRBQWQXGCKXR.m4a",
           :date 1639160590080,
           :lang nil,
           :size 74,
           :type "audio",
           :alias "BK Rounds 1 & 3"}
          {:url "/upload/ZUSJJJULTEAQYZAZ.m4a",
           :date 1639160784173,
           :lang nil,
           :size 18,
           :type "audio",
           :alias "BK Round 2"}],
 :skills [],
 :actions {:green-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                           {:end 8.85,
                                            :data [{:end 8.75, :anim "talk", :start 7.99}],
                                            :type "animation-sequence",
                                            :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                            :start 7.98,
                                            :duration 0.8699999999999992,
                                            :phrase-text "green"}],
                                    :type "sequence-data"}],
                            :type "sequence-data",
                            :phrase "Color green",
                            :unique-tag "color",
                            :editor-type "dialog",
                            :phrase-description "Color green"},
           :say-color-r1 {:data [{:type "action", :from-params [{:param-property "say-color", :action-property "id"}]}
                                 {:type "test-var-scalar", :value true, :success "next-say-r1", :var-name "say"}],
                          :type "sequence-data"},
           :unhighlight-r3 {:data [{:type "set-variable",
                                    :var-value false,
                                    :from-params [{:template "colliding-raw-%",
                                                   :param-property "transition",
                                                   :action-property "var-name"}]}
                                   {:type "set-attribute",
                                    :attr-name "highlight",
                                    :attr-value false,
                                    :from-params [{:param-property "transition", :action-property "target"}]}
                                   {:type "set-variable", :var-name "highlighted", :var-value false}],
                            :type "sequence-data"},
           :question-0-options-option-3-voice-over-activate {:data [{:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-3-voice-over-background"}
                                                                    {:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-3-voice-over-icon"}],
                                                             :tags ["activate-voice-over-option-4-question-0-question-id"],
                                                             :type "sequence-data"},
           :question-0-task-dialog {:data [{:data [{:type "empty", :duration 0}
                                                   {:animation "color",
                                                    :fill 45823,
                                                    :phrase-text "Question placeholder",
                                                    :start 58.682,
                                                    :type "text-animation",
                                                    :duration 2.46,
                                                    :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                    :target "question-0-task-text",
                                                    :end 61.142,
                                                    :data [{:at 58.696,
                                                            :end 58.886,
                                                            :chunk 0,
                                                            :start 58.696,
                                                            :duration 0.19}
                                                           {:at 58.88,
                                                            :end 59.043,
                                                            :chunk 1,
                                                            :start 58.88,
                                                            :duration 0.163}
                                                           {:at 59.084,
                                                            :end 59.177,
                                                            :chunk 2,
                                                            :start 59.084,
                                                            :duration 0.093}
                                                           {:at 59.169,
                                                            :end 59.538,
                                                            :chunk 3,
                                                            :start 59.169,
                                                            :duration 0.369}
                                                           {:at 59.531,
                                                            :end 59.838,
                                                            :chunk 4,
                                                            :start 59.531,
                                                            :duration 0.307}
                                                           {:at 59.871,
                                                            :end 60.163,
                                                            :chunk 5,
                                                            :start 59.871,
                                                            :duration 0.292}
                                                           {:at 60.17,
                                                            :end 60.425,
                                                            :chunk 6,
                                                            :start 60.17,
                                                            :duration 0.255}
                                                           {:at 60.416,
                                                            :end 61.128,
                                                            :chunk 7,
                                                            :start 60.416,
                                                            :duration 0.712}]}],
                                            :type "sequence-data"}],
                                    :tags ["question-action"],
                                    :type "sequence-data",
                                    :editor-type "dialog",
                                    :phrase-description "Question text"},
           :start-background-music {:id "/upload/LGFWFEKYFMZRKXYS.mp3", :loop true, :type "audio", :volume "0.1"},
           :wrong-answer-r2 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 0.483,
                                             :data [],
                                             :type "animation-sequence",
                                             :audio "/upload/JYNSFQQINNQMSQCP.mp3",
                                             :start 0,
                                             :duration 0.483,
                                             :phrase-text "incorrect sound"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "wrong-answer",
                             :editor-type "dialog",
                             :phrase-description "wrong answer"},
           :question-0-options-option-0-substrate-inactivate {:id "default",
                                                              :tags ["inactivate-options-question-0-question-id"
                                                                     "inactivate-option-option-1-question-0-question-id"],
                                                              :type "state",
                                                              :target "question-0-options-option-0-substrate"},
           :wrong-option-r2 {:data [{:id "unhighlight-all-r2", :type "action"}
                                    {:id "crayon-revert-r2", :type "action"}
                                    {:id "wrong-answer-r2", :type "action"}],
                             :type "parallel"},
           :yellow-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 7.38,
                                             :data [{:end 7.33, :anim "talk", :start 6.5}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 6.48,
                                             :duration 0.8999999999999995,
                                             :phrase-text "yellow"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color yellow",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color yellow"},
           :blink-objects-r3 {:data [{:type "set-attribute",
                                      :from-var [{:var-name "object-1", :action-property "target"}],
                                      :attr-name "highlight",
                                      :attr-value true}
                                     {:type "set-attribute",
                                      :from-var [{:var-name "object-2", :action-property "target"}],
                                      :attr-name "highlight",
                                      :attr-value true}
                                     {:type "empty", :duration 2000}
                                     {:type "set-attribute",
                                      :from-var [{:var-name "object-1", :action-property "target"}],
                                      :attr-name "highlight",
                                      :attr-value false}
                                     {:type "set-attribute",
                                      :from-var [{:var-name "object-2", :action-property "target"}],
                                      :attr-name "highlight",
                                      :attr-value false}],
                              :type "sequence-data"},
           :yellow-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 7.38,
                                             :data [{:end 7.33, :anim "talk", :start 6.5}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 6.48,
                                             :duration 0.8999999999999995,
                                             :phrase-text "yellow"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color yellow",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color yellow"},
           :red-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                         {:end 4.41,
                                          :data [{:end 4.37, :anim "talk", :start 3.77}],
                                          :type "animation-sequence",
                                          :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                          :start 3.75,
                                          :duration 0.6600000000000001,
                                          :phrase-text "red"}],
                                  :type "sequence-data"}],
                          :type "sequence-data",
                          :phrase "Color red",
                          :unique-tag "color",
                          :editor-type "dialog",
                          :phrase-description "Color red"},
           :red-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                         {:end 4.41,
                                          :data [{:end 4.37, :anim "talk", :start 3.77}],
                                          :type "animation-sequence",
                                          :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                          :start 3.75,
                                          :duration 0.6600000000000001,
                                          :phrase-text "red"}],
                                  :type "sequence-data"}],
                          :type "sequence-data",
                          :phrase "Color red",
                          :unique-tag "color",
                          :editor-type "dialog",
                          :phrase-description "Color red"},
           :init-technical-1 {:data [{:id "hide-technical", :type "state", :target "layered-background-r0"}
                                     {:id "hide-technical", :type "state", :target "yellow-table-r0"}
                                     {:id "hide-technical", :type "state", :target "blue-table-r0"}
                                     {:id "hide-technical", :type "state", :target "red-table-r0"}
                                     {:id "hide-technical", :type "state", :target "librarian-r0"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "red-box-r1"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "green-box-r1"}
                                     {:id "show-technical", :type "state", :target "red-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "background-r1"}
                                     {:id "show-technical", :type "state", :target "red-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "yellow-box-r1"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "green-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "orange-box-r1"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-3-r1"}
                                     {:id "show-technical", :type "state", :target "red-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "green-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-2-r1"}
                                     {:id "show-technical", :type "state", :target "green-crayon-1-r1"}
                                     {:id "show-technical", :type "state", :target "blue-box-r1"}
                                     {:id "show-technical", :type "state", :target "purple-box-r1"}],
                              :type "parallel"},
           :purple-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 11.58,
                                             :data [{:end 11.52, :anim "talk", :start 10.88}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 10.71,
                                             :duration 0.87,
                                             :phrase-text "purple"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color purple",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color purple"},
           :correct-answer-4-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.625,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.625,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 4",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 4"},
           :instruction-6-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 122.16,
                                              :data [{:end 122.09, :anim "talk", :start 119.83}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 119.73,
                                              :target "librarian-r3",
                                              :duration 2.4299999999999926,
                                              :phrase-text "Put the orange crayon on its table."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-6",
                              :editor-type "dialog",
                              :phrase-description "Put the orange crayon on its table."},
           :correct-answer-5-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.626,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.626,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 5",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 5"},
           :next-round-r0 {:id "finish-scene-r0", :type "action"},
           :question-0-option-voice-over-voice-over-option-3 {:data [{:data [{:type "empty", :duration 0}
                                                                             {:end 70.95,
                                                                              :data [{:end 70.89,
                                                                                      :anim "talk",
                                                                                      :start 68.88}],
                                                                              :type "animation-sequence",
                                                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                                              :start 68.85,
                                                                              :duration 2.1000000000000085,
                                                                              :phrase-text "They are both in their crayon boxes.",
                                                                              :region-text "they are both in their crayon boxes"}],
                                                                      :type "sequence-data"}],
                                                              :type "sequence-data",
                                                              :editor-type "dialog",
                                                              :phrase-description "Option \"Option 3\" voice-over"},
           :stop-activity-r2 {:type "stop-activity"},
           :task-5-r3 {:data [{:type "set-variable", :var-name "object-1", :var-value "blue-crayon-r3"}
                              {:type "set-variable", :var-name "object-2", :var-value "blue-box-r3"}
                              {:type "set-variable",
                               :var-name "check-collide",
                               :var-value ["colliding-object-blue-crayon-r3" "colliding-blue-box-r3"]}
                              {:type "set-variable", :var-name "next-task", :var-value "task-6-r3"}
                              {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-5-r3"}
                              {:type "set-variable", :var-name "instruction", :var-value "instruction-5-r3"}
                              {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                              {:id "instruction-5-r3", :type "action"}],
                       :type "sequence-data"},
           :question-0-options-option-2-substrate-inactivate {:id "default",
                                                              :tags ["inactivate-options-question-0-question-id"
                                                                     "inactivate-option-option-3-question-0-question-id"],
                                                              :type "state",
                                                              :target "question-0-options-option-2-substrate"},
           :question-0-options-option-0-voice-over-activate {:data [{:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-0-voice-over-background"}
                                                                    {:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-0-voice-over-icon"}],
                                                             :tags ["activate-voice-over-option-1-question-0-question-id"],
                                                             :type "sequence-data"},
           :question-0-task-voice-over-click {:data [{:tag "activate-voice-over-task-question-0-question-id",
                                                      :type "parallel-by-tag"}
                                                     {:id "question-0-task-dialog", :type "action"}
                                                     {:tag "inactivate-voice-over-task-question-0-question-id",
                                                      :type "parallel-by-tag"}],
                                              :type "sequence-data"},
           :question-0-options-option-1-substrate-inactivate {:id "default",
                                                              :tags ["inactivate-options-question-0-question-id"
                                                                     "inactivate-option-option-2-question-0-question-id"],
                                                              :type "state",
                                                              :target "question-0-options-option-1-substrate"},
           :question-0-options-option-3-substrate-inactivate {:id "default",
                                                              :tags ["inactivate-options-question-0-question-id"
                                                                     "inactivate-option-option-4-question-0-question-id"],
                                                              :type "state",
                                                              :target "question-0-options-option-3-substrate"},
           :purple-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 11.58,
                                             :data [{:end 11.52, :anim "talk", :start 10.88}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 10.71,
                                             :duration 0.87,
                                             :phrase-text "purple"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color purple",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color purple"},
           :correct-answer-r1 {:data [{:data [{:type "empty", :duration 0}
                                              {:end 0.119,
                                               :data [],
                                               :type "animation-sequence",
                                               :audio "/upload/IYPUFFYJAQHCFWTA.mp3",
                                               :start 0.051,
                                               :duration 0.068,
                                               :phrase-text "snap into place sound"}],
                                       :type "sequence-data"}],
                               :type "sequence-data",
                               :phrase "correct-answer",
                               :editor-type "dialog",
                               :phrase-description "correct answer"},
           :object-revert-r3 {:id "init-position",
                              :type "state",
                              :from-params [{:param-property "self", :action-property "target"}]},
           :init-activity-r0 {:data [{:type "start-activity"}
                                     {:id "voiceover-r0", :type "action"}
                                     {:id "next-round-r0", :type "action"}],
                              :type "sequence-data"},
           :task-3-r3 {:data [{:type "set-variable", :var-name "object-1", :var-value "yellow-crayon-r3"}
                              {:type "set-variable", :var-name "object-2", :var-value "yellow-box-r3"}
                              {:type "set-variable",
                               :var-name "check-collide",
                               :var-value ["colliding-object-yellow-crayon-r3" "colliding-yellow-box-r3"]}
                              {:type "set-variable", :var-name "next-task", :var-value "task-4-r3"}
                              {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-3-r3"}
                              {:type "set-variable", :var-name "instruction", :var-value "instruction-3-r3"}
                              {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                              {:id "instruction-3-r3", :type "action"}],
                       :type "sequence-data"},
           :correct-option-r2 {:data [{:id "unhighlight-all-r2", :type "action"}
                                      {:type "counter", :counter-id "sorted-crayons", :counter-action "increase"}
                                      {:id "crayon-in-right-box-r2", :type "action"}
                                      {:id "correct-answer-r2", :type "action"}
                                      {:type "test-var-inequality",
                                       :value 18,
                                       :success "finish-round-r2",
                                       :var-name "sorted-crayons",
                                       :inequality ">="}],
                               :type "sequence-data"},
           :yellow-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 7.38,
                                             :data [{:end 7.33, :anim "talk", :start 6.5}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 6.48,
                                             :duration 0.8999999999999995,
                                             :phrase-text "yellow"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color yellow",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color yellow"},
           :voiceover-r0 {:data [{:data [{:type "empty", :duration 0}
                                         {:phrase-text "Can you please help me clean",
                                          :start 6.66,
                                          :type "animation-sequence",
                                          :duration 1.2,
                                          :region-text "can you please help me clean",
                                          :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                          :target "librarian-r0",
                                          :end 7.86,
                                          :data [{:end 7.51, :anim "talk", :start 6.89}]}],
                                  :type "sequence-data"}
                                 {:data [{:type "empty", :duration nil}
                                         {:phrase-text "up?",
                                          :start 8.029,
                                          :type "animation-sequence",
                                          :duration 0.221,
                                          :region-text "up",
                                          :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                          :target "librarian-r0",
                                          :end 8.25,
                                          :phrase-placeholder "Enter phrase text",
                                          :data [{:end 8.2, :anim "talk", :start 8.05}]}],
                                  :type "sequence-data"}
                                 {:data [{:type "empty", :duration 0}
                                         {:phrase-text "Tap on me if you need help",
                                          :start 32.04,
                                          :type "animation-sequence",
                                          :duration 1.3200000000000003,
                                          :region-text "tap me if you need help",
                                          :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                          :target "librarian-r0",
                                          :end 33.36,
                                          :phrase-placeholder "Enter phrase text",
                                          :data [{:end 33.33, :anim "talk", :start 32.09}]}],
                                  :type "sequence-data"}],
                          :type "sequence-data",
                          :phrase "Introduce Game",
                          :editor-type "dialog",
                          :phrase-description "Introduce Game"},
           :question-0-option-voice-over-voice-over-option-1 {:data [{:data [{:type "empty", :duration 0}
                                                                             {:end 63.45,
                                                                              :data [{:end 63.41,
                                                                                      :anim "talk",
                                                                                      :start 62.51}],
                                                                              :type "animation-sequence",
                                                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                                              :start 62.43,
                                                                              :duration 1.0200000000000031,
                                                                              :phrase-text "They are both red.",
                                                                              :region-text "they are both red"}],
                                                                      :type "sequence-data"}],
                                                              :type "sequence-data",
                                                              :editor-type "dialog",
                                                              :phrase-description "Option \"Option 1\" voice-over"},
           :task-4-r3 {:data [{:type "set-variable", :var-name "object-1", :var-value "green-crayon-r3"}
                              {:type "set-variable", :var-name "object-2", :var-value "green-table-r3"}
                              {:type "set-variable",
                               :var-name "check-collide",
                               :var-value ["colliding-object-green-crayon-r3" "colliding-green-table-r3"]}
                              {:type "set-variable", :var-name "next-task", :var-value "task-5-r3"}
                              {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-4-r3"}
                              {:type "set-variable", :var-name "instruction", :var-value "instruction-4-r3"}
                              {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                              {:id "instruction-4-r3", :type "action"}],
                       :type "sequence-data"},
           :stop-activity-r1 {:type "stop-activity"},
           :init-activity-r1 {:data [{:type "counter",
                                      :counter-id "sorted-crayons",
                                      :counter-value 0,
                                      :counter-action "reset"}
                                     {:id "intro-r1", :type "action"}],
                              :type "sequence-data"},
           :correct-answer-dialog-r3 {:data [{:data [{:type "empty", :duration 0}
                                                     {:end 0.097,
                                                      :data [],
                                                      :type "animation-sequence",
                                                      :audio "/upload/IYPUFFYJAQHCFWTA.mp3",
                                                      :start 0.05,
                                                      :duration 0.047,
                                                      :phrase-text "snap into place sound"}],
                                              :type "sequence-data"}],
                                      :type "sequence-data",
                                      :phrase "correct-answer-dialog",
                                      :editor-type "dialog",
                                      :phrase-description "Correct answer dialog"},
           :start-drag-r3 {:data [{:type "set-variable-list",
                                   :values [false false],
                                   :from-var [{:var-name "check-collide", :action-property "var-names"}]}
                                  {:type "set-variable",
                                   :var-value true,
                                   :from-params [{:template "colliding-object-%",
                                                  :param-property "self",
                                                  :action-property "var-name"}]}
                                  {:type "set-variable", :var-name "say", :var-value true}
                                  {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                  {:id "check-say-r3", :type "action"}
                                  {:id "next-check-collide-r3", :type "action"}],
                           :type "sequence-data"},
           :stop-activity-r0 {:type "stop-activity"},
           :blue-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                          {:end 5.82,
                                           :data [{:end 5.78, :anim "talk", :start 5.17}],
                                           :type "animation-sequence",
                                           :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                           :start 5.07,
                                           :duration 0.75,
                                           :phrase-text "blue"}],
                                   :type "sequence-data"}],
                           :type "sequence-data",
                           :phrase "Color blue",
                           :unique-tag "color",
                           :editor-type "dialog",
                           :phrase-description "Color blue"},
           :highlight-r3 {:data [{:type "set-variable", :var-name "highlighted", :var-value true}
                                 {:type "set-variable",
                                  :var-value true,
                                  :from-params [{:template "colliding-raw-%",
                                                 :param-property "transition",
                                                 :action-property "var-name"}]}
                                 {:type "set-attribute",
                                  :attr-name "highlight",
                                  :attr-value true,
                                  :from-params [{:param-property "transition", :action-property "target"}]}],
                          :type "sequence-data"},
           :empty-r3 {:type "empty", :duration 100},
           :finish-activity-r3 {:type "finish-activity"},
           :green-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                           {:end 8.85,
                                            :data [{:end 8.75, :anim "talk", :start 7.99}],
                                            :type "animation-sequence",
                                            :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                            :start 7.98,
                                            :duration 0.8699999999999992,
                                            :phrase-text "green"}],
                                    :type "sequence-data"}],
                            :type "sequence-data",
                            :phrase "Color green",
                            :unique-tag "color",
                            :editor-type "dialog",
                            :phrase-description "Color green"},
           :question-0-options-option-2-voice-over-inactivate {:data [{:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-2-voice-over-background"}
                                                                      {:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-2-voice-over-icon"}],
                                                               :tags ["inactivate-voice-overs-question-0-question-id"
                                                                      "inactivate-voice-over-option-3-question-0-question-id"],
                                                               :type "sequence-data"},
           :question-0-options-option-1-substrate-activate {:id "active",
                                                            :tags ["activate-option-option-2-question-0-question-id"],
                                                            :type "state",
                                                            :target "question-0-options-option-1-substrate"},
           :instruction-1-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 101.634,
                                              :data [{:end 101.34, :anim "talk", :start 98.79}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 98.743,
                                              :target "librarian-r3",
                                              :duration 2.891,
                                              :phrase-text "Put the red crayon in its crayon box."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-1",
                              :editor-type "dialog",
                              :phrase-description "Put the red crayon in its crayon box."},
           :highlight-r1 {:data [{:type "set-variable",
                                  :var-value true,
                                  :from-params [{:param-property "check-variable", :action-property "var-name"}]}
                                 {:type "set-attribute",
                                  :attr-name "highlight",
                                  :attr-value true,
                                  :from-params [{:param-property "transition", :action-property "target"}]}],
                          :type "sequence-data"},
           :question-0-hide {:type "set-attribute", :target "question-0", :attr-name "visible", :attr-value false},
           :red-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                         {:end 4.41,
                                          :data [{:end 4.37, :anim "talk", :start 3.77}],
                                          :type "animation-sequence",
                                          :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                          :start 3.75,
                                          :duration 0.6600000000000001,
                                          :phrase-text "red"}],
                                  :type "sequence-data"}],
                          :type "sequence-data",
                          :phrase "Color red",
                          :unique-tag "color",
                          :editor-type "dialog",
                          :phrase-description "Color red"},
           :say-color-r3 {:data [{:type "action", :from-params [{:param-property "say-color", :action-property "id"}]}
                                 {:id "check-say-r3", :type "action"}],
                          :type "sequence-data"},
           :finish-scene-r0 {:data ["finish-activity-r0"], :type "sequence"},
           :finish-activity-r1 {:data [{:id "init-technical-2", :type "action"}
                                       {:id "init-activity-r2", :type "action"}],
                                :type "sequence-data"},
           :check-collide-r1 {:data [{:fail "unhighlight-r1",
                                      :type "test-transitions-and-pointer-collide",
                                      :success "highlight-r1",
                                      :transitions ["yellow-box-r1"
                                                    "blue-box-r1"
                                                    "red-box-r1"
                                                    "purple-box-r1"
                                                    "orange-box-r1"
                                                    "green-box-r1"],
                                      :action-params [{:check-variable "yellow-box-selected"}
                                                      {:check-variable "blue-box-selected"}
                                                      {:check-variable "red-box-selected"}
                                                      {:check-variable "purple-box-selected"}
                                                      {:check-variable "orange-box-selected"}
                                                      {:check-variable "green-box-selected"}]}
                                     {:type "test-var-scalar",
                                      :value true,
                                      :success "next-check-collide-r1",
                                      :var-name "next-check-collide"}],
                              :type "sequence-data"},
           :finish-round-r2 {:data [{:id "finish-round-dialog-r2", :type "action"}
                                    {:id "finish-scene-r2", :type "action"}],
                             :type "sequence-data"},
           :question-0-options-option-0-voice-over-inactivate {:data [{:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-0-voice-over-background"}
                                                                      {:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-0-voice-over-icon"}],
                                                               :tags ["inactivate-voice-overs-question-0-question-id"
                                                                      "inactivate-voice-over-option-1-question-0-question-id"],
                                                               :type "sequence-data"},
           :question-0-options-option-0-substrate-activate {:id "active",
                                                            :tags ["activate-option-option-1-question-0-question-id"],
                                                            :type "state",
                                                            :target "question-0-options-option-0-substrate"},
           :question-0-options-option-3-substrate-activate {:id "active",
                                                            :tags ["activate-option-option-4-question-0-question-id"],
                                                            :type "state",
                                                            :target "question-0-options-option-3-substrate"},
           :question-0-options-option-2-substrate-activate {:id "active",
                                                            :tags ["activate-option-option-3-question-0-question-id"],
                                                            :type "state",
                                                            :target "question-0-options-option-2-substrate"},
           :finish-dialog-r3 {:data [{:data [{:type "empty", :duration 0}
                                             {:end 35.01,
                                              :data [{:end 34.97, :anim "talk", :start 34.34}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 34.08,
                                              :target "librarian-r3",
                                              :duration 0.9299999999999997,
                                              :phrase-text "great work"}],
                                      :type "sequence-data"}],
                              :type "sequence-data",
                              :phrase "Finish dialog",
                              :editor-type "dialog",
                              :phrase-description "Finish dialog"},
           :instruction-5-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 117.9,
                                              :data [{:end 117.84, :anim "talk", :start 115.2}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 115.14,
                                              :target "librarian-r3",
                                              :duration 2.760000000000005,
                                              :phrase-text "Put the blue crayon in its crayon box."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-5",
                              :editor-type "dialog",
                              :phrase-description "Put the blue crayon in its crayon box."},
           :blue-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                          {:end 5.82,
                                           :data [{:end 5.78, :anim "talk", :start 5.17}],
                                           :type "animation-sequence",
                                           :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                           :start 5.07,
                                           :duration 0.75,
                                           :phrase-text "blue"}],
                                   :type "sequence-data"}],
                           :type "sequence-data",
                           :phrase "Color blue",
                           :unique-tag "color",
                           :editor-type "dialog",
                           :phrase-description "Color blue"},
           :init-technical-0 {:data [{:id "show-technical", :type "state", :target "layered-background-r0"}
                                     {:id "show-technical", :type "state", :target "yellow-table-r0"}
                                     {:id "show-technical", :type "state", :target "blue-table-r0"}
                                     {:id "show-technical", :type "state", :target "red-table-r0"}
                                     {:id "show-technical", :type "state", :target "librarian-r0"}],
                              :type "parallel"},
           :purple-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 11.58,
                                             :data [{:end 11.52, :anim "talk", :start 10.88}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 10.71,
                                             :duration 0.87,
                                             :phrase-text "purple"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color purple",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color purple"},
           :next-check-collide-r2 {:data [{:type "set-timeout", :action "check-collide-r2", :interval 10}],
                                   :type "sequence-data"},
           :finish-scene-r2 {:data [{:id "check-collide-2", :type "remove-interval"}
                                    {:id "finish-activity-r2", :type "action"}],
                             :type "sequence-data"},
           :question-0-option-voice-over-voice-over-option-2 {:data [{:data [{:type "empty", :duration 0}
                                                                             {:end 67.35,
                                                                              :data [{:end 67.29,
                                                                                      :anim "talk",
                                                                                      :start 64.99}],
                                                                              :type "animation-sequence",
                                                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                                              :start 64.98,
                                                                              :duration 2.3699999999999903,
                                                                              :phrase-text "They are both crayons you can use to color.",
                                                                              :region-text "they are both crayons you can use the color"}],
                                                                      :type "sequence-data"}],
                                                              :type "sequence-data",
                                                              :editor-type "dialog",
                                                              :phrase-description "Option \"Option 2\" voice-over"},
           :correct-answer-single-r3 {:data [{:id "unhighlight-all-r3", :type "action"}
                                             {:id "init-position",
                                              :type "state",
                                              :from-params [{:param-property "self", :action-property "target"}]}
                                             {:type "action",
                                              :from-var [{:var-name "correct-answer", :action-property "id"}]}
                                             {:type "action",
                                              :from-var [{:var-name "next-task", :action-property "id"}]}
                                             {:id "object-revert-r3", :type "action"}],
                                      :type "sequence-data"},
           :orange-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 10.11,
                                             :data [{:end 10.1, :anim "talk", :start 9.31}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 9.3,
                                             :duration 0.8099999999999987,
                                             :phrase-text "orange"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color orange",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color orange"},
           :next-check-collide-r3 {:data [{:type "set-timeout", :action "check-collide-r3", :interval 10}],
                                   :type "sequence-data"},
           :stop-activity-r3 {:type "stop-activity"},
           :tap-instructions-r2 {:data [{:data [{:type "empty", :duration 0}
                                                {:phrase-text "Drag the crayons to the box or table that matches their color.",
                                                 :start 0.69,
                                                 :type "animation-sequence",
                                                 :duration 4.619999999999999,
                                                 :region-text "drag the kranz to the box or table that matches their color",
                                                 :audio "/upload/SAAUMXETEVLKZVPF.mp3",
                                                 :target "librarian-r2",
                                                 :end 5.31,
                                                 :data [{:end 3.83, :anim "talk", :start 0.77}
                                                        {:end 5.29, :anim "talk", :start 3.99}]}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Tap instructions",
                                 :unique-tag "instructions",
                                 :editor-type "dialog",
                                 :phrase-description "Tap instructions"},
           :unhighlight-all-r2 {:data [{:type "set-attribute",
                                        :target "yellow-box-r2",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "blue-box-r2",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "red-box-r2",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "purple-table-r2",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "orange-table-r2",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "green-table-r2",
                                        :attr-name "highlight",
                                        :attr-value false}],
                                :type "parallel"},
           :question-0-check-answers-correct-answer-dialog {:data [{:data [{:type "empty", :duration 0}
                                                                           {:data [],
                                                                            :type "animation-sequence",
                                                                            :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                                            :phrase-text ""}],
                                                                    :type "sequence-data"}],
                                                            :type "sequence-data",
                                                            :editor-type "dialog",
                                                            :phrase-description "Correct answer"},
           :next-say-r2 {:data [{:type "set-timeout", :action "say-color-r2", :interval 100}], :type "sequence-data"},
           :intro-r2 {:data [{:data [{:type "empty", :duration 0}
                                     {:phrase-text "Drag the crayons to the box or table that matches their color.",
                                      :start 1.2,
                                      :type "animation-sequence",
                                      :duration 4.199,
                                      :region-text "dr the grand to the box or table that matches their color",
                                      :audio "/upload/ZUSJJJULTEAQYZAZ.m4a",
                                      :target "librarian-r2",
                                      :end 5.399,
                                      :data [{:end 5.27, :anim "talk", :start 1.33}]}],
                              :type "sequence-data"}],
                      :tags ["user-interactions-blocked"],
                      :type "sequence-data",
                      :phrase "Introduce task",
                      :unique-tag "instructions",
                      :editor-type "dialog",
                      :phrase-description "Introduce task"},
           :correct-option-r1 {:data [{:id "unhighlight-all-r1", :type "action"}
                                      {:type "counter", :counter-id "sorted-crayons", :counter-action "increase"}
                                      {:id "crayon-in-right-box-r1", :type "action"}
                                      {:id "correct-answer-r1", :type "action"}
                                      {:fail "continue-sorting-r1",
                                       :type "test-var-inequality",
                                       :value 18,
                                       :success "finish-scene-r1",
                                       :var-name "sorted-crayons",
                                       :inequality ">="}],
                               :type "sequence-data"},
           :task-6-r3 {:data [{:type "set-variable", :var-name "object-1", :var-value "orange-crayon-r3"}
                              {:type "set-variable", :var-name "object-2", :var-value "orange-table-r3"}
                              {:type "set-variable",
                               :var-name "check-collide",
                               :var-value ["colliding-object-orange-crayon-r3" "colliding-orange-table-r3"]}
                              {:type "set-variable", :var-name "next-task", :var-value "finish-r3"}
                              {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-6-r3"}
                              {:type "set-variable", :var-name "instruction", :var-value "instruction-6-r3"}
                              {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                              {:id "instruction-6-r3", :type "action"}],
                       :type "sequence-data"},
           :finish-activity-r2 {:data [{:id "init-technical-3", :type "action"}
                                       {:id "start-activity-r3", :type "action"}],
                                :type "sequence-data"},
           :check-collide-r2 {:data [{:fail "unhighlight-r2",
                                      :type "test-transitions-and-pointer-collide",
                                      :success "highlight-r2",
                                      :transitions ["purple-table-r2"
                                                    "orange-table-r2"
                                                    "green-table-r2"
                                                    "yellow-box-r2"
                                                    "blue-box-r2"
                                                    "red-box-r2"],
                                      :action-params [{:check-variable "purple-table-selected"}
                                                      {:check-variable "orange-table-selected"}
                                                      {:check-variable "green-table-selected"}
                                                      {:check-variable "yellow-box-selected"}
                                                      {:check-variable "blue-box-selected"}
                                                      {:check-variable "red-box-selected"}]}
                                     {:type "test-var-scalar",
                                      :value true,
                                      :success "next-check-collide-r2",
                                      :var-name "next-check-collide"}],
                              :type "sequence-data"},
           :question-0-options-option-1-voice-over-activate {:data [{:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-1-voice-over-background"}
                                                                    {:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-1-voice-over-icon"}],
                                                             :tags ["activate-voice-over-option-2-question-0-question-id"],
                                                             :type "sequence-data"},
           :instruction-4-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 113.34,
                                              :data [{:end 113.28, :anim "talk", :start 111.04}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 110.97,
                                              :target "librarian-r3",
                                              :duration 2.3700000000000045,
                                              :phrase-text "Put the green crayon on its table."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-4",
                              :editor-type "dialog",
                              :phrase-description "Put the green crayon on its table."},
           :start-activity-r3 {:data [{:type "set-variable", :var-name "instruction", :var-value "empty-r3"}
                                      {:id "intro-r3", :type "action"}
                                      {:type "set-variable", :var-name "object-1", :var-value "red-crayon-r3"}
                                      {:type "set-variable", :var-name "object-2", :var-value "red-box-r3"}
                                      {:type "set-variable",
                                       :var-name "check-collide",
                                       :var-value ["colliding-object-red-crayon-r3" "colliding-red-box-r3"]}
                                      {:type "set-variable", :var-name "next-task", :var-value "task-2-r3"}
                                      {:type "set-variable",
                                       :var-name "correct-answer",
                                       :var-value "correct-answer-1-r3"}
                                      {:type "set-variable", :var-name "instruction", :var-value "instruction-1-r3"}
                                      {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                                      {:id "instruction-1-r3", :type "action"}],
                               :type "sequence-data"},
           :crayon-in-right-box-r1 {:type "set-attribute",
                                    :attr-name "visible",
                                    :attr-value false,
                                    :from-params [{:param-property "target", :action-property "target"}]},
           :question-0-options-option-1-voice-over-inactivate {:data [{:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-1-voice-over-background"}
                                                                      {:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-1-voice-over-icon"}],
                                                               :tags ["inactivate-voice-overs-question-0-question-id"
                                                                      "inactivate-voice-over-option-2-question-0-question-id"],
                                                               :type "sequence-data"},
           :continue-sorting-r1 {:data [{:data [{:type "empty", :duration 0}
                                                {:type "animation-sequence",
                                                 :audio nil,
                                                 :phrase-text "New action",
                                                 :phrase-placeholder "Enter phrase text"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "continue-sorting",
                                 :editor-type "dialog",
                                 :phrase-description "Continue sorting"},
           :stop-drag-hide-r3 {:data [{:type "copy-variables",
                                       :from-params [{:template "colliding-%",
                                                      :param-property "colliders",
                                                      :action-property "var-names"}
                                                     {:template "colliding-raw-%",
                                                      :param-property "colliders",
                                                      :action-property "from-list"}]}
                                      {:type "set-variable", :var-name "say", :var-value false}
                                      {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                      {:fail "object-revert-r3",
                                       :type "test-value",
                                       :value1 true,
                                       :success "stop-drag-highlighted-r3",
                                       :from-var [{:var-name "highlighted", :action-property "value2"}]}],
                               :type "sequence-data"},
           :check-say-r3 {:type "test-var-scalar", :value true, :success "next-say-r3", :var-name "say"},
           :question-0-option-click-handler {:data [{:id "question-0-question-id",
                                                     :type "question-pick",
                                                     :from-params [{:param-property "value", :action-property "value"}]}
                                                    {:id "question-0-question-id",
                                                     :fail {:type "parallel-by-tag",
                                                            :from-params [{:template "inactivate-option-%-question-0-question-id",
                                                                           :param-property "value",
                                                                           :action-property "tag"}]},
                                                     :type "question-test",
                                                     :success {:type "parallel-by-tag",
                                                               :from-params [{:template "activate-option-%-question-0-question-id",
                                                                              :param-property "value",
                                                                              :action-property "tag"}]},
                                                     :from-params [{:param-property "value", :action-property "value"}]}
                                                    {:type "test-value",
                                                     :value1 "one",
                                                     :value2 "one",
                                                     :success "question-0-check-answers"}],
                                             :type "sequence-data"},
           :intro-r3 {:data [{:data [{:type "empty", :duration 0}
                                     {:phrase-text "Listen carefully to find out where to drag each crayon.",
                                      :start 21.54,
                                      :type "animation-sequence",
                                      :duration 4.08,
                                      :region-text "listen carefully to find out where to drag each",
                                      :audio "/upload/TCKFMRBQWQXGCKXR.m4a",
                                      :target "librarian-r3",
                                      :end 25.62,
                                      :data [{:end 24.32, :anim "talk", :start 21.55}
                                             {:end 25.4, :anim "talk", :start 24.48}]}],
                              :type "sequence-data"}],
                      :tags ["user-interactions-blocked"],
                      :type "sequence-data",
                      :phrase "continue-sorting",
                      :editor-type "dialog",
                      :phrase-description "Listen carefully, because it might be a different place than where you put it before!"},
           :next-say-r1 {:data [{:type "set-timeout", :action "say-color-r1", :interval 100}], :type "sequence-data"},
           :start-drag-r2 {:data [{:type "stop-transition",
                                   :from-params [{:param-property "target", :action-property "id"}]}
                                  {:type "set-variable", :var-name "purple-table-selected", :var-value false}
                                  {:type "set-variable", :var-name "orange-table-selected", :var-value false}
                                  {:type "set-variable", :var-name "green-table-selected", :var-value false}
                                  {:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "say", :var-value true}
                                  {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                  {:id "next-say-r2", :type "action"}
                                  {:id "next-check-collide-r2", :type "action"}],
                           :type "sequence-data"},
           :drag-crayon-r1 {:data [{:type "copy-variable",
                                    :var-name "current-selection-state",
                                    :from-params [{:param-property "check-variable", :action-property "from"}]}
                                   {:type "set-variable", :var-name "say", :var-value false}
                                   {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                   {:fail "wrong-option-r1",
                                    :type "test-var-scalar",
                                    :value true,
                                    :success "correct-option-r1",
                                    :var-name "current-selection-state"}],
                            :type "sequence-data"},
           :question-0-options-option-3-voice-over-inactivate {:data [{:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-3-voice-over-background"}
                                                                      {:id "default",
                                                                       :type "state",
                                                                       :target "question-0-options-option-3-voice-over-icon"}],
                                                               :tags ["inactivate-voice-overs-question-0-question-id"
                                                                      "inactivate-voice-over-option-4-question-0-question-id"],
                                                               :type "sequence-data"},
           :highlight-r2 {:data [{:type "set-variable",
                                  :var-value true,
                                  :from-params [{:param-property "check-variable", :action-property "var-name"}]}
                                 {:type "set-attribute",
                                  :attr-name "highlight",
                                  :attr-value true,
                                  :from-params [{:param-property "transition", :action-property "target"}]}],
                          :type "sequence-data"},
           :question-0-check-answers-wrong-answer {:data [{:type "empty", :duration 500}
                                                          {:id "question-0-check-answers-wrong-answer-dialog",
                                                           :type "action"}],
                                                   :type "sequence-data"},
           :question-0-options-option-2-voice-over-activate {:data [{:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-2-voice-over-background"}
                                                                    {:id "active",
                                                                     :type "state",
                                                                     :target "question-0-options-option-2-voice-over-icon"}],
                                                             :tags ["activate-voice-over-option-3-question-0-question-id"],
                                                             :type "sequence-data"},
           :intro-r1 {:data [{:data [{:type "empty", :duration 1000}
                                     {:phrase-text "Drag the crayons to the box that matches their color.",
                                      :start 2.37,
                                      :type "animation-sequence",
                                      :duration 3.8999999999999995,
                                      :region-text "drag the kranz to the box that matches their color",
                                      :audio "/upload/TCKFMRBQWQXGCKXR.m4a",
                                      :target "librarian-r0",
                                      :end 6.27,
                                      :data [{:end 6.19, :anim "talk", :start 2.46}]}],
                              :type "sequence-data"}],
                      :tags ["user-interactions-blocked"],
                      :type "sequence-data",
                      :phrase "intro",
                      :editor-type "dialog",
                      :phrase-description "Introduce task"},
           :unhighlight-all-r3 {:data [{:type "set-variable", :var-name "highlighted", :var-value false}
                                       {:type "set-attribute",
                                        :target "yellow-box-r3",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "blue-box-r3",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "red-box-r3",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "purple-table-r3",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "orange-table-r3",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "green-table-r3",
                                        :attr-name "highlight",
                                        :attr-value false}],
                                :type "parallel"},
           :instruction-2-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 105.06,
                                              :data [{:end 105, :anim "talk", :start 102.85}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 102.81,
                                              :target "librarian-r3",
                                              :duration 2.25,
                                              :phrase-text "Put the purple crayon on its table."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-2",
                              :editor-type "dialog",
                              :phrase-description "Put the purple crayon on its table."},
           :intermediate-action-1 {:data [{:id "init-technical-1", :type "action"}
                                          {:id "init-activity-r1", :type "action"}],
                                   :type "sequence-data"},
           :init-activity-r2 {:data [{:type "counter",
                                      :counter-id "sorted-crayons",
                                      :counter-value 0,
                                      :counter-action "reset"}
                                     {:id "intro-r2", :type "action"}],
                              :type "sequence-data"},
           :drag-crayon-r2 {:data [{:type "copy-variable",
                                    :var-name "current-selection-state",
                                    :from-params [{:param-property "check-variable", :action-property "from"}]}
                                   {:type "set-variable", :var-name "say", :var-value false}
                                   {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                   {:fail "wrong-option-r2",
                                    :type "test-var-scalar",
                                    :value true,
                                    :success "correct-option-r2",
                                    :var-name "current-selection-state"}],
                            :type "sequence-data"},
           :orange-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 10.11,
                                             :data [{:end 10.1, :anim "talk", :start 9.31}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 9.3,
                                             :duration 0.8099999999999987,
                                             :phrase-text "orange"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color orange",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color orange"},
           :question-0-task-text-group-button-activate {:data [{:id "active",
                                                                :type "state",
                                                                :target "question-0-task-text-group-button-background"}
                                                               {:id "active",
                                                                :type "state",
                                                                :target "question-0-task-text-group-button-icon"}],
                                                        :tags ["activate-voice-over-task-question-0-question-id"],
                                                        :type "sequence-data"},
           :question-0-option-voice-over {:data [{:type "parallel-by-tag",
                                                  :from-params [{:template "activate-voice-over-%-question-0-question-id",
                                                                 :param-property "value",
                                                                 :action-property "tag"}]}
                                                 {:type "case",
                                                  :options {:option-1 {:id "question-0-option-voice-over-voice-over-option-1",
                                                                       :type "action"},
                                                            :option-2 {:id "question-0-option-voice-over-voice-over-option-2",
                                                                       :type "action"},
                                                            :option-3 {:id "question-0-option-voice-over-voice-over-option-3",
                                                                       :type "action"},
                                                            :option-4 {:id "question-0-option-voice-over-voice-over-option-4",
                                                                       :type "action"}},
                                                  :from-params [{:param-property "value", :action-property "value"}]}
                                                 {:type "parallel-by-tag",
                                                  :from-params [{:template "inactivate-voice-over-%-question-0-question-id",
                                                                 :param-property "value",
                                                                 :action-property "tag"}]}],
                                          :type "sequence-data"},
           :orange-color-r1 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 10.11,
                                             :data [{:end 10.1, :anim "talk", :start 9.31}],
                                             :type "animation-sequence",
                                             :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                             :start 9.3,
                                             :duration 0.8099999999999987,
                                             :phrase-text "orange"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "Color orange",
                             :unique-tag "color",
                             :editor-type "dialog",
                             :phrase-description "Color orange"},
           :correct-answer-2-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.626,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.626,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 2",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 2"},
           :correct-answer-6-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.627,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.627,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 6",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 6"},
           :question-0-check-answers-correct-answer {:data [{:id "question-0-check-answers-correct-answer-dialog",
                                                             :type "action"}
                                                            {:id "question-0-hide", :type "action"}
                                                            {:tag "question-0-question-id", :type "finish-flows"}],
                                                     :type "sequence-data"},
           :intermediate-action-0 {:data [{:id "init-technical-0", :type "action"}
                                          {:id "init-activity-r0", :type "action"}],
                                   :type "sequence-data"},
           :finish-scene-r1 {:data [{:id "finish-dialog-r1", :type "action"}
                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                    {:id "finish-activity-r1", :type "action"}],
                             :type "sequence-data"},
           :question-0-show {:type "set-attribute", :target "question-0", :attr-name "visible", :attr-value true},
           :intermediate-action-2 {:data [{:id "init-technical-2", :type "action"}
                                          {:id "init-activity-r2", :type "action"}],
                                   :type "sequence-data"},
           :finish-activity-r0 {:data [{:id "init-technical-1", :type "action"}
                                       {:id "init-activity-r1", :type "action"}],
                                :type "sequence-data"},
           :finish-round-dialog-r2 {:data [{:data [{:type "empty", :duration 0} {:id "question-0", :type "action"}],
                                            :type "sequence-data"}
                                           {:data [{:type "empty", :duration 0}
                                                   {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                            :type "sequence-data"}],
                                    :type "sequence-data",
                                    :phrase "Finish round",
                                    :editor-type "dialog",
                                    :phrase-description "Finish round"},
           :wrong-answer-dialog-r3 {:data [{:data [{:type "empty", :duration 0}
                                                   {:end 0.469,
                                                    :data [],
                                                    :type "animation-sequence",
                                                    :audio "/upload/JYNSFQQINNQMSQCP.mp3",
                                                    :start 0,
                                                    :duration 0.469,
                                                    :phrase-text "incorrect sound",
                                                    :phrase-placeholder "Enter phrase text"}],
                                            :type "sequence-data"}
                                           {:data [{:type "empty", :duration 0}
                                                   {:phrase-text "try again",
                                                    :start 123.09,
                                                    :type "animation-sequence",
                                                    :duration 0.7800000000000011,
                                                    :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                    :target "librarian-r3",
                                                    :end 123.87,
                                                    :phrase-placeholder "Enter phrase text",
                                                    :data [{:end 123.39, :anim "talk", :start 123.25}]}],
                                            :type "sequence-data"}],
                                    :type "sequence-data",
                                    :phrase "wrong-answer-dialog",
                                    :editor-type "dialog",
                                    :phrase-description "Wrong answer dialog"},
           :init-technical-2 {:data [{:id "hide-technical", :type "state", :target "yellow-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "orange-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "orange-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "red-box-r1"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "green-box-r1"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "background-r1"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "yellow-box-r1"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "yellow-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "orange-box-r1"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-3-r1"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "yellow-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "orange-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-2-r1"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-1-r1"}
                                     {:id "hide-technical", :type "state", :target "blue-box-r1"}
                                     {:id "hide-technical", :type "state", :target "purple-box-r1"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "librarian-r2"}
                                     {:id "show-technical", :type "state", :target "red-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "red-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "blue-box-r2"}
                                     {:id "show-technical", :type "state", :target "red-box-r2"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "yellow-box-r2"}
                                     {:id "show-technical", :type "state", :target "green-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "green-table-r2"}
                                     {:id "show-technical", :type "state", :target "orange-table-r2"}
                                     {:id "show-technical", :type "state", :target "green-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "purple-table-r2"}
                                     {:id "show-technical", :type "state", :target "green-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-1-r2"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "layered-background-r2"}
                                     {:id "show-technical", :type "state", :target "red-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-3-r2"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-2-r2"}],
                              :type "parallel"},
           :next-check-collide-r1 {:data [{:type "set-timeout", :action "check-collide-r1", :interval 10}],
                                   :type "sequence-data"},
           :tap-instructions-r3 {:type "action", :from-var [{:var-name "instruction", :action-property "id"}]},
           :wrong-answer-r3 {:data [{:id "unhighlight-all-r3", :type "action"}
                                    {:id "object-revert-r3", :type "action"}
                                    {:type "counter", :counter-id "wrong-answers-counter", :counter-action "increase"}
                                    {:type "test-var-inequality",
                                     :value 2,
                                     :success "blink-objects-r3",
                                     :var-name "wrong-answers-counter",
                                     :inequality ">="}
                                    {:data [{:id "wrong-answer-dialog-r3", :type "action"}
                                            {:type "action",
                                             :from-var [{:var-name "instruction", :action-property "id"}]}],
                                     :type "sequence-data"}],
                             :type "parallel"},
           :wrong-answer-r1 {:data [{:data [{:type "empty", :duration 0}
                                            {:end 0.412,
                                             :data [],
                                             :type "animation-sequence",
                                             :audio "/upload/JYNSFQQINNQMSQCP.mp3",
                                             :start 0,
                                             :duration 0.412,
                                             :phrase-text "incorrect sound"}],
                                     :type "sequence-data"}],
                             :type "sequence-data",
                             :phrase "wrong-answer",
                             :editor-type "dialog",
                             :phrase-description "wrong answer"},
           :question-0-check-answers-wrong-answer-dialog {:data [{:data [{:type "empty", :duration 0}
                                                                         {:end 80.16,
                                                                          :data [{:end 77.83,
                                                                                  :anim "talk",
                                                                                  :start 74.39}
                                                                                 {:end 80.13,
                                                                                  :anim "talk",
                                                                                  :start 78.32}],
                                                                          :type "animation-sequence",
                                                                          :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                                          :start 74.37,
                                                                          :duration 5.789999999999992,
                                                                          :phrase-text "Hmmm, I see a red crayon and a  purple crayon. What is the same about both of them?",
                                                                          :region-text "whom i see a red crayon and a purple crayon what is the same about both of them"}],
                                                                  :type "sequence-data"}],
                                                          :type "sequence-data",
                                                          :editor-type "dialog",
                                                          :phrase-description "Wrong answer"},
           :question-0-task-text-group-button-inactivate {:data [{:id "default",
                                                                  :type "state",
                                                                  :target "question-0-task-text-group-button-background"}
                                                                 {:id "default",
                                                                  :type "state",
                                                                  :target "question-0-task-text-group-button-icon"}],
                                                          :tags ["inactivate-voice-overs-question-0-question-id"
                                                                 "inactivate-voice-over-task-question-0-question-id"],
                                                          :type "sequence-data"},
           :blue-color-r3 {:data [{:data [{:type "empty", :duration 0}
                                          {:end 5.82,
                                           :data [{:end 5.78, :anim "talk", :start 5.17}],
                                           :type "animation-sequence",
                                           :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                           :start 5.07,
                                           :duration 0.75,
                                           :phrase-text "blue"}],
                                   :type "sequence-data"}],
                           :type "sequence-data",
                           :phrase "Color blue",
                           :unique-tag "color",
                           :editor-type "dialog",
                           :phrase-description "Color blue"},
           :unhighlight-r2 {:data [{:type "set-variable",
                                    :var-value false,
                                    :from-params [{:param-property "check-variable", :action-property "var-name"}]}
                                   {:type "set-attribute",
                                    :attr-name "highlight",
                                    :attr-value false,
                                    :from-params [{:param-property "transition", :action-property "target"}]}],
                            :type "sequence-data"},
           :finish-dialog-r1 {:data [{:data [{:type "empty", :duration 0}
                                             {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                      :type "sequence-data"}],
                              :type "sequence-data",
                              :phrase "finish-dialog",
                              :editor-type "dialog",
                              :phrase-description "finish dialog"},
           :next-say-r3 {:data [{:type "set-timeout", :action "say-color-r3", :interval 100}], :type "sequence-data"},
           :crayon-revert-r1 {:type "transition",
                              :from-params [{:param-property "target", :action-property "transition-id"}
                                            {:param-property "init-position", :action-property "to"}]},
           :crayon-in-right-box-r2 {:type "set-attribute",
                                    :attr-name "visible",
                                    :attr-value false,
                                    :from-params [{:param-property "target", :action-property "target"}]},
           :wrong-option-r1 {:data [{:id "unhighlight-all-r1", :type "action"}
                                    {:id "crayon-revert-r1", :type "action"}
                                    {:id "wrong-answer-r1", :type "action"}],
                             :type "parallel"},
           :correct-answer-3-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.627,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.627,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 3",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 3"},
           :question-0-check-answers {:data [{:id "question-0-question-id",
                                              :fail "question-0-check-answers-wrong-answer",
                                              :type "question-check",
                                              :answer ["option-2"],
                                              :success "question-0-check-answers-correct-answer"}
                                             {:id "question-0-question-id", :type "question-reset"}
                                             {:tag "inactivate-options-question-0-question-id", :type "parallel-by-tag"}],
                                      :type "sequence-data"},
           :question-0 {:data [{:id "question-0-show", :type "action"} {:id "question-0-task-dialog", :type "action"}],
                        :tags ["question-0-question-id"],
                        :type "sequence-data",
                        :description "-- Description --",
                        :workflow-user-input true},
           :green-color-r2 {:data [{:data [{:type "empty", :duration 0}
                                           {:end 8.85,
                                            :data [{:end 8.75, :anim "talk", :start 7.99}],
                                            :type "animation-sequence",
                                            :audio "/upload/UGMZCSAYDMJPTISA.mp3",
                                            :start 7.98,
                                            :duration 0.8699999999999992,
                                            :phrase-text "green"}],
                                    :type "sequence-data"}],
                            :type "sequence-data",
                            :phrase "Color green",
                            :unique-tag "color",
                            :editor-type "dialog",
                            :phrase-description "Color green"},
           :task-2-r3 {:data [{:type "set-variable", :var-name "object-1", :var-value "purple-crayon-r3"}
                              {:type "set-variable", :var-name "object-2", :var-value "purple-table-r3"}
                              {:type "set-variable",
                               :var-name "check-collide",
                               :var-value ["colliding-object-purple-crayon-r3" "colliding-purple-table-r3"]}
                              {:type "set-variable", :var-name "next-task", :var-value "task-3-r3"}
                              {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-2-r3"}
                              {:type "set-variable", :var-name "instruction", :var-value "instruction-2-r3"}
                              {:type "counter", :counter-id "wrong-answers-counter", :counter-action "reset"}
                              {:id "instruction-2-r3", :type "action"}],
                       :type "sequence-data"},
           :say-color-r2 {:data [{:type "action", :from-params [{:param-property "say-color", :action-property "id"}]}
                                 {:type "test-var-scalar", :value true, :success "next-say-r2", :var-name "say"}],
                          :type "sequence-data"},
           :instruction-3-r3 {:data [{:data [{:type "empty", :duration "1000"}
                                             {:end 109.41,
                                              :data [{:end 109.36, :anim "talk", :start 106.55}],
                                              :type "animation-sequence",
                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                              :start 106.53,
                                              :target "librarian-r3",
                                              :duration 2.8799999999999955,
                                              :phrase-text "Put the yellow crayon in its crayon box."}],
                                      :type "sequence-data"}],
                              :tags ["user-interactions-blocked"],
                              :type "sequence-data",
                              :phrase "instruction-3",
                              :editor-type "dialog",
                              :phrase-description "Put the yellow crayon in its crayon box."},
           :start-drag-r1 {:data [{:type "stop-transition",
                                   :from-params [{:param-property "target", :action-property "id"}]}
                                  {:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "purple-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "orange-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "green-box-selected", :var-value false}
                                  {:type "set-variable", :var-name "say", :var-value true}
                                  {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                  {:id "next-say-r1", :type "action"}
                                  {:id "next-check-collide-r1", :type "action"}],
                           :type "sequence-data"},
           :check-next-check-collide-r3 {:type "test-var-scalar",
                                         :value true,
                                         :success "next-check-collide-r3",
                                         :var-name "next-check-collide"},
           :unhighlight-r1 {:data [{:type "set-variable",
                                    :var-value false,
                                    :from-params [{:param-property "check-variable", :action-property "var-name"}]}
                                   {:type "set-attribute",
                                    :attr-name "highlight",
                                    :attr-value false,
                                    :from-params [{:param-property "transition", :action-property "target"}]}],
                            :type "sequence-data"},
           :check-collide-r3 {:data [{:fail "unhighlight-r3",
                                      :type "test-transitions-and-pointer-collide",
                                      :success "highlight-r3",
                                      :from-params [{:param-property "colliders", :action-property "transitions"}]}
                                     {:id "check-next-check-collide-r3", :type "action"}],
                              :type "sequence-data"},
           :stop-drag-highlighted-r3 {:fail "wrong-answer-r3",
                                      :type "test-var-list",
                                      :values [true true],
                                      :success "correct-answer-single-r3",
                                      :from-var [{:var-name "check-collide", :action-property "var-names"}],
                                      :var-names ["check-collide-1" "check-collide-2"]},
           :correct-answer-r2 {:data [{:data [{:type "empty", :duration 0}
                                              {:end 0.111,
                                               :data [],
                                               :type "animation-sequence",
                                               :audio "/upload/IYPUFFYJAQHCFWTA.mp3",
                                               :start 0.046,
                                               :duration 0.065,
                                               :phrase-text "snap into place sound"}],
                                       :type "sequence-data"}],
                               :type "sequence-data",
                               :phrase "correct-answer",
                               :editor-type "dialog",
                               :phrase-description "correct answer"},
           :finish-r3 {:data [{:id "finish-dialog-r3", :type "action"} {:id "finish-activity-r3", :type "action"}],
                       :type "sequence-data"},
           :init-technical-3 {:data [{:id "hide-technical", :type "state", :target "orange-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "librarian-r2"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-2-r2"}
                                     {:id "hide-technical", :type "state", :target "orange-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-2-r2"}
                                     {:id "hide-technical", :type "state", :target "yellow-crayon-2-r2"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "yellow-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "blue-box-r2"}
                                     {:id "hide-technical", :type "state", :target "red-box-r2"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "yellow-box-r2"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-2-r2"}
                                     {:id "hide-technical", :type "state", :target "green-table-r2"}
                                     {:id "hide-technical", :type "state", :target "orange-table-r2"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "blue-crayon-2-r2"}
                                     {:id "hide-technical", :type "state", :target "purple-table-r2"}
                                     {:id "hide-technical", :type "state", :target "green-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-1-r2"}
                                     {:id "hide-technical", :type "state", :target "purple-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "layered-background-r2"}
                                     {:id "hide-technical", :type "state", :target "red-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "yellow-crayon-3-r2"}
                                     {:id "hide-technical", :type "state", :target "orange-crayon-2-r2"}
                                     {:id "show-technical", :type "state", :target "red-box-r3"}
                                     {:id "show-technical", :type "state", :target "yellow-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "green-table-r3"}
                                     {:id "show-technical", :type "state", :target "purple-table-r3"}
                                     {:id "show-technical", :type "state", :target "blue-box-r3"}
                                     {:id "show-technical", :type "state", :target "orange-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "yellow-box-r3"}
                                     {:id "show-technical", :type "state", :target "red-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "green-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "layered-background-r3"}
                                     {:id "show-technical", :type "state", :target "librarian-r3"}
                                     {:id "show-technical", :type "state", :target "blue-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "purple-crayon-r3"}
                                     {:id "show-technical", :type "state", :target "orange-table-r3"}],
                              :type "parallel"},
           :question-0-option-voice-over-voice-over-option-4 {:data [{:data [{:type "empty", :duration 0}
                                                                             {:end 73.71,
                                                                              :data [{:end 73.67,
                                                                                      :anim "talk",
                                                                                      :start 72.43}],
                                                                              :type "animation-sequence",
                                                                              :audio "/upload/VBIMTMHQXEWJOVXK.mp3",
                                                                              :start 72.39,
                                                                              :duration 1.3199999999999932,
                                                                              :phrase-text "They are both books.",
                                                                              :region-text "they are both books"}],
                                                                      :type "sequence-data"}],
                                                              :type "sequence-data",
                                                              :editor-type "dialog",
                                                              :phrase-description "Option \"Option 4\" voice-over"},
           :crayon-revert-r2 {:type "transition",
                              :from-params [{:param-property "target", :action-property "transition-id"}
                                            {:param-property "init-position", :action-property "to"}]},
           :unhighlight-all-r1 {:data [{:type "set-attribute",
                                        :target "yellow-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "blue-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "red-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "purple-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "orange-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}
                                       {:type "set-attribute",
                                        :target "green-box-r1",
                                        :attr-name "highlight",
                                        :attr-value false}],
                                :type "parallel"},
           :intermediate-action-3 {:data [{:id "init-technical-3", :type "action"}
                                          {:id "start-activity-r3", :type "action"}],
                                   :type "sequence-data"},
           :correct-answer-1-r3 {:data [{:data [{:type "empty", :duration 0}
                                                {:end 0.627,
                                                 :data [],
                                                 :type "animation-sequence",
                                                 :audio "/upload/SIXWHSICTJUWRKRH.mp3",
                                                 :start 0,
                                                 :duration 0.627,
                                                 :phrase-text "correct"}],
                                         :type "sequence-data"}],
                                 :type "sequence-data",
                                 :phrase "Correct answer for task 1",
                                 :editor-type "dialog",
                                 :phrase-description "Correct answer for task 1"}},
 :objects {:blue-table-r0 {:x 330,
                           :y 667,
                           :src "/raw/img/categorize/blue_table.png",
                           :type "image",
                           :scale 0.8,
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}}},
           :orange-crayon-1-r2 {:y 850,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "orange-table-r2",
                                                              :target "orange-crayon-1-r2",
                                                              :init-position {:x 746, :y 850, :duration 1},
                                                              :check-variable "orange-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-1-r2",
                                                                :say-color "orange-color-r2"}}},
                                :draggable true,
                                :x 746,
                                :visible false},
           :yellow-crayon-1-r1 {:y 521,
                                :rotation -129,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "yellow-crayon-1-r1",
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "yellow-crayon-1-r1",
                                                              :init-position {:x 145, :y 521, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-1-r1",
                                                                :say-color "yellow-color-r1",
                                                                :crayon-color "yellow"}}},
                                :draggable true,
                                :x 145,
                                :visible false},
           :red-box-r3 {:x 500,
                        :y 506,
                        :src "/raw/img/categorize/red_box_small.png",
                        :type "image",
                        :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                        :visible false},
           :question-0-options-option-0-voice-over-icon {:y 24,
                                                         :states {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                         :fill "#000000",
                                                         :width 41,
                                                         :type "svg-path",
                                                         :x 19.5,
                                                         :scene-name "letter-tutorial-trace",
                                                         :height 32,
                                                         :data "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
           :question-0-options-option-0-text {:y 50,
                                              :vertical-align "middle",
                                              :font-size 48,
                                              :word-wrap true,
                                              :width 720,
                                              :editable? {:select true},
                                              :type "text",
                                              :actions {:click {:id "question-0-option-click-handler",
                                                                :on "click",
                                                                :type "action",
                                                                :params {:value "option-1"},
                                                                :unique-tag "question-action"}},
                                              :chunks [{:end 4, :start 0}
                                                       {:end 8, :start 5}
                                                       {:end 13, :start 9}
                                                       {:end 18, :start 14}],
                                              :x 130,
                                              :text "They are both red."},
           :question-0-options-option-2-voice-over-icon {:y 24,
                                                         :states {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                         :fill "#000000",
                                                         :width 41,
                                                         :type "svg-path",
                                                         :x 19.5,
                                                         :scene-name "letter-tutorial-trace",
                                                         :height 32,
                                                         :data "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
           :librarian-r2 {:y 1000,
                          :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                          :speed 0.3,
                          :scale {:x 0.8, :y 0.8},
                          :name "senoravaca",
                          :width 351,
                          :start true,
                          :editable? {:select true, :show-in-tree? true},
                          :type "animation",
                          :actions {:click {:id "tap-instructions-r2", :on "click", :type "action"}},
                          :anim "idle",
                          :x 250,
                          :visible false,
                          :skin "lion",
                          :height 717},
           :red-crayon-2-r2 {:y 958,
                             :rotation -90,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :scale 0.35,
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r2",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:box "red-box-r2",
                                                           :target "red-crayon-2-r2",
                                                           :init-position {:x 1618, :y 958, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r2",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-2-r2", :say-color "red-color-r2"}}},
                             :draggable true,
                             :x 1618,
                             :visible false},
           :question-0-options-option-0-voice-over-background {:x 0,
                                                               :y 0,
                                                               :fill 16777215,
                                                               :type "rectangle",
                                                               :width 80,
                                                               :height 80,
                                                               :states {:active {:fill 45823},
                                                                        :default {:fill 16777215}},
                                                               :border-radius 40},
           :yellow-crayon-r3 {:y 139,
                              :rotation -90,
                              :states {:init-position {:x 154, :y 139, :visible true},
                                       :hide-technical {:visible false},
                                       :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/yellow_crayons.png",
                              :actions {:drag-end {:id "stop-drag-hide-r3",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:self "yellow-crayon-r3",
                                                            :target "yellow-box-r3",
                                                            :colliders ["yellow-box-r3"
                                                                        "blue-box-r3"
                                                                        "red-box-r3"
                                                                        "purple-table-r3"
                                                                        "orange-table-r3"
                                                                        "green-table-r3"],
                                                            :init-position {:x 154, :y 139, :duration 1}}},
                                        :drag-start {:id "start-drag-r3",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:self "yellow-crayon-r3",
                                                              :target "yellow-box-r3",
                                                              :colliders ["yellow-box-r3"
                                                                          "blue-box-r3"
                                                                          "red-box-r3"
                                                                          "purple-table-r3"
                                                                          "orange-table-r3"
                                                                          "green-table-r3"],
                                                              :say-color "yellow-color-r3"}}},
                              :draggable true,
                              :x 154,
                              :visible false},
           :green-table-r3 {:x 330,
                            :y 667,
                            :src "/raw/img/categorize/green_table.png",
                            :type "image",
                            :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                            :visible false},
           :orange-crayon-3-r1 {:y 144,
                                :rotation 135,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "orange-crayon-3-r1",
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "orange-crayon-3-r1",
                                                              :init-position {:x 1753, :y 144, :duration 1},
                                                              :check-variable "orange-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-3-r1",
                                                                :say-color "orange-color-r1",
                                                                :crayon-color "orange"}}},
                                :draggable true,
                                :x 1753,
                                :visible false},
           :purple-table-r3 {:x 1120,
                             :y 652,
                             :src "/raw/img/categorize/purple_table.png",
                             :type "image",
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :visible false},
           :orange-crayon-1-r1 {:y 178,
                                :rotation 37,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "orange-crayon-1-r1",
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "orange-crayon-1-r1",
                                                              :init-position {:x 134, :y 178, :duration 1},
                                                              :check-variable "orange-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-1-r1",
                                                                :say-color "orange-color-r1",
                                                                :crayon-color "orange"}}},
                                :draggable true,
                                :x 134,
                                :visible false},
           :red-box-r1 {:x 675,
                        :y 810,
                        :src "/raw/img/categorize/red_box.png",
                        :type "image",
                        :scale 0.65,
                        :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                        :visible false},
           :purple-crayon-1-r1 {:y 521,
                                :rotation -39,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "purple-crayon-1-r1",
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "purple-crayon-1-r1",
                                                              :init-position {:x 145, :y 521, :duration 1},
                                                              :check-variable "purple-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-1-r1",
                                                                :say-color "purple-color-r1",
                                                                :crayon-color "purple"}}},
                                :draggable true,
                                :x 145,
                                :visible false},
           :orange-crayon-3-r2 {:y 238,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "orange-table-r2",
                                                              :target "orange-crayon-3-r2",
                                                              :init-position {:x 317, :y 238, :duration 1},
                                                              :check-variable "orange-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-3-r2",
                                                                :say-color "orange-color-r2"}}},
                                :draggable true,
                                :x 317,
                                :visible false},
           :blue-box-r3 {:x 1352,
                         :y 490,
                         :src "/raw/img/categorize/blue_box_small.png",
                         :type "image",
                         :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                         :visible false},
           :purple-crayon-2-r2 {:y 236,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "purple-table-r2",
                                                              :target "purple-crayon-2-r2",
                                                              :init-position {:x 1271, :y 236, :duration 1},
                                                              :check-variable "purple-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-2-r2",
                                                                :say-color "purple-color-r2"}}},
                                :draggable true,
                                :x 1271,
                                :visible false},
           :green-box-r1 {:x 1575,
                          :y 810,
                          :src "/raw/img/categorize/green_box.png",
                          :type "image",
                          :scale 0.65,
                          :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                          :visible false},
           :red-crayon-3-r1 {:y 106,
                             :rotation -52,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :transition "red-crayon-3-r1",
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r1",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:target "red-crayon-3-r1",
                                                           :init-position {:x 1097, :y 106, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r1",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-3-r1",
                                                             :say-color "red-color-r1",
                                                             :crayon-color "red"}}},
                             :draggable true,
                             :x 1097,
                             :visible false},
           :question-0-options-option-0-voice-over {:x 0,
                                                    :y 0,
                                                    :type "group",
                                                    :actions {:click {:id "question-0-option-voice-over",
                                                                      :on "click",
                                                                      :type "action",
                                                                      :params {:value "option-1"},
                                                                      :unique-tag "question-action"}},
                                                    :children ["question-0-options-option-0-voice-over-background"
                                                               "question-0-options-option-0-voice-over-icon"]},
           :question-0-background {:x 0,
                                   :y 412.5286478227654,
                                   :fill 16753409,
                                   :type "rectangle",
                                   :width 1920,
                                   :height 667.4713521772346,
                                   :object-name "question-0-background"},
           :yellow-crayon-2-r2 {:y 126,
                                :rotation -90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "yellow-box-r2",
                                                              :target "yellow-crayon-2-r2",
                                                              :init-position {:x 1171, :y 126, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-2-r2",
                                                                :say-color "yellow-color-r2"}}},
                                :draggable true,
                                :x 1171,
                                :visible false},
           :orange-crayon-r3 {:y 914,
                              :rotation -90,
                              :states {:init-position {:x 1495, :y 914},
                                       :hide-technical {:visible false},
                                       :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/orange_crayons.png",
                              :actions {:drag-end {:id "stop-drag-hide-r3",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:self "orange-crayon-r3",
                                                            :target "orange-table-r3",
                                                            :colliders ["yellow-box-r3"
                                                                        "blue-box-r3"
                                                                        "red-box-r3"
                                                                        "purple-table-r3"
                                                                        "orange-table-r3"
                                                                        "green-table-r3"],
                                                            :init-position {:x 1495, :y 914, :duration 1}}},
                                        :drag-start {:id "start-drag-r3",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:self "orange-crayon-r3",
                                                              :target "orange-table-r3",
                                                              :colliders ["yellow-box-r3"
                                                                          "blue-box-r3"
                                                                          "red-box-r3"
                                                                          "purple-table-r3"
                                                                          "orange-table-r3"
                                                                          "green-table-r3"],
                                                              :say-color "orange-color-r3"}}},
                              :draggable true,
                              :x 1495,
                              :visible false},
           :background-r1 {:src "/raw/img/categorize/background.png",
                           :type "background",
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :yellow-box-r3 {:x 943,
                           :y 628,
                           :src "/raw/img/categorize/yellow_box_small.png",
                           :type "image",
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :question-0-options-option-1-text {:y 50,
                                              :vertical-align "middle",
                                              :font-size 48,
                                              :word-wrap true,
                                              :width 720,
                                              :editable? {:select true},
                                              :type "text",
                                              :actions {:click {:id "question-0-option-click-handler",
                                                                :on "click",
                                                                :type "action",
                                                                :params {:value "option-2"},
                                                                :unique-tag "question-action"}},
                                              :chunks [{:end 4, :start 0}
                                                       {:end 8, :start 5}
                                                       {:end 13, :start 9}
                                                       {:end 21, :start 14}
                                                       {:end 25, :start 22}
                                                       {:end 29, :start 26}
                                                       {:end 33, :start 30}
                                                       {:end 36, :start 34}
                                                       {:end 43, :start 37}],
                                              :x 130,
                                              :text "They are both crayons you can use to color."},
           :red-crayon-1-r2 {:y 500,
                             :rotation -90,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :scale 0.35,
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r2",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:box "red-box-r2",
                                                           :target "red-crayon-1-r2",
                                                           :init-position {:x 924, :y 500, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r2",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-1-r2", :say-color "red-color-r2"}}},
                             :draggable true,
                             :x 924,
                             :visible false},
           :yellow-crayon-1-r2 {:y 691,
                                :rotation -90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "yellow-box-r2",
                                                              :target "yellow-crayon-1-r2",
                                                              :init-position {:x 764, :y 691, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-1-r2",
                                                                :say-color "yellow-color-r2"}}},
                                :draggable true,
                                :x 764,
                                :visible false},
           :yellow-table-r0 {:x 745,
                             :y 773,
                             :src "/raw/img/categorize/yellow_table.png",
                             :type "image",
                             :scale 0.8,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}}},
           :blue-box-r2 {:x 943,
                         :y 628,
                         :src "/raw/img/categorize/blue_box_small.png",
                         :type "image",
                         :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                         :visible false},
           :red-crayon-2-r1 {:y 387,
                             :rotation -69,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :transition "red-crayon-2-r1",
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r1",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:target "red-crayon-2-r1",
                                                           :init-position {:x 736, :y 387, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r1",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-2-r1",
                                                             :say-color "red-color-r1",
                                                             :crayon-color "red"}}},
                             :draggable true,
                             :x 736,
                             :visible false},
           :red-table-r0 {:x 1160,
                          :y 652,
                          :src "/raw/img/categorize/red_table.png",
                          :type "image",
                          :scale 0.8,
                          :states {:hide-technical {:visible false}, :show-technical {:visible true}}},
           :question-0-options-option-3-text {:y 50,
                                              :vertical-align "middle",
                                              :font-size 48,
                                              :word-wrap true,
                                              :width 720,
                                              :editable? {:select true},
                                              :type "text",
                                              :actions {:click {:id "question-0-option-click-handler",
                                                                :on "click",
                                                                :type "action",
                                                                :params {:value "option-4"},
                                                                :unique-tag "question-action"}},
                                              :chunks [{:end 4, :start 0}
                                                       {:end 8, :start 5}
                                                       {:end 13, :start 9}
                                                       {:end 20, :start 14}],
                                              :x 130,
                                              :text "They are both books."},
           :red-box-r2 {:x 1352,
                        :y 490,
                        :src "/raw/img/categorize/red_box_small.png",
                        :type "image",
                        :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                        :visible false},
           :blue-crayon-3-r2 {:y 143,
                              :rotation -90,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r2",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:box "blue-box-r2",
                                                            :target "blue-crayon-3-r2",
                                                            :init-position {:x 17, :y 143, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r2",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-3-r2", :say-color "blue-color-r2"}}},
                              :draggable true,
                              :x 17,
                              :visible false},
           :question-0-options-option-2 {:x 60,
                                         :y 363.7356760886173,
                                         :type "group",
                                         :children ["question-0-options-option-2-substrate"
                                                    "question-0-options-option-2-voice-over"
                                                    "question-0-options-option-2-text"]},
           :red-crayon-r3 {:y 501,
                           :rotation -90,
                           :states {:init-position {:x 776, :y 501},
                                    :hide-technical {:visible false},
                                    :show-technical {:visible true}},
                           :scale 0.35,
                           :type "image",
                           :src "/raw/img/categorize/red_crayons.png",
                           :actions {:drag-end {:id "stop-drag-hide-r3",
                                                :on "drag-end",
                                                :type "action",
                                                :params {:self "red-crayon-r3",
                                                         :target "red-box-r3",
                                                         :colliders ["yellow-box-r3"
                                                                     "blue-box-r3"
                                                                     "red-box-r3"
                                                                     "purple-table-r3"
                                                                     "orange-table-r3"
                                                                     "green-table-r3"],
                                                         :init-position {:x 776, :y 501, :duration 1}}},
                                     :drag-start {:id "start-drag-r3",
                                                  :on "drag-start",
                                                  :type "action",
                                                  :params {:self "red-crayon-r3",
                                                           :target "red-box-r3",
                                                           :colliders ["yellow-box-r3"
                                                                       "blue-box-r3"
                                                                       "red-box-r3"
                                                                       "purple-table-r3"
                                                                       "orange-table-r3"
                                                                       "green-table-r3"],
                                                           :say-color "red-color-r3"}}},
                           :draggable true,
                           :x 776,
                           :visible false},
           :yellow-box-r1 {:x 75,
                           :y 810,
                           :src "/raw/img/categorize/yellow_box.png",
                           :type "image",
                           :scale 0.65,
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :question-0-task-text {:y 0,
                                  :vertical-align "top",
                                  :font-size 48,
                                  :word-wrap true,
                                  :width 956.6157372039727,
                                  :editable? {:select true},
                                  :type "text",
                                  :chunks [{:end 4, :start 0}
                                           {:end 7, :start 5}
                                           {:end 11, :start 8}
                                           {:end 16, :start 12}
                                           {:end 22, :start 17}
                                           {:end 28, :start 23}
                                           {:end 32, :start 29}
                                           {:end 41, :start 33}],
                                  :x 110,
                                  :text "What is the same about these two crayons?"},
           :blue-crayon-1-r2 {:y 1050,
                              :rotation -90,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r2",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:box "blue-box-r2",
                                                            :target "blue-crayon-1-r2",
                                                            :init-position {:x 46, :y 1050, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r2",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-1-r2", :say-color "blue-color-r2"}}},
                              :draggable true,
                              :x 46,
                              :visible false},
           :blue-crayon-2-r1 {:y 456,
                              :rotation 125,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :transition "blue-crayon-2-r1",
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r1",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:target "blue-crayon-2-r1",
                                                            :init-position {:x 1406, :y 456, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r1",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-2-r1",
                                                              :say-color "blue-color-r1",
                                                              :crayon-color "blue"}}},
                              :draggable true,
                              :x 1406,
                              :visible false},
           :yellow-box-r2 {:x 500,
                           :y 506,
                           :src "/raw/img/categorize/yellow_box_small.png",
                           :type "image",
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :question-0-task-text-group-button-background {:x 0,
                                                          :y 0,
                                                          :fill 16777215,
                                                          :type "rectangle",
                                                          :width 80,
                                                          :height 80,
                                                          :states {:active {:fill 45823}, :default {:fill 16777215}},
                                                          :border-radius 40},
           :green-crayon-r3 {:y 263,
                             :rotation -90,
                             :states {:init-position {:x 19, :y 263},
                                      :hide-technical {:visible false},
                                      :show-technical {:visible true}},
                             :scale 0.35,
                             :type "image",
                             :src "/raw/img/categorize/green_crayons.png",
                             :actions {:drag-end {:id "stop-drag-hide-r3",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:self "green-crayon-r3",
                                                           :target "green-table-r3",
                                                           :colliders ["yellow-box-r3"
                                                                       "blue-box-r3"
                                                                       "red-box-r3"
                                                                       "purple-table-r3"
                                                                       "orange-table-r3"
                                                                       "green-table-r3"],
                                                           :init-position {:x 19, :y 263, :duration 1}}},
                                       :drag-start {:id "start-drag-r3",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:self "green-crayon-r3",
                                                             :target "green-table-r3",
                                                             :colliders ["yellow-box-r3"
                                                                         "blue-box-r3"
                                                                         "red-box-r3"
                                                                         "purple-table-r3"
                                                                         "orange-table-r3"
                                                                         "green-table-r3"],
                                                             :say-color "green-color-r3"}}},
                             :draggable true,
                             :x 19,
                             :visible false},
           :green-crayon-2-r2 {:y 818,
                               :rotation 90,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :scale 0.35,
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r2",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:box "green-table-r2",
                                                             :target "green-crayon-2-r2",
                                                             :init-position {:x 1418, :y 818, :duration 1},
                                                             :check-variable "green-table-selected"}},
                                         :drag-start {:id "start-drag-r2",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-2-r2", :say-color "green-color-r2"}}},
                               :draggable true,
                               :x 1418,
                               :visible false},
           :question-0-options-option-2-substrate {:y 0,
                                                   :states {:active {:border-color 45823}, :default {:border-color 0}},
                                                   :fill 16777215,
                                                   :width 760,
                                                   :type "rectangle",
                                                   :actions {:click {:id "question-0-option-click-handler",
                                                                     :on "click",
                                                                     :type "action",
                                                                     :params {:value "option-3"},
                                                                     :unique-tag "question-action"}},
                                                   :border-width 2,
                                                   :border-color 0,
                                                   :x 110,
                                                   :border-radius 20,
                                                   :height 100},
           :question-0-options {:x 0,
                                :y 412.5286478227654,
                                :type "group",
                                :children ["question-0-options-option-0"
                                           "question-0-options-option-1"
                                           "question-0-options-option-2"
                                           "question-0-options-option-3"]},
           :green-table-r2 {:x 330,
                            :y 667,
                            :src "/raw/img/categorize/green_table.png",
                            :type "image",
                            :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                            :visible false},
           :question-0-options-option-1 {:x 990,
                                         :y 203.7356760886173,
                                         :type "group",
                                         :children ["question-0-options-option-1-substrate"
                                                    "question-0-options-option-1-voice-over"
                                                    "question-0-options-option-1-text"]},
           :green-crayon-3-r1 {:y 106,
                               :rotation 30,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :transition "green-crayon-3-r1",
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r1",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:target "green-crayon-3-r1",
                                                             :init-position {:x 1097, :y 106, :duration 1},
                                                             :check-variable "green-box-selected"}},
                                         :drag-start {:id "start-drag-r1",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-3-r1",
                                                               :say-color "green-color-r1",
                                                               :crayon-color "green"}}},
                               :draggable true,
                               :x 1097,
                               :visible false},
           :yellow-crayon-3-r1 {:y 558,
                                :rotation -120,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "yellow-crayon-3-r1",
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "yellow-crayon-3-r1",
                                                              :init-position {:x 1033, :y 558, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-3-r1",
                                                                :say-color "yellow-color-r1",
                                                                :crayon-color "yellow"}}},
                                :draggable true,
                                :x 1033,
                                :visible false},
           :orange-box-r1 {:x 1275,
                           :y 810,
                           :src "/raw/img/categorize/orange_box.png",
                           :type "image",
                           :scale 0.65,
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :question-0-task-image {:y 206.2643239113827,
                                   :max-height 292.5286478227654,
                                   :scale {:x 1, :y 1},
                                   :editable? {:select true},
                                   :type "image",
                                   :src "/upload/XYYMRKSNYSJMCNNS.png",
                                   :max-width 613.3842627960273,
                                   :origin {:type "center-center"},
                                   :x 366.69213139801366},
           :blue-crayon-3-r1 {:y 144,
                              :rotation 45,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :transition "blue-crayon-3-r1",
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r1",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:target "blue-crayon-3-r1",
                                                            :init-position {:x 1753, :y 144, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r1",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-3-r1",
                                                              :say-color "blue-color-r1",
                                                              :crayon-color "blue"}}},
                              :draggable true,
                              :x 1753,
                              :visible false},
           :orange-table-r2 {:x 1120,
                             :y 652,
                             :src "/raw/img/categorize/orange_table.png",
                             :type "image",
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :visible false},
           :layered-background-r3 {:type "layered-background",
                                   :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                   :surface {:src "/raw/img/categorize/02.png"},
                                   :visible false,
                                   :background {:src "/raw/img/categorize/background-3.png"},
                                   :decoration {:src "/raw/img/categorize/03.png"}},
           :question-0-options-option-1-voice-over {:x 0,
                                                    :y 0,
                                                    :type "group",
                                                    :actions {:click {:id "question-0-option-voice-over",
                                                                      :on "click",
                                                                      :type "action",
                                                                      :params {:value "option-2"},
                                                                      :unique-tag "question-action"}},
                                                    :children ["question-0-options-option-1-voice-over-background"
                                                               "question-0-options-option-1-voice-over-icon"]},
           :purple-crayon-3-r1 {:y 558,
                                :rotation -30,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "purple-crayon-3-r1",
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "purple-crayon-3-r1",
                                                              :init-position {:x 1033, :y 558, :duration 1},
                                                              :check-variable "purple-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-3-r1",
                                                                :say-color "purple-color-r1",
                                                                :crayon-color "purple"}}},
                                :draggable true,
                                :x 1033,
                                :visible false},
           :question-0-options-option-2-voice-over {:x 0,
                                                    :y 0,
                                                    :type "group",
                                                    :actions {:click {:id "question-0-option-voice-over",
                                                                      :on "click",
                                                                      :type "action",
                                                                      :params {:value "option-3"},
                                                                      :unique-tag "question-action"}},
                                                    :children ["question-0-options-option-2-voice-over-background"
                                                               "question-0-options-option-2-voice-over-icon"]},
           :question-0-substrate {:x 0,
                                  :y 0,
                                  :fill 16777215,
                                  :type "rectangle",
                                  :width 1920,
                                  :height 1080,
                                  :object-name "question-0-substrate"},
           :librarian-r3 {:y 1000,
                          :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                          :speed 0.3,
                          :scale {:x 0.8, :y 0.8},
                          :name "senoravaca",
                          :width 351,
                          :start true,
                          :editable? {:select true, :show-in-tree? true},
                          :type "animation",
                          :actions {:click {:id "tap-instructions-r3", :on "click", :type "action"}},
                          :anim "idle",
                          :x 250,
                          :visible false,
                          :skin "lion",
                          :height 717},
           :question-0-options-option-3 {:x 990,
                                         :y 363.7356760886173,
                                         :type "group",
                                         :children ["question-0-options-option-3-substrate"
                                                    "question-0-options-option-3-voice-over"
                                                    "question-0-options-option-3-text"]},
           :green-crayon-1-r2 {:y 210,
                               :rotation 90,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :scale 0.35,
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r2",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:box "green-table-r2",
                                                             :target "green-crayon-1-r2",
                                                             :init-position {:x 714, :y 210, :duration 1},
                                                             :check-variable "green-table-selected"}},
                                         :drag-start {:id "start-drag-r2",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-1-r2", :say-color "green-color-r2"}}},
                               :draggable true,
                               :x 714,
                               :visible false},
           :question-0-options-option-0-substrate {:y 0,
                                                   :states {:active {:border-color 45823}, :default {:border-color 0}},
                                                   :fill 16777215,
                                                   :width 760,
                                                   :type "rectangle",
                                                   :actions {:click {:id "question-0-option-click-handler",
                                                                     :on "click",
                                                                     :type "action",
                                                                     :params {:value "option-1"},
                                                                     :unique-tag "question-action"}},
                                                   :border-width 2,
                                                   :border-color 0,
                                                   :x 110,
                                                   :border-radius 20,
                                                   :height 100},
           :blue-crayon-2-r2 {:y 500,
                              :rotation -90,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r2",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:box "blue-box-r2",
                                                            :target "blue-crayon-2-r2",
                                                            :init-position {:x 592, :y 500, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r2",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-2-r2", :say-color "blue-color-r2"}}},
                              :draggable true,
                              :x 592,
                              :visible false},
           :red-crayon-1-r1 {:y 64,
                             :rotation 31,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :transition "red-crayon-1-r1",
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r1",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:target "red-crayon-1-r1",
                                                           :init-position {:x 760, :y 64, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r1",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-1-r1",
                                                             :say-color "red-color-r1",
                                                             :crayon-color "red"}}},
                             :draggable true,
                             :x 760,
                             :visible false},
           :purple-table-r2 {:x 745,
                             :y 773,
                             :src "/raw/img/categorize/purple_table.png",
                             :type "image",
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :visible false},
           :green-crayon-3-r2 {:y 364,
                               :rotation 90,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :scale 0.35,
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r2",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:box "green-table-r2",
                                                             :target "green-crayon-3-r2",
                                                             :init-position {:x 1678, :y 364, :duration 1},
                                                             :check-variable "green-table-selected"}},
                                         :drag-start {:id "start-drag-r2",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-3-r2", :say-color "green-color-r2"}}},
                               :draggable true,
                               :x 1678,
                               :visible false},
           :blue-crayon-r3 {:y 440,
                            :rotation -90,
                            :states {:init-position {:x 1611, :y 440},
                                     :hide-technical {:visible false},
                                     :show-technical {:visible true}},
                            :scale 0.35,
                            :type "image",
                            :src "/raw/img/categorize/blue_crayons.png",
                            :actions {:drag-end {:id "stop-drag-hide-r3",
                                                 :on "drag-end",
                                                 :type "action",
                                                 :params {:self "blue-crayon-r3",
                                                          :target "blue-box-r3",
                                                          :colliders ["yellow-box-r3"
                                                                      "blue-box-r3"
                                                                      "red-box-r3"
                                                                      "purple-table-r3"
                                                                      "orange-table-r3"
                                                                      "green-table-r3"],
                                                          :init-position {:x 1611, :y 440, :duration 1}}},
                                      :drag-start {:id "start-drag-r3",
                                                   :on "drag-start",
                                                   :type "action",
                                                   :params {:self "blue-crayon-r3",
                                                            :target "blue-box-r3",
                                                            :colliders ["yellow-box-r3"
                                                                        "blue-box-r3"
                                                                        "red-box-r3"
                                                                        "purple-table-r3"
                                                                        "orange-table-r3"
                                                                        "green-table-r3"],
                                                            :say-color "blue-color-r3"}}},
                            :draggable true,
                            :x 1611,
                            :visible false},
           :blue-crayon-1-r1 {:y 228,
                              :rotation -53,
                              :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                              :transition "blue-crayon-1-r1",
                              :type "image",
                              :src "/raw/img/categorize/blue_crayons.png",
                              :actions {:drag-end {:id "drag-crayon-r1",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:target "blue-crayon-1-r1",
                                                            :init-position {:x 184, :y 228, :duration 1},
                                                            :check-variable "blue-box-selected"}},
                                        :drag-start {:id "start-drag-r1",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:target "blue-crayon-1-r1",
                                                              :say-color "blue-color-r1",
                                                              :crayon-color "blue"}}},
                              :draggable true,
                              :x 184,
                              :visible false},
           :question-0-options-option-3-substrate {:y 0,
                                                   :states {:active {:border-color 45823}, :default {:border-color 0}},
                                                   :fill 16777215,
                                                   :width 760,
                                                   :type "rectangle",
                                                   :actions {:click {:id "question-0-option-click-handler",
                                                                     :on "click",
                                                                     :type "action",
                                                                     :params {:value "option-4"},
                                                                     :unique-tag "question-action"}},
                                                   :border-width 2,
                                                   :border-color 0,
                                                   :x 110,
                                                   :border-radius 20,
                                                   :height 100},
           :question-0-options-option-2-voice-over-background {:x 0,
                                                               :y 0,
                                                               :fill 16777215,
                                                               :type "rectangle",
                                                               :width 80,
                                                               :height 80,
                                                               :states {:active {:fill 45823},
                                                                        :default {:fill 16777215}},
                                                               :border-radius 40},
           :librarian-r0 {:y 1000,
                          :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                          :speed 0.3,
                          :scale {:x 0.8, :y 0.8},
                          :name "senoravaca",
                          :width 351,
                          :start true,
                          :editable? {:select true, :show-in-tree? true},
                          :type "animation",
                          :anim "idle",
                          :x 380,
                          :skin "lion",
                          :height 717},
           :green-crayon-2-r1 {:y 387,
                               :rotation 25,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :transition "green-crayon-2-r1",
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r1",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:target "green-crayon-2-r1",
                                                             :init-position {:x 736, :y 387, :duration 1},
                                                             :check-variable "green-box-selected"}},
                                         :drag-start {:id "start-drag-r1",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-2-r1",
                                                               :say-color "green-color-r1",
                                                               :crayon-color "green"}}},
                               :draggable true,
                               :x 736,
                               :visible false},
           :question-0-task-text-group-button-icon {:y 24,
                                                    :states {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                    :fill "#000000",
                                                    :width 41,
                                                    :type "svg-path",
                                                    :x 19.5,
                                                    :scene-name "letter-tutorial-trace",
                                                    :height 32,
                                                    :data "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
           :yellow-crayon-2-r1 {:y 279,
                                :rotation 75,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "yellow-crayon-2-r1",
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "yellow-crayon-2-r1",
                                                              :init-position {:x 1071, :y 279, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-2-r1",
                                                                :say-color "yellow-color-r1",
                                                                :crayon-color "yellow"}}},
                                :draggable true,
                                :x 1071,
                                :visible false},
           :purple-crayon-1-r2 {:y 541,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "purple-table-r2",
                                                              :target "purple-crayon-1-r2",
                                                              :init-position {:x 664, :y 541, :duration 1},
                                                              :check-variable "purple-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-1-r2",
                                                                :say-color "purple-color-r2"}}},
                                :draggable true,
                                :x 664,
                                :visible false},
           :purple-crayon-3-r2 {:y 310,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "purple-table-r2",
                                                              :target "purple-crayon-3-r2",
                                                              :init-position {:x 1418, :y 310, :duration 1},
                                                              :check-variable "purple-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-3-r2",
                                                                :say-color "purple-color-r2"}}},
                                :draggable true,
                                :x 1418,
                                :visible false},
           :question-0-options-option-1-voice-over-icon {:y 24,
                                                         :states {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                         :fill "#000000",
                                                         :width 41,
                                                         :type "svg-path",
                                                         :x 19.5,
                                                         :scene-name "letter-tutorial-trace",
                                                         :height 32,
                                                         :data "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
           :orange-crayon-2-r1 {:y 456,
                                :rotation 215,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "orange-crayon-2-r1",
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "orange-crayon-2-r1",
                                                              :init-position {:x 1406, :y 456, :duration 1},
                                                              :check-variable "orange-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-2-r1",
                                                                :say-color "orange-color-r1",
                                                                :crayon-color "orange"}}},
                                :draggable true,
                                :x 1406,
                                :visible false},
           :question-0-options-option-1-substrate {:y 0,
                                                   :states {:active {:border-color 45823}, :default {:border-color 0}},
                                                   :fill 16777215,
                                                   :width 760,
                                                   :type "rectangle",
                                                   :actions {:click {:id "question-0-option-click-handler",
                                                                     :on "click",
                                                                     :type "action",
                                                                     :params {:value "option-2"},
                                                                     :unique-tag "question-action"}},
                                                   :border-width 2,
                                                   :border-color 0,
                                                   :x 110,
                                                   :border-radius 20,
                                                   :height 100},
           :question-0-options-option-3-voice-over-background {:x 0,
                                                               :y 0,
                                                               :fill 16777215,
                                                               :type "rectangle",
                                                               :width 80,
                                                               :height 80,
                                                               :states {:active {:fill 45823},
                                                                        :default {:fill 16777215}},
                                                               :border-radius 40},
           :purple-crayon-2-r1 {:y 279,
                                :rotation 165,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :transition "purple-crayon-2-r1",
                                :type "image",
                                :src "/raw/img/categorize/purple_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r1",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:target "purple-crayon-2-r1",
                                                              :init-position {:x 1071, :y 279, :duration 1},
                                                              :check-variable "purple-box-selected"}},
                                          :drag-start {:id "start-drag-r1",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "purple-crayon-2-r1",
                                                                :say-color "purple-color-r1",
                                                                :crayon-color "purple"}}},
                                :draggable true,
                                :x 1071,
                                :visible false},
           :purple-crayon-r3 {:y 1054,
                              :rotation -90,
                              :states {:init-position {:x 60, :y 1054},
                                       :hide-technical {:visible false},
                                       :show-technical {:visible true}},
                              :scale 0.35,
                              :type "image",
                              :src "/raw/img/categorize/purple_crayons.png",
                              :actions {:drag-end {:id "stop-drag-hide-r3",
                                                   :on "drag-end",
                                                   :type "action",
                                                   :params {:self "purple-crayon-r3",
                                                            :target "purple-table-r3",
                                                            :colliders ["yellow-box-r3"
                                                                        "blue-box-r3"
                                                                        "red-box-r3"
                                                                        "purple-table-r3"
                                                                        "orange-table-r3"
                                                                        "green-table-r3"],
                                                            :init-position {:x 60, :y 1054, :duration 1}}},
                                        :drag-start {:id "start-drag-r3",
                                                     :on "drag-start",
                                                     :type "action",
                                                     :params {:self "purple-crayon-r3",
                                                              :target "purple-table-r3",
                                                              :colliders ["yellow-box-r3"
                                                                          "blue-box-r3"
                                                                          "red-box-r3"
                                                                          "purple-table-r3"
                                                                          "orange-table-r3"
                                                                          "green-table-r3"],
                                                              :say-color "purple-color-r3"}}},
                              :draggable true,
                              :x 60,
                              :visible false},
           :layered-background-r2 {:type "layered-background",
                                   :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                   :surface {:src "/raw/img/categorize/02.png"},
                                   :visible false,
                                   :background {:src "/raw/img/categorize/01.png"},
                                   :decoration {:src "/raw/img/categorize/03.png"}},
           :question-0-options-option-2-text {:y 50,
                                              :vertical-align "middle",
                                              :font-size 48,
                                              :word-wrap true,
                                              :width 720,
                                              :editable? {:select true},
                                              :type "text",
                                              :actions {:click {:id "question-0-option-click-handler",
                                                                :on "click",
                                                                :type "action",
                                                                :params {:value "option-3"},
                                                                :unique-tag "question-action"}},
                                              :chunks [{:end 4, :start 0}
                                                       {:end 8, :start 5}
                                                       {:end 13, :start 9}
                                                       {:end 16, :start 14}
                                                       {:end 22, :start 17}
                                                       {:end 29, :start 23}
                                                       {:end 36, :start 30}],
                                              :x 130,
                                              :text "They are both in their crayon boxes."},
           :red-crayon-3-r2 {:y 164,
                             :rotation -90,
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :scale 0.35,
                             :type "image",
                             :src "/raw/img/categorize/red_crayons.png",
                             :actions {:drag-end {:id "drag-crayon-r2",
                                                  :on "drag-end",
                                                  :type "action",
                                                  :params {:box "red-box-r2",
                                                           :target "red-crayon-3-r2",
                                                           :init-position {:x 1548, :y 164, :duration 1},
                                                           :check-variable "red-box-selected"}},
                                       :drag-start {:id "start-drag-r2",
                                                    :on "drag-start",
                                                    :type "action",
                                                    :params {:target "red-crayon-3-r2", :say-color "red-color-r2"}}},
                             :draggable true,
                             :x 1548,
                             :visible false},
           :green-crayon-1-r1 {:y 64,
                               :rotation 121,
                               :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                               :transition "green-crayon-1-r1",
                               :type "image",
                               :src "/raw/img/categorize/green_crayons.png",
                               :actions {:drag-end {:id "drag-crayon-r1",
                                                    :on "drag-end",
                                                    :type "action",
                                                    :params {:target "green-crayon-1-r1",
                                                             :init-position {:x 760, :y 64, :duration 1},
                                                             :check-variable "green-box-selected"}},
                                         :drag-start {:id "start-drag-r1",
                                                      :on "drag-start",
                                                      :type "action",
                                                      :params {:target "green-crayon-1-r1",
                                                               :say-color "green-color-r1",
                                                               :crayon-color "green"}}},
                               :draggable true,
                               :x 760,
                               :visible false},
           :question-0-task-text-group {:x 793.3842627960273,
                                        :y 60,
                                        :type "group",
                                        :children ["question-0-task-text-group-button" "question-0-task-text"]},
           :question-0 {:x 0,
                        :y 0,
                        :type "group",
                        :alias "What is the same about these two crayons?",
                        :visible true,
                        :children ["question-0-substrate"
                                   "question-0-background"
                                   "question-0-task-text-group"
                                   "question-0-options"
                                   "question-0-task-image"],
                        :metadata {:assets ["/images/questions/question.png"],
                                   :actions ["question-0-options-option-3-voice-over-activate"
                                             "question-0-task-dialog"
                                             "question-0-options-option-0-substrate-inactivate"
                                             "question-0-option-voice-over-voice-over-option-3"
                                             "question-0-options-option-2-substrate-inactivate"
                                             "question-0-options-option-0-voice-over-activate"
                                             "question-0-task-voice-over-click"
                                             "question-0-options-option-1-substrate-inactivate"
                                             "question-0-options-option-3-substrate-inactivate"
                                             "question-0-option-voice-over-voice-over-option-1"
                                             "question-0-options-option-2-voice-over-inactivate"
                                             "question-0-options-option-1-substrate-activate"
                                             "question-0-hide"
                                             "question-0-options-option-0-voice-over-inactivate"
                                             "question-0-options-option-0-substrate-activate"
                                             "question-0-options-option-3-substrate-activate"
                                             "question-0-options-option-2-substrate-activate"
                                             "question-0-option-voice-over-voice-over-option-2"
                                             "question-0-check-answers-correct-answer-dialog"
                                             "question-0-options-option-1-voice-over-activate"
                                             "question-0-options-option-1-voice-over-inactivate"
                                             "question-0-option-click-handler"
                                             "question-0-options-option-3-voice-over-inactivate"
                                             "question-0-check-answers-wrong-answer"
                                             "question-0-options-option-2-voice-over-activate"
                                             "question-0-task-text-group-button-activate"
                                             "question-0-option-voice-over"
                                             "question-0-check-answers-correct-answer"
                                             "question-0-show"
                                             "question-0-check-answers-wrong-answer-dialog"
                                             "question-0-task-text-group-button-inactivate"
                                             "question-0-check-answers"
                                             "question-0"
                                             "question-0-option-voice-over-voice-over-option-4"],
                                   :objects ["question-0-options-option-0-voice-over-icon"
                                             "question-0-options-option-0-text"
                                             "question-0-options-option-2-voice-over-icon"
                                             "question-0-options-option-0-voice-over-background"
                                             "question-0-options-option-0-voice-over"
                                             "question-0-background"
                                             "question-0-options-option-1-text"
                                             "question-0-options-option-3-text"
                                             "question-0-options-option-2"
                                             "question-0-task-text"
                                             "question-0-task-text-group-button-background"
                                             "question-0-options-option-2-substrate"
                                             "question-0-options"
                                             "question-0-options-option-1"
                                             "question-0-task-image"
                                             "question-0-options-option-1-voice-over"
                                             "question-0-options-option-2-voice-over"
                                             "question-0-substrate"
                                             "question-0-options-option-3"
                                             "question-0-options-option-0-substrate"
                                             "question-0-options-option-3-substrate"
                                             "question-0-options-option-2-voice-over-background"
                                             "question-0-task-text-group-button-icon"
                                             "question-0-options-option-1-voice-over-icon"
                                             "question-0-options-option-1-substrate"
                                             "question-0-options-option-3-voice-over-background"
                                             "question-0-options-option-2-text"
                                             "question-0-task-text-group"
                                             "question-0"
                                             "question-0-options-option-3-voice-over-icon"
                                             "question-0-options-option-0"
                                             "question-0-task-text-group-button"
                                             "question-0-options-option-1-voice-over-background"
                                             "question-0-options-option-3-voice-over"],
                                   :question? true},
                        :editable? {:show-in-tree? true}},
           :layered-background-r0 {:type "layered-background",
                                   :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                   :surface {:src "/raw/img/categorize/02.png"},
                                   :background {:src "/raw/img/categorize/01.png"},
                                   :decoration {:src "/raw/img/categorize/03.png"}},
           :blue-box-r1 {:x 375,
                         :y 810,
                         :src "/raw/img/categorize/blue_box.png",
                         :type "image",
                         :scale 0.65,
                         :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                         :visible false},
           :question-0-options-option-3-voice-over-icon {:y 24,
                                                         :states {:active {:fill "#FFFFFF"}, :default {:fill "#000000"}},
                                                         :fill "#000000",
                                                         :width 41,
                                                         :type "svg-path",
                                                         :x 19.5,
                                                         :scene-name "letter-tutorial-trace",
                                                         :height 32,
                                                         :data "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z"},
           :orange-table-r3 {:x 745,
                             :y 773,
                             :src "/raw/img/categorize/orange_table.png",
                             :type "image",
                             :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                             :visible false},
           :purple-box-r1 {:x 975,
                           :y 810,
                           :src "/raw/img/categorize/purple_box.png",
                           :type "image",
                           :scale 0.65,
                           :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                           :visible false},
           :yellow-crayon-3-r2 {:y 440,
                                :rotation -90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/yellow_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "yellow-box-r2",
                                                              :target "yellow-crayon-3-r2",
                                                              :init-position {:x 1618, :y 440, :duration 1},
                                                              :check-variable "yellow-box-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "yellow-crayon-3-r2",
                                                                :say-color "yellow-color-r2"}}},
                                :draggable true,
                                :x 1618,
                                :visible false},
           :question-0-options-option-0 {:x 60,
                                         :y 203.7356760886173,
                                         :type "group",
                                         :children ["question-0-options-option-0-substrate"
                                                    "question-0-options-option-0-voice-over"
                                                    "question-0-options-option-0-text"]},
           :question-0-task-text-group-button {:x 0,
                                               :y 0,
                                               :type "group",
                                               :actions {:click {:id "question-0-task-voice-over-click",
                                                                 :on "click",
                                                                 :type "action",
                                                                 :unique-tag "question-action"}},
                                               :children ["question-0-task-text-group-button-background"
                                                          "question-0-task-text-group-button-icon"]},
           :question-0-options-option-1-voice-over-background {:x 0,
                                                               :y 0,
                                                               :fill 16777215,
                                                               :type "rectangle",
                                                               :width 80,
                                                               :height 80,
                                                               :states {:active {:fill 45823},
                                                                        :default {:fill 16777215}},
                                                               :border-radius 40},
           :orange-crayon-2-r2 {:y 400,
                                :rotation 90,
                                :states {:hide-technical {:visible false}, :show-technical {:visible true}},
                                :scale 0.35,
                                :type "image",
                                :src "/raw/img/categorize/orange_crayons.png",
                                :actions {:drag-end {:id "drag-crayon-r2",
                                                     :on "drag-end",
                                                     :type "action",
                                                     :params {:box "orange-table-r2",
                                                              :target "orange-crayon-2-r2",
                                                              :init-position {:x 892, :y 400, :duration 1},
                                                              :check-variable "orange-table-selected"}},
                                          :drag-start {:id "start-drag-r2",
                                                       :on "drag-start",
                                                       :type "action",
                                                       :params {:target "orange-crayon-2-r2",
                                                                :say-color "orange-color-r2"}}},
                                :draggable true,
                                :x 892,
                                :visible false},
           :question-0-options-option-3-voice-over {:x 0,
                                                    :y 0,
                                                    :type "group",
                                                    :actions {:click {:id "question-0-option-voice-over",
                                                                      :on "click",
                                                                      :type "action",
                                                                      :params {:value "option-4"},
                                                                      :unique-tag "question-action"}},
                                                    :children ["question-0-options-option-3-voice-over-background"
                                                               "question-0-options-option-3-voice-over-icon"]}},
 :metadata {:template-name "Categorize - Colors",
            :stages [{:name "Round 1",
                      :objects ["layered-background-r0" "yellow-table-r0" "blue-table-r0" "red-table-r0" "librarian-r0"]}
                     {:name "Round 2",
                      :objects ["yellow-crayon-1-r1"
                                "orange-crayon-3-r1"
                                "orange-crayon-1-r1"
                                "red-box-r1"
                                "purple-crayon-1-r1"
                                "green-box-r1"
                                "red-crayon-3-r1"
                                "background-r1"
                                "red-crayon-2-r1"
                                "yellow-box-r1"
                                "blue-crayon-2-r1"
                                "green-crayon-3-r1"
                                "yellow-crayon-3-r1"
                                "orange-box-r1"
                                "blue-crayon-3-r1"
                                "purple-crayon-3-r1"
                                "red-crayon-1-r1"
                                "blue-crayon-1-r1"
                                "green-crayon-2-r1"
                                "yellow-crayon-2-r1"
                                "orange-crayon-2-r1"
                                "purple-crayon-2-r1"
                                "green-crayon-1-r1"
                                "blue-box-r1"
                                "purple-box-r1"]}
                     {:name "Round 3",
                      :objects ["orange-crayon-1-r2"
                                "librarian-r2"
                                "red-crayon-2-r2"
                                "orange-crayon-3-r2"
                                "purple-crayon-2-r2"
                                "yellow-crayon-2-r2"
                                "red-crayon-1-r2"
                                "yellow-crayon-1-r2"
                                "blue-box-r2"
                                "red-box-r2"
                                "blue-crayon-3-r2"
                                "blue-crayon-1-r2"
                                "yellow-box-r2"
                                "green-crayon-2-r2"
                                "green-table-r2"
                                "orange-table-r2"
                                "green-crayon-1-r2"
                                "blue-crayon-2-r2"
                                "purple-table-r2"
                                "green-crayon-3-r2"
                                "purple-crayon-1-r2"
                                "purple-crayon-3-r2"
                                "layered-background-r2"
                                "red-crayon-3-r2"
                                "yellow-crayon-3-r2"
                                "orange-crayon-2-r2"]}
                     {:name "Round 4",
                      :objects ["red-box-r3"
                                "yellow-crayon-r3"
                                "green-table-r3"
                                "purple-table-r3"
                                "blue-box-r3"
                                "orange-crayon-r3"
                                "yellow-box-r3"
                                "red-crayon-r3"
                                "green-crayon-r3"
                                "layered-background-r3"
                                "librarian-r3"
                                "blue-crayon-r3"
                                "purple-crayon-r3"
                                "orange-table-r3"]}],
            :tracks [{:nodes [{:type "dialog", :action-id "question-0-task-dialog"}
                              {:type "dialog", :action-id "question-0-check-answers-correct-answer-dialog"}
                              {:type "dialog", :action-id "question-0-check-answers-wrong-answer-dialog"}
                              {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-1"}
                              {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-2"}
                              {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-3"}
                              {:type "dialog", :action-id "question-0-option-voice-over-voice-over-option-4"}],
                      :title "What is the same about these two crayons?",
                      :question-id "question-0"}
                     {:nodes [{:type "dialog", :action-id "voiceover-r0"}], :title "Welcome"}
                     {:nodes [{:type "dialog", :action-id "intro-r1"}
                              {:text "Correct answer", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-r1"}
                              {:text "Wrong answer", :type "prompt"}
                              {:type "dialog", :action-id "wrong-answer-r1"}
                              {:text "Continue sorting", :type "prompt"}
                              {:type "dialog", :action-id "continue-sorting-r1"}
                              {:text "Finish dialog", :type "prompt"}
                              {:type "dialog", :action-id "finish-dialog-r1"}],
                      :title "Round 1"}
                     {:nodes [{:type "dialog", :action-id "yellow-color-r1"}
                              {:type "dialog", :action-id "blue-color-r1"}
                              {:type "dialog", :action-id "red-color-r1"}
                              {:type "dialog", :action-id "purple-color-r1"}
                              {:type "dialog", :action-id "orange-color-r1"}
                              {:type "dialog", :action-id "green-color-r1"}],
                      :title "Round 1 - colors"}
                     {:nodes [{:type "dialog", :action-id "intro-r2"}
                              {:text "Correct answer", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-r2"}
                              {:text "Wrong answer", :type "prompt"}
                              {:type "dialog", :action-id "wrong-answer-r2"}
                              {:type "dialog", :action-id "tap-instructions-r2"}
                              {:text "Dialog after all elements correctly found", :type "prompt"}
                              {:type "dialog", :action-id "finish-round-dialog-r2"}],
                      :title "Round 2"}
                     {:nodes [{:type "dialog", :action-id "yellow-color-r2"}
                              {:type "dialog", :action-id "blue-color-r2"}
                              {:type "dialog", :action-id "red-color-r2"}
                              {:type "dialog", :action-id "purple-color-r2"}
                              {:type "dialog", :action-id "orange-color-r2"}
                              {:type "dialog", :action-id "green-color-r2"}],
                      :title "Round 2 - colors"}
                     {:nodes [{:type "dialog", :action-id "intro-r3"} {:type "dialog", :action-id "finish-dialog-r3"}],
                      :title "Round 3 - Intro and finish"}
                     {:nodes [{:type "dialog", :action-id "wrong-answer-dialog-r3"}
                              {:type "dialog", :action-id "correct-answer-dialog-r3"}],
                      :title "Round 3 - Action result"}
                     {:nodes [{:text "Put the red crayon in its crayon box.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-1-r3"}
                              {:text "Put the purple crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-2-r3"}
                              {:text "Put the yellow crayon in its crayon box.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-3-r3"}
                              {:text "Put the green crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-4-r3"}
                              {:text "Put the blue crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-5-r3"}
                              {:text "Put the orange crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "instruction-6-r3"}],
                      :title "Round 3 - tasks"}
                     {:nodes [{:text "Put the red crayon in its crayon box.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-1-r3"}
                              {:text "Put the purple crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-2-r3"}
                              {:text "Put the yellow crayon in its crayon box.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-3-r3"}
                              {:text "Put the green crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-4-r3"}
                              {:text "Put the blue crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-5-r3"}
                              {:text "Put the orange crayon on its table.", :type "prompt"}
                              {:type "dialog", :action-id "correct-answer-6-r3"}],
                      :title "Round 3 - Correct responses"}
                     {:nodes [{:type "dialog", :action-id "yellow-color-r3"}
                              {:type "dialog", :action-id "blue-color-r3"}
                              {:type "dialog", :action-id "red-color-r3"}
                              {:type "dialog", :action-id "purple-color-r3"}
                              {:type "dialog", :action-id "orange-color-r3"}
                              {:type "dialog", :action-id "green-color-r3"}],
                      :title "Round 3 - colors"}],
            :history {:created {:template-id 22},
                      :updated [{:data {:background-music {:src "/upload/LGFWFEKYFMZRKXYS.mp3", :volume "0.1"}},
                                 :action "background-music",
                                 :common-action? true}
                                {:data {:question-page-object {:answers-number "one",
                                                               :layout "vertical",
                                                               :task-type "text-image",
                                                               :question-type "multiple-choice-text",
                                                               :alias "What is the same about these two crayons?",
                                                               :options-number 4,
                                                               :mark-options ["thumbs-up" "thumbs-down"],
                                                               :option-label "audio-text",
                                                               :correct-answers ["option-2"]}},
                                 :action "add-question",
                                 :common-action? true}]},
            :template-version nil,
            :next-action-index 1,
            :lip-not-sync false,
            :unique-suffix 0,
            :template-id 22,
            :available-actions [{:name "Ask What is the same about these two crayons?",
                                 :type "question",
                                 :action "question-0"}]},
 :triggers {:music {:on "start", :action "start-background-music"},
            :start {:on "start", :action "intermediate-action-0"}},
 :scene-objects [["layered-background-r0"]
                 ["yellow-table-r0" "blue-table-r0" "red-table-r0"]
                 ["librarian-r0"]
                 ["background-r1"]
                 ["yellow-box-r1" "blue-box-r1" "red-box-r1"]
                 ["purple-box-r1" "orange-box-r1" "green-box-r1"]
                 ["blue-crayon-1-r1" "yellow-crayon-1-r1" "red-crayon-1-r1"]
                 ["orange-crayon-1-r1" "purple-crayon-1-r1" "green-crayon-1-r1"]
                 ["red-crayon-2-r1" "blue-crayon-2-r1" "yellow-crayon-2-r1"]
                 ["green-crayon-2-r1" "orange-crayon-2-r1" "purple-crayon-2-r1"]
                 ["yellow-crayon-3-r1" "red-crayon-3-r1" "blue-crayon-3-r1"]
                 ["green-crayon-3-r1" "orange-crayon-3-r1" "purple-crayon-3-r1"]
                 ["layered-background-r2"]
                 ["orange-table-r2" "green-table-r2" "purple-table-r2"]
                 ["librarian-r2"]
                 ["yellow-box-r2" "blue-box-r2" "red-box-r2"]
                 ["blue-crayon-1-r2" "blue-crayon-2-r2" "blue-crayon-3-r2"]
                 ["yellow-crayon-1-r2" "yellow-crayon-2-r2" "yellow-crayon-3-r2"]
                 ["purple-crayon-1-r2" "purple-crayon-2-r2" "purple-crayon-3-r2"]
                 ["red-crayon-1-r2" "red-crayon-2-r2" "red-crayon-3-r2"]
                 ["green-crayon-1-r2" "green-crayon-2-r2" "green-crayon-3-r2"]
                 ["orange-crayon-1-r2" "orange-crayon-2-r2" "orange-crayon-3-r2"]
                 ["layered-background-r3"]
                 ["purple-table-r3" "green-table-r3" "orange-table-r3"]
                 ["librarian-r3"]
                 ["yellow-box-r3" "blue-box-r3" "red-box-r3"]
                 ["red-crayon-r3"
                  "purple-crayon-r3"
                  "yellow-crayon-r3"
                  "green-crayon-r3"
                  "blue-crayon-r3"
                  "orange-crayon-r3"]
                 ["question-0"]]})
