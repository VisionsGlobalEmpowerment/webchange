(ns webchange.editor-v2.diagram.scene-data-parser.parser-test-scene-1-parsed)

(def data {:box1                               {:type "object"
                                                :next [:click-on-box1]}

           :click-on-box1                      {:type        "test-var-scalar"
                                                :connections {:box1 {:next [:first-word
                                                                            :pick-wrong]}}
                                                :data        {:type     "test-var-scalar"
                                                              :var-name "current-box"
                                                              :value    "box1"
                                                              :success  "first-word"
                                                              :fail     "pick-wrong"}}
           :first-word                         {:type        "sequence"
                                                :connections {:click-on-box1 {:next [:show-first-box-word]}}
                                                :data        {:type       "sequence"
                                                              :data       ["show-first-box-word"
                                                                           "introduce-word"
                                                                           "bye-current-box"
                                                                           "set-current-box2"
                                                                           "senora-vaca-audio-touch-second-box"]
                                                              :unique-tag "box"}}
           :show-first-box-word                {:type        "parallel"
                                                :connections {:first-word {:parent :first-word
                                                                           :next   [:show-first-box-word-0
                                                                                    :show-first-box-word-1
                                                                                    :show-first-box-word-2
                                                                                    :show-first-box-word-3]}}
                                                :data        {:type "parallel"
                                                              :data [{:type "animation" :target "box1" :id "wood" :loop false}
                                                                     {:type     "set-skin" :target "box1"
                                                                      :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                                     {:type "copy-variable" :var-name "current-word" :from "item-1"}
                                                                     {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}}
           :show-first-box-word-0              {:type        "animation"
                                                :connections {:show-first-box-word {:parent :show-first-box-word
                                                                                    :next   [:introduce-word]}}
                                                :data        {:type "animation" :target "box1" :id "wood" :loop false}}
           :show-first-box-word-1              {:type        "set-skin"
                                                :connections {:show-first-box-word {:parent :show-first-box-word
                                                                                    :next   [:introduce-word]}}
                                                :data        {:type     "set-skin" :target "box1"
                                                              :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}}
           :show-first-box-word-2              {:type        "copy-variable"
                                                :connections {:show-first-box-word {:parent :show-first-box-word
                                                                                    :next   [:introduce-word]}}
                                                :data        {:type "copy-variable" :var-name "current-word" :from "item-1"}}
           :show-first-box-word-3              {:type        "add-animation"
                                                :connections {:show-first-box-word {:parent :show-first-box-word
                                                                                    :next   [:introduce-word]}}
                                                :data        {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}}

           :introduce-word                     {:type        "sequence"
                                                :connections {:show-first-box-word {:parent :first-word
                                                                                    :next   [:empty-big]}}
                                                :data        {:type "sequence"
                                                              :data ["empty-big"
                                                                     "vaca-this-is-var"
                                                                     "empty-small"
                                                                     "vaca-can-you-say"
                                                                     "vaca-question-var"
                                                                     "empty-small"
                                                                     "vaca-word-var"
                                                                     "empty-big"]}}
           :empty-big                          {:type        "empty"
                                                :connections {:introduce-word {:parent :introduce-word
                                                                               :next   [:vaca-this-is-var]}
                                                              :vaca-word-var  {:parent :introduce-word
                                                                               :next   [:bye-current-box]}}
                                                :data        {:type "empty" :duration 1000}}

           :vaca-this-is-var                   {:type        "action"
                                                :connections {:empty-big {:parent :introduce-word
                                                                          :next   [:empty-small]}}
                                                :data        {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-this-is-action"}]}}

           :empty-small                        {:type        "empty"
                                                :connections {:vaca-this-is-var  {:parent :introduce-word
                                                                                  :next   [:vaca-can-you-say]}
                                                              :vaca-question-var {:parent :introduce-word
                                                                                  :next   [:vaca-word-var]}}
                                                :data        {:type "empty" :duration 500}}

           :vaca-can-you-say                   {:type        "animation-sequence"
                                                :connections {:empty-small {:parent :introduce-word
                                                                            :next   [:vaca-question-var]}}
                                                :data        {:type     "animation-sequence",
                                                              :target   "senoravaca",
                                                              :track    1,
                                                              :offset   20.28,
                                                              :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                              :data     [{:start 20.363, :end 20.98, :duration 0.617, :anim "talk"}],
                                                              :start    20.28,
                                                              :duration 0.813}}

           :vaca-question-var                  {:type        "action"
                                                :connections {:vaca-can-you-say {:parent :introduce-word
                                                                                 :next   [:empty-small]}}

                                                :data        {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-question-action"}]}}

           :vaca-word-var                      {:type        "action"
                                                :connections {:empty-small {:parent :introduce-word
                                                                            :next   [:empty-big]}}
                                                :data        {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-word-action"}]}}

           :bye-current-box                    {:type        "sequence-data"
                                                :connections {:empty-big {:parent :first-word
                                                                          :next   [:bye-current-box-0]}}
                                                :data        [{:type "parallel"
                                                               :data [{:type     "animation" :id "jump"
                                                                       :from-var [{:var-name "current-box" :action-property "target"}]}
                                                                      {:type     "transition" :to {:y -100 :duration 2}
                                                                       :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                                  {:var-name "current-position-x" :action-property "to.x"}]}]}
                                                              {:type     "state" :id "default"
                                                               :from-var [{:var-name "current-box" :action-property "target"}]}]}

           :bye-current-box-0                  {:type        "parallel"
                                                :connections {:bye-current-box {:parent :bye-current-box
                                                                                :next   [:bye-current-box-0-0
                                                                                         :bye-current-box-0-1]}}
                                                :data        {:type "parallel"
                                                              :data [{:type     "animation" :id "jump"
                                                                      :from-var [{:var-name "current-box" :action-property "target"}]}
                                                                     {:type     "transition" :to {:y -100 :duration 2}
                                                                      :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                                 {:var-name "current-position-x" :action-property "to.x"}]}]}}

           :bye-current-box-0-0                {:type        "animation"
                                                :connections {:bye-current-box-0 {:parent :bye-current-box-0
                                                                                  :next   [:bye-current-box-1]}}
                                                :data        {:type     "animation" :id "jump"
                                                              :from-var [{:var-name "current-box" :action-property "target"}]}}

           :bye-current-box-0-1                {:type        "transition"
                                                :connections {:bye-current-box-0 {:parent :bye-current-box-0
                                                                                  :next   [:bye-current-box-1]}}
                                                :data        {:type     "transition" :to {:y -100 :duration 2}
                                                              :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                         {:var-name "current-position-x" :action-property "to.x"}]}}

           :bye-current-box-1                  {:type        "state"
                                                :connections {:bye-current-box-0 {:parent :bye-current-box
                                                                                  :next   [:set-current-box2]}}
                                                :data        {:type     "state" :id "default"
                                                              :from-var [{:var-name "current-box" :action-property "target"}]}}

           :set-current-box2                   {:type        "parallel"
                                                :connections {:bye-current-box {:parent :first-word
                                                                                :next   [:set-current-box2-0
                                                                                         :set-current-box2-1]}}
                                                :data        {:type "parallel"
                                                              :data [{:type "set-variable" :var-name "current-box" :var-value "box2"}
                                                                     {:type "set-variable" :var-name "current-position-x" :var-value 850}]}}

           :set-current-box2-0                 {:type        "set-variable"
                                                :connections {:set-current-box2 {:parent :set-current-box2
                                                                                 :next   [:senora-vaca-audio-touch-second-box]}}
                                                :data        {:type "set-variable" :var-name "current-box" :var-value "box2"}}

           :set-current-box2-1                 {:type        "set-variable"
                                                :connections {:set-current-box2 {:parent :set-current-box2
                                                                                 :next   [:senora-vaca-audio-touch-second-box]}}
                                                :data        {:type "set-variable" :var-name "current-position-x" :var-value 850}}

           :senora-vaca-audio-touch-second-box {:type        "animation-sequence"
                                                :connections {:set-current-box2 {:parent :first-word
                                                                                 :next   []}}
                                                :data        {:type     "animation-sequence",
                                                              :target   "senoravaca",
                                                              :track    1,
                                                              :offset   52.453,
                                                              :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                              :data     [{:start 52.6, :end 53.467, :duration 0.867, :anim "talk"}
                                                                         {:start 54.36, :end 56.307, :duration 1.947, :anim "talk"}
                                                                         {:start 56.987, :end 59.173, :duration 2.186, :anim "talk"}],
                                                              :start    52.453,
                                                              :duration 6.987}}

           :pick-wrong                         {:type        "sequence"
                                                :connections {:click-on-box1 {:next [:audio-wrong]}}
                                                :data        {:type "sequence"
                                                              :data ["audio-wrong"]}}

           :audio-wrong                        {:type        "audio"
                                                :connections {:pick-wrong {:parent :pick-wrong
                                                                           :next   []}}
                                                :data        {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}}})