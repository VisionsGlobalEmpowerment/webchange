(ns webchange.templates.library.syllables
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

(def m {:id          33
        :name        "Syllables"
        :tags        ["Syllables"]
        :description "Some description of syllables mechanics and covered skills"
        :actions     {
                      :add-word {:title   "Add word",
                                 :options {
                                           :word {:label "Word"
                                                  :type  "string"}
                                           }}}})

(def t {:assets
                       [
                        {:url "/raw/img/syllables/bg.png", :size 10, :type "audio"}
                        {:url "/raw/img/syllables/fog.png", :size 10, :type "audio"}
                        {:url "/raw/img/syllables/splash_cymbal.png", :size 10, :type "audio"}
                        ],
        :objects       {:layered-background {
                                             :type       "layered-background"
                                             :background {:src "/raw/img/syllables/bg.png"}
                                             :decoration {:src "/raw/img/syllables/fog.png"},
                                             }
                        :vera               {:width      380,
                                             :height     537,
                                             :scale      {:x 0.75, :y 0.75},
                                             :speed      0.5
                                             :meshes     true
                                             :name       "vera"
                                             :skin       "02 Vera_2"
                                             :x          1744
                                             :y          513
                                             :type       "animation"
                                             :editable?  true
                                             :anim       "idle"
                                             :start      true
                                             :scene-name "vera"
                                             }
                        :splash-cymbal      {
                                             :x          349,
                                             :y          510,
                                             :width      257,
                                             :height     157,
                                             :type       "image",
                                             :src        "/raw/img/syllables/splash_cymbal.png",
                                             :scene-name "splash-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "splash-cymbal-tap"
                                                                  :on   "click"}}
                                             },
                        :group              {
                                             :x          313,
                                             :y          640,
                                             :width      1201,
                                             :height     440,
                                             :type       "image",
                                             :src        "/raw/img/syllables/group.png",
                                             :scene-name "group",
                                             },
                        :bass-drum          {
                                             :x          738,
                                             :y          688,
                                             :width      372,
                                             :height     392,
                                             :type       "image",
                                             :src        "/raw/img/syllables/bass_drum.png",
                                             :scene-name "bass-drum",
                                             :actions    {:click {:type "action"
                                                                  :id   "bass-drum-tap"
                                                                  :on   "click"}}
                                             },
                        :hi-hat             {
                                             :x          143,
                                             :y          695,
                                             :width      314,
                                             :height     165,
                                             :type       "image",
                                             :src        "/raw/img/syllables/hi_hat.png",
                                             :scene-name "hi-hat",
                                             :actions    {:click {:type "action"
                                                                  :id   "hi-hat-tap"
                                                                  :on   "click"}}
                                             },
                        :share-drum         {
                                             :x          492,
                                             :y          713,
                                             :width      293,
                                             :height     262,
                                             :type       "image",
                                             :src        "/raw/img/syllables/share_drum.png",
                                             :scene-name "share-drum",
                                             :actions    {:click {:type "action"
                                                                  :id   "share-drum-tap"
                                                                  :on   "click"}}
                                             },
                        :tom-tom-left       {
                                             :x          638,
                                             :y          522,
                                             :width      227,
                                             :height     276,
                                             :type       "image",
                                             :src        "/raw/img/syllables/tom_tom_left.png",
                                             :scene-name "tom-tom-left",
                                             :actions    {:click {:type "action"
                                                                  :id   "tom-tom-left-tap"
                                                                  :on   "click"}}
                                             }
                        :tom-tom-right      {
                                             :x          966,
                                             :y          522,
                                             :width      229,
                                             :height     277,
                                             :type       "image",
                                             :src        "/raw/img/syllables/tom_tom_right.png",
                                             :scene-name "tom-tom-right",
                                             :actions    {:click {:type "action"
                                                                  :id   "tom-tom-right-tap"
                                                                  :on   "click"}}
                                             }
                        :ride-cymbal        {
                                             :x          1250,
                                             :y          517,
                                             :width      335,
                                             :height     187,
                                             :type       "image",
                                             :src        "/raw/img/syllables/ride_cymbal.png",
                                             :scene-name "ride-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "ride-cymbal-tap"
                                                                  :on   "click"}}
                                             }
                        :china-cymbal       {
                                             :x          1387,
                                             :y          530,
                                             :width      444,
                                             :height     279,
                                             :type       "image",
                                             :src        "/raw/img/syllables/china_cymbal.png",
                                             :scene-name "china-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "china-cymbal-tap"
                                                                  :on   "click"}}
                                             }
                        :text-rectangle     {:type          "rectangle"
                                             :object-name   :activity-finished-frame
                                             :border-radius 56
                                             :fill          0xffffff
                                             :width         856,
                                             :height        320,
                                             :x             532,
                                             :y             104,
                                             :scene-name    "text-rectangle",
                                             :visible       false,
                                             :states        {:visible {:visible true} :non-visible {:visible false}},
                                             }

                        },
        :scene-objects [["layered-background"]
                        ["group" "bass-drum" "tom-tom-left" "tom-tom-right" "splash-cymbal" "hi-hat" "share-drum" "ride-cymbal" "china-cymbal"]
                        ["text-rectangle" "vera"]
                        ],
        :actions       {
                        :splash-cymbal-dialog  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "splash-cymbal-dialog",
                                                :phrase-description "Splash cymbal dialog"}

                        :splash-cymbal-tap     {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "splash-cymbal-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :bass-drum-dialog      {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "bass-drum-dialog",
                                                :phrase-description "Bass drum dialog"}

                        :bass-drum-tap         {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "bass-drum-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :hi-hat-dialog         {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "hi-hat-dialog",
                                                :phrase-description "Hi hat dialog"}

                        :hi-hat-tap            {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "hi-hat-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :share-drum-dialog     {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "share-drum-dialog",
                                                :phrase-description "Share drum dialog"}

                        :share-drum-tap        {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "share-drum-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :tom-tom-left-dialog   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "tom-tom-left-dialog",
                                                :phrase-description "Tom tom left dialog"}

                        :tom-tom-left-tap      {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "tom-tom-left-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :tom-tom-right-dialog  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "tom-tom-right-dialog",
                                                :phrase-description "Tom tom right dialog"}

                        :tom-tom-right-tap     {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "tom-tom-right-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :ride-cymbal-dialog    {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "ride-cymbal-dialog",
                                                :phrase-description "Ride cymbal dialog"}

                        :ride-cymbal-tap       {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "ride-cymbal-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :china-cymbal-dialog   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "china-cymbal-dialog",
                                                :phrase-description "China cymbal dialog"}

                        :china-cymbal-tap      {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "china-cymbal-dialog"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :cymbal-click          {:type "sequence-data",
                                                :data [{:type "action" :id "next-counter"}
                                                       {:type "action" :id "next-syllable"}
                                                       ]}

                        :next-syllable         {:type      "animate-next-text",
                                                :animation "color",
                                                :fill      "#ff0000",
                                                :from-var  [{:var-name "word-name", :action-property "target"}]
                                                }
                        :next-counter          {:type "next-timeout-counter"
                                                :id   "syllables-counter",}
                        :timeout-timer         {:type     "start-timeout-counter",
                                                :id       "syllables-counter",
                                                :action   "check-result",
                                                :interval 2000}
                        :check-result          {:type        "sequence-data",
                                                :editor-type "dialog",
                                                :data        [
                                                              {:type     "state" :id "non-visible"
                                                               :from-var [{:var-name "word-name", :action-property "target"}]
                                                               }
                                                              {:type       "test-var-inequality"
                                                               :var-name   "syllables-counter-value",
                                                               :inequality ">=",
                                                               :fail       "wrong-task-action"
                                                               :success    "success-task-action"
                                                               :from-var   [{:var-name "syllable-count", :action-property "value"}]
                                                               }]}

                        :wrong-task-action     {:type "sequence-data",
                                                :data [{:type "action" :id "wrong-answer-dialog"}
                                                       {:type "action" :id "task-setup-0"}]}

                        :success-task-action   {:type "sequence-data",
                                                :data [
                                                       {:type "action" :id "correct-answer-dialog"}
                                                       {:type "action" :from-var [{:var-name "success-action", :action-property "id"}]}]}

                        :intro                 {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "intro",
                                                :phrase-description "Intro"}

                        :correct-answer-dialog {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "correct-answer-dialog",
                                                :phrase-description "Correct answer dialog"}

                        :wrong-answer-dialog   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "wrong answer dialog",
                                                :phrase-description "Wrong answer dialog"}

                        :intro-words           {:type "sequence-data",
                                                :data [{:type "state" :id "visible" :target "text-rectangle"}
                                                       {:type "state" :id "non-visible" :target "text-rectangle"}]}

                        :intro-task            {:type "sequence-data",
                                                :data [{:type "state" :id "visible" :target "text-rectangle"}
                                                       {:type "state" :id "visible" :target "text-0"}
                                                       {:type "action" :id "intro-task-dialog"}]}

                        :intro-task-dialog     {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "Intro task dialog",
                                                :phrase-description "Intro task dialog"}

                        :start-scene           {:type "sequence-data",
                                                :data [{:type "action" :id "intro"}
                                                       {:type "action" :id "intro-words"}
                                                       {:type "action" :id "intro-task"}
                                                       {:type "action" :id "task-setup-0"}
                                                       ]}

                        :task-setup-0          {:type "sequence-data",
                                                :data [{:type "action" :id "finish-scene"}]}

                        :finish-scene          {:type "sequence-data",
                                                :data [
                                                       {:type "state" :id "non-visible" :target "text-rectangle"}
                                                       {:type "action" :id "finish-dialog"}
                                                       {:type "action" :id "play-30-seconds"}
                                                       ]}

                        :play-30-seconds       {:type      "start-timeout-counter",
                                                :id        "play-30-seconds",
                                                :action    "stop-activity",
                                                :autostart true
                                                :interval  30000}

                        :stop-activity         {:type "sequence-data",
                                                :data [
                                                       {:type "stop-activity", :id "syllables"},]}

                        :finish-dialog         {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "finish",
                                                :phrase-description "Task finish"}

                        }

        :triggers      {:start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true
                        :tracks    [{:title "Instruments"
                                     :nodes [{:type      "dialog"
                                              :action-id :splash-cymbal-dialog}
                                             {:type      "dialog"
                                              :action-id :bass-drum-dialog}
                                             {:type      "dialog"
                                              :action-id :hi-hat-dialog}
                                             {:type      "dialog"
                                              :action-id :share-drum-dialog}
                                             {:type      "dialog"
                                              :action-id :tom-tom-left-dialog}
                                             {:type      "dialog"
                                              :action-id :tom-tom-right-dialog}
                                             {:type      "dialog"
                                              :action-id :ride-cymbal-dialog}
                                             {:type      "dialog"
                                              :action-id :china-cymbal-dialog}
                                             ]}
                                    {:title "Dialogs"
                                     :nodes [{:type      "dialog"
                                              :action-id :finish-dialog}
                                             {:type      "dialog"
                                              :action-id :intro}
                                             {:type      "dialog"
                                              :action-id :intro-task-dialog}]}
                                    {:title "Words"
                                     :nodes []}
                                    ]
                        }})

