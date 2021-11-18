(ns webchange.templates.library.syllables
  (:require
    [clojure.string :as str]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

(def m {:id          33
        :name        "Syllables"
        :tags        ["Syllables"]
        :description "Some description of syllables mechanics and covered skills"
        :actions     {:add-word {:title   "Add word",
                                 :options {:word {:label "Word"
                                                  :type  "string"}}}}})

(def t {:assets [{:url "/raw/img/syllables/bg.png", :size 10, :type "audio"}
                 {:url "/raw/img/syllables/fog.png", :size 10, :type "audio"}
                 {:url "/raw/img/syllables/splash_cymbal.png", :size 10, :type "audio"}],
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/syllables/bg.png"}
                                             :decoration {:src "/raw/img/syllables/fog.png"}}
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
                                             :editable? {:drag true, :select true, :show-in-tree? true},
                                             :metadata  {:added-character? true}
                                             :anim       "idle"
                                             :start      true
                                             :scene-name "vera"}
                        :splash-cymbal      {:x          349,
                                             :y          510,
                                             :width      257,
                                             :height     157,
                                             :type       "image",
                                             :src        "/raw/img/syllables/splash_cymbal.png",
                                             :scene-name "splash-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "splash-cymbal-tap"
                                                                  :on   "click"}}},
                        :group              {:x          313,
                                             :y          640,
                                             :width      1201,
                                             :height     440,
                                             :type       "image",
                                             :src        "/raw/img/syllables/group.png",
                                             :scene-name "group",},
                        :bass-drum          {:x          738,
                                             :y          688,
                                             :width      372,
                                             :height     392,
                                             :type       "image",
                                             :src        "/raw/img/syllables/bass_drum.png",
                                             :scene-name "bass-drum",
                                             :actions    {:click {:type "action"
                                                                  :id   "bass-drum-tap"
                                                                  :on   "click"}}},
                        :hi-hat             {:x          143,
                                             :y          695,
                                             :width      314,
                                             :height     165,
                                             :type       "image",
                                             :src        "/raw/img/syllables/hi_hat.png",
                                             :scene-name "hi-hat",
                                             :actions    {:click {:type "action"
                                                                  :id   "hi-hat-tap"
                                                                  :on   "click"}}},
                        :share-drum         {:x          492,
                                             :y          713,
                                             :width      293,
                                             :height     262,
                                             :type       "image",
                                             :src        "/raw/img/syllables/share_drum.png",
                                             :scene-name "share-drum",
                                             :actions    {:click {:type "action"
                                                                  :id   "share-drum-tap"
                                                                  :on   "click"}}},
                        :tom-tom-left       {:x          638,
                                             :y          522,
                                             :width      227,
                                             :height     276,
                                             :type       "image",
                                             :src        "/raw/img/syllables/tom_tom_left.png",
                                             :scene-name "tom-tom-left",
                                             :actions    {:click {:type "action"
                                                                  :id   "tom-tom-left-tap"
                                                                  :on   "click"}}}
                        :tom-tom-right      {:x          966,
                                             :y          522,
                                             :width      229,
                                             :height     277,
                                             :type       "image",
                                             :src        "/raw/img/syllables/tom_tom_right.png",
                                             :scene-name "tom-tom-right",
                                             :actions    {:click {:type "action"
                                                                  :id   "tom-tom-right-tap"
                                                                  :on   "click"}}}
                        :ride-cymbal        {:x          1250,
                                             :y          517,
                                             :width      335,
                                             :height     187,
                                             :type       "image",
                                             :src        "/raw/img/syllables/ride_cymbal.png",
                                             :scene-name "ride-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "ride-cymbal-tap"
                                                                  :on   "click"}}}
                        :china-cymbal       {:x          1387,
                                             :y          530,
                                             :width      444,
                                             :height     279,
                                             :type       "image",
                                             :src        "/raw/img/syllables/china_cymbal.png",
                                             :scene-name "china-cymbal",
                                             :actions    {:click {:type "action"
                                                                  :id   "china-cymbal-tap"
                                                                  :on   "click"}}}
                        :text-rectangle     {:type          "rectangle"
                                             :border-radius 56
                                             :fill          0xffffff
                                             :width         856,
                                             :height        320,
                                             :x             532,
                                             :y             104}

                        },
        :scene-objects [["layered-background"]
                        ["group" "bass-drum" "tom-tom-left" "tom-tom-right" "splash-cymbal" "hi-hat" "share-drum" "ride-cymbal" "china-cymbal"]
                        ["text-rectangle" "vera"]],
        :actions       {:splash-cymbal-dialog  (dialog/default "splash-cymbal-dialog")
                        :splash-cymbal-tap     {:type "parallel",
                                                :data [{:type "action" :id "splash-cymbal-dialog"}
                                                       {:type "action" :id "blink-splash-cymbal"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :bass-drum-dialog      (dialog/default "bass-drum-dialog")
                        :bass-drum-tap         {:type "parallel",
                                                :data [{:type "action" :id "bass-drum-dialog"}
                                                       {:type "action" :id "blink-bass-drum"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :hi-hat-dialog         (dialog/default "hi-hat-dialog")
                        :hi-hat-tap            {:type "parallel",
                                                :data [{:type "action" :id "hi-hat-dialog"}
                                                       {:type "action" :id "blink-hi-hat"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :share-drum-dialog     (dialog/default "snare-drum-dialog")
                        :share-drum-tap        {:type "parallel",
                                                :data [{:type "action" :id "share-drum-dialog"}
                                                       {:type "action" :id "blink-share-drum"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :tom-tom-left-dialog   (dialog/default "tom-tom-left-dialog")
                        :tom-tom-left-tap      {:type "parallel",
                                                :data [{:type "action" :id "tom-tom-left-dialog"}
                                                       {:type "action" :id "blink-tom-tom-left"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :tom-tom-right-dialog  (dialog/default "tom-tom-right-dialog")
                        :tom-tom-right-tap     {:type "parallel",
                                                :data [{:type "action" :id "tom-tom-right-dialog"}
                                                       {:type "action" :id "blink-tom-tom-right"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :ride-cymbal-dialog    (dialog/default "ride-cymbal-dialog")
                        :ride-cymbal-tap       {:type "parallel",
                                                :data [{:type "action" :id "ride-cymbal-dialog"}
                                                       {:type "action" :id "blink-ride-cymbal"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :china-cymbal-dialog   (dialog/default "china-cymbal-dialog")
                        :china-cymbal-tap      {:type "parallel",
                                                :data [{:type "action" :id "china-cymbal-dialog"}
                                                       {:type "action" :id "blink-china-cymbal"}
                                                       {:type "action" :id "cymbal-click"}]}

                        :cymbal-click          {:type "sequence-data",
                                                :data [{:type "action" :id "next-counter"}
                                                       {:type "action" :id "next-syllable"}]}

                        :next-syllable         {:type      "animate-next-text",
                                                :animation "color",
                                                :from-var  [{:var-name "word-name", :action-property "target"}]}
                        
                        :next-counter          {:type "next-timeout-counter"
                                                :id   "syllables-counter"}
                        :timeout-timer         {:type     "start-timeout-counter",
                                                :id       "syllables-counter",
                                                :action   "check-result",
                                                :interval 2000}
                        :check-result          {:type        "sequence-data",
                                                :data        [{:type "action" :id "hide-text"}
                                                              {:type    "test-value",
                                                               :success "success-task-action",
                                                               :fail    "wrong-task-action",
                                                               :from-var
                                                               [{:var-name "syllable-count", :action-property "value1"}
                                                                {:var-name "syllables-counter-value", :action-property "value2"}]}]}

                        :wrong-task-action     {:type "sequence-data",
                                                :data [{:type "action" :id "wrong-answer-dialog"}
                                                       {:type "action"
                                                        :from-var [{:var-name "task-name", :action-property "id"}]}]}

                        :success-task-action   {:type "sequence-data",
                                                :data [{:type "action" :id "correct-answer-dialog"}
                                                       {:type "finish-flows" :from-var [{:var-name "task-name", :action-property "tag"}]}]}

                        :intro                 (dialog/default "intro")
                        :correct-answer-dialog (dialog/default "corect answer dialog")
                        :wrong-answer-dialog   (dialog/default "wrong answer dialog")
                        :intro-task-dialog     (dialog/default "intro task dialog")

                        :start-scene           {:type "sequence-data",
                                                :data [{:type "start-activity"}
                                                       {:type "action" :id "intro"}
                                                       {:type "action" :id "intro-task-dialog"}
                                                       {:type "action" :id "finish-scene"}]}

                        :finish-scene          {:type "sequence-data",
                                                :data [{:type "action" :id "hide-text"}
                                                       {:type "action" :id "finish-dialog"}
                                                       {:type "action" :id "play-30-seconds"}]}

                        :play-30-seconds       {:type      "start-timeout-counter",
                                                :id        "play-30-seconds",
                                                :action    "finish-activity",
                                                :autostart true
                                                :interval  30000}

                        :hide-text             {:type "parallel"
                                                :data []}
                        :stop-activity         {:type "stop-activity"}
                        :finish-activity       {:type "finish-activity"}
                        :finish-dialog         (dialog/default "finish")}

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
                                     :nodes []}]}})

(defn- text-object
  [old-data args]
  {:type           "text",
   :x              725,
   :y              203,
   :width          470,
   :height         122,
   :align          "center",
   :chunks         (:result (reduce (fn [result syllable]
                                      (let [len (count syllable)
                                            start (:last result)
                                            end (+ start len)]
                                        (-> result
                                            (update :result conj {:end end, :start start})
                                            (assoc :last end))
                                        )) {:last 0 :result []} (str/split (:word args) #" "))),
   :fill           "#000000",
   :font-family    "Liberation Sans",
   :font-size      104,
   :text           (str/replace (:word args) " " ""),
   :vertical-align "middle",
   :visible        false,
   :editable?      {:show-in-tree? true
                    :select true}})

(defn task-start-action
  [scene-data args]
  (let [task-start-name (common/make-name-unique scene-data "task-setup")
        dialog-name (common/make-name-unique scene-data "word-dialog")]
    {:type "sequence-data",
     :workflow-user-input true
     :tags [task-start-name]
     :data [{:type "action" :id "hide-text"}
            {:type "reset-animate-text", :animation "color",
             :fill "#000000", :target (common/make-name-unique scene-data "text")}
            {:type "set-attribute" :attr-name "visible" :attr-value true :target (common/make-name-unique scene-data "text")}
            {:type "set-variable" :var-name "word-name" :var-value (common/make-name-unique scene-data "text")}
            {:type "set-variable" :var-name "syllable-count" :var-value (count (clojure.string/split (:word args) #" "))}
            {:type "set-variable" :var-name "task-name" :var-value task-start-name}
            {:type "action" :id dialog-name}
            {:type "action" :id "timeout-timer"}]}))

(defn- show-text-action
  [word-name]
  {:type "sequence-data"
   :data [{:type "set-attribute"
           :attr-name "visible"
           :attr-value true
           :target word-name}
          {:type "set-variable"
           :var-name "word-name"
           :var-value word-name}]})

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (common/add-available-action "hide-text" "Hide text")
      (common/add-available-action "next-syllable" "Next syllable")
      (common/add-blink "splash-cymbal" "Blink splash cymbal")
      (common/add-blink "bass-drum" "Blink bass drum")
      (common/add-blink "hi-hat" "Blink hi hat")
      (common/add-blink "share-drum" "Blink snare drum")
      (common/add-blink "tom-tom-left" "Blink tom tom left")
      (common/add-blink "tom-tom-right" "Blink tom tom right")
      (common/add-blink "ride-cymbal" "Blink ride cymbal")
      (common/add-blink "china-cymbal" "Blink china cymbal")))

(defn fu
  [old-data args]
  (let [word-name (common/make-name-unique old-data "text")
        word (text-object old-data args)

        dialog-name (common/make-name-unique old-data "word-dialog")
        dialog (dialog/default (:word args))

        task-start-name (common/make-name-unique old-data "task-setup")
        task-start (task-start-action old-data args)

        show-text-name (common/make-name-unique old-data "show-text")
        show-text (show-text-action word-name)]

    (-> old-data
        (assoc-in [:objects (keyword word-name)] word)
        (assoc-in [:actions (keyword dialog-name)] dialog)
        (assoc-in [:actions (keyword show-text-name)] show-text)        
        (common/add-track-action {:track-name "Words"
                                  :type       "dialog"
                                  :action-id  (keyword dialog-name)})
        (assoc-in [:actions (keyword task-start-name)] task-start)
        (update-in [:actinons :hide-text :data] concat [{:type "set-attribute"
                                                         :attr-name "visible"
                                                         :attr-value false
                                                         :target word-name}])
        (common/add-available-action show-text-name (str "Show text " (:word args)))
        (common/add-available-action task-start-name (str "Ask text " (:word args)))
        (common/add-scene-object [word-name])
        (common/update-unique-suffix))))

(core/register-template
  m f fu)