(defn text-object
  [old-data args]
  {(common/make-name-unique old-data "text") {:type           "text",
                                              :x              725,
                                              :scene-name     (name (common/make-name-unique old-data "text")),
                                              :y              203,
                                              :width          470,
                                              :height         122,
                                              :align          "center",
                                              :chunks         (:result (reduce (fn [result syllable]
                                                                                 (let [len (count syllable)
                                                                                       start (:last result)
                                                                                       end (+ start len)
                                                                                       ]
                                                                                   (-> result
                                                                                       (update :result conj {:end end, :start start})
                                                                                       (assoc :last end)
                                                                                       )
                                                                                   )) {:last 0 :result []} (clojure.string/split (:word args) #" "))),
                                              :fill           "#000000",
                                              :font-family    "Liberation Sans",
                                              :font-size      104,
                                              :text           (clojure.string/replace (:word args) " " ""),
                                              :vertical-align "middle",
                                              :visible        false,
                                              :states         {:visible {:visible true} :non-visible {:visible false}}}})

(defn task-start-action
  [scene-data args]
  {(common/make-name-unique scene-data "task-setup") {:type "sequence-data",
                                                      :data [{:type "reset-animate-text", :animation "color",
                                                              :fill "#000000", :target (common/make-name-unique scene-data "text")}
                                                             {:type "state" :id "visible" :target (common/make-name-unique scene-data "text")}
                                                             {:type "set-variable" :var-name "word-name" :var-value (common/make-name-unique scene-data "text")}
                                                             {:type "set-variable" :var-name "syllable-count" :var-value (count (clojure.string/split (:word args) #" "))}
                                                             {:type "set-variable" :var-name "success-action" :var-value "finish-scene"}
                                                             {:type "action" :id "timeout-timer"}]}
   }
  )

(defn f
  [args]
  (common/init-metadata m t args))

(defn fu
  [old-data args]
  (let [syntax (common/get-unique-suffix old-data)
        word (text-object old-data args)
        word-name (first (vec (map name (keys word))))
        dialog (dialog/create-simple "word-dialog" (:word args) syntax ["next-syllable"])
        dialog-name (first (vec (map name (keys dialog))))
        task-start (task-start-action old-data args)
        task-start-name (first (vec (map name (keys task-start))))
        scene-data (cond-> old-data
                           true (update :objects merge word)
                           true (update :actions merge dialog)
                           true (common/add-track-action {:track-name "Words"
                                                          :type       "dialog"
                                                          :action-id  (keyword dialog-name)})
                           true (update :actions merge task-start)
                           (not (common/unique-suffix-first? old-data))
                           (assoc-in [:actions (common/make-prev-name-unique old-data "task-setup") :data 4 :var-value] task-start-name)
                           true (update-in [:actions :intro-words :data] (fn [intro-words]
                                                                           (let [last-item (last intro-words)]
                                                                             (-> intro-words
                                                                                 (drop-last)
                                                                                 (vec)
                                                                                 (conj {:type "set-variable" :var-name "word-name" :var-value word-name})
                                                                                 (conj {:type "state" :id "visible" :target word-name})
                                                                                 (conj {:type "action" :id dialog-name})
                                                                                 (conj {:type "state" :id "non-visible" :target word-name})
                                                                                 (conj last-item)))))
                           true (common/add-scene-object [word-name])
                           true (common/update-unique-suffix))]
    scene-data))

(core/register-template
  m f fu)
