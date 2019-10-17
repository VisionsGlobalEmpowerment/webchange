(ns webchange.editor-v2.diagram.scene-data-parser.parser-test-scene-1-parsed)

(def data {:box1                               {:type "object"
                                                :next [:click-on-box1]}
           :click-on-box1                      {:type "test-var-scalar"
                                                :next [:first-word
                                                       :pick-wrong]
                                                :data {:type     "test-var-scalar"
                                                       :var-name "current-box"
                                                       :value    "box1"
                                                       :success  "first-word"
                                                       :fail     "pick-wrong"}}
           :first-word                         {:type "sequence"
                                                :next [:show-first-box-word]
                                                :data {:type       "sequence"
                                                       :data       ["show-first-box-word"
                                                                    "introduce-word"
                                                                    "bye-current-box"
                                                                    "set-current-box2"
                                                                    "senora-vaca-audio-touch-second-box"]
                                                       :unique-tag "box"}}
           :show-first-box-word                {:type   "parallel"
                                                :parent :first-word
                                                :next   [:show-first-box-word-0
                                                         :show-first-box-word-1
                                                         :show-first-box-word-2
                                                         :show-first-box-word-3]
                                                :data   {:type "parallel"
                                                         :data [{:type "animation" :target "box1" :id "wood" :loop false}
                                                                {:type     "set-skin" :target "box1"
                                                                 :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                                {:type "copy-variable" :var-name "current-word" :from "item-1"}
                                                                {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}}
           :show-first-box-word-0              {:type   "animation"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]
                                                :data   {:type "animation" :target "box1" :id "wood" :loop false}}
           :show-first-box-word-1              {:type   "set-skin"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]
                                                :data   {:type     "set-skin" :target "box1"
                                                         :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}}
           :show-first-box-word-2              {:type   "copy-variable"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]
                                                :data   {:type "copy-variable" :var-name "current-word" :from "item-1"}}
           :show-first-box-word-3              {:type   "add-animation"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]
                                                :data   {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}}

           :introduce-word                     {:type   "sequence"
                                                :parent :first-word
                                                :next   [:empty-big-copy-1]
                                                :data   {:type "sequence"
                                                         :data ["empty-big"
                                                                "vaca-this-is-var"
                                                                "empty-small"
                                                                "vaca-can-you-say"
                                                                "vaca-question-var"
                                                                "empty-small"
                                                                "vaca-word-var"
                                                                "empty-big"]}}
           :empty-big-copy-1                   {:type     "empty"
                                                :original :empty-big
                                                :parent   :introduce-word
                                                :next     [:vaca-this-is-var]
                                                :data     {}}

           :vaca-this-is-var                   {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-small-copy-1]
                                                :data   {}}

           :empty-big                          {:type "empty"
                                                :data {:type "empty" :duration 1000}}

           :empty-small                        {:type "empty"
                                                :data {:type "empty" :duration 500}}

           :empty-small-copy-1                 {:type     "empty"
                                                :parent   :introduce-word
                                                :original :empty-small
                                                :next     [:vaca-can-you-say]}

           :vaca-can-you-say                   {:type   "animation-sequence"
                                                :parent :introduce-word
                                                :next   [:vaca-question-var]
                                                :data   {:type     "animation-sequence",
                                                         :target   "senoravaca",
                                                         :track    1,
                                                         :offset   20.28,
                                                         :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                         :data     [{:start 20.363, :end 20.98, :duration 0.617, :anim "talk"}],
                                                         :start    20.28,
                                                         :duration 0.813}}

           :vaca-question-var                  {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-small-copy-2]
                                                :data   {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-question-action"}]}}

           :empty-small-copy-2                 {:type     "empty"
                                                :parent   :introduce-word
                                                :original :empty-small
                                                :next     [:vaca-word-var]}

           :vaca-word-var                      {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-big-copy-2]
                                                :data   {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-word-action"}]}}

           :empty-big-copy-2                   {:type     "empty"
                                                :original :empty-big
                                                :parent   :introduce-word
                                                :next     [:bye-current-box]}

           :bye-current-box                    {:type   "sequence-data"
                                                :parent :first-word
                                                :next   [:bye-current-box-0]
                                                :data   [{:type "parallel"
                                                          :data [{:type     "animation" :id "jump"
                                                                  :from-var [{:var-name "current-box" :action-property "target"}]}
                                                                 {:type     "transition" :to {:y -100 :duration 2}
                                                                  :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                             {:var-name "current-position-x" :action-property "to.x"}]}]}
                                                         {:type     "state" :id "default"
                                                          :from-var [{:var-name "current-box" :action-property "target"}]}]}

           :bye-current-box-0                  {:type   "parallel"
                                                :parent :bye-current-box
                                                :next   [:bye-current-box-0-0
                                                         :bye-current-box-0-1]
                                                :data   {:type "parallel"
                                                         :data [{:type     "animation" :id "jump"
                                                                 :from-var [{:var-name "current-box" :action-property "target"}]}
                                                                {:type     "transition" :to {:y -100 :duration 2}
                                                                 :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                            {:var-name "current-position-x" :action-property "to.x"}]}]}}

           :bye-current-box-0-0                {:type   "animation"
                                                :parent :bye-current-box-0
                                                :next   [:bye-current-box-1]
                                                :data   {:type     "animation" :id "jump"
                                                         :from-var [{:var-name "current-box" :action-property "target"}]}}

           :bye-current-box-0-1                {:type   "transition"
                                                :parent :bye-current-box-0
                                                :next   [:bye-current-box-1]
                                                :data   {:type     "transition" :to {:y -100 :duration 2}
                                                         :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                    {:var-name "current-position-x" :action-property "to.x"}]}}

           :bye-current-box-1                  {:type   "state"
                                                :parent :bye-current-box
                                                :next   [:set-current-box2]
                                                :data   {:type     "state" :id "default"
                                                         :from-var [{:var-name "current-box" :action-property "target"}]}}

           :set-current-box2                   {:type   "parallel"
                                                :parent :first-word
                                                :next   [:set-current-box2-0
                                                         :set-current-box2-1]
                                                :data   {:type "parallel"
                                                         :data [{:type "set-variable" :var-name "current-box" :var-value "box2"}
                                                                {:type "set-variable" :var-name "current-position-x" :var-value 850}]}}

           :set-current-box2-0                 {:type   "set-variable"
                                                :parent :set-current-box2
                                                :next   [:senora-vaca-audio-touch-second-box]
                                                :data   {:type "set-variable" :var-name "current-box" :var-value "box2"}}

           :set-current-box2-1                 {:type   "set-variable"
                                                :parent :set-current-box2
                                                :next   [:senora-vaca-audio-touch-second-box]
                                                :data   {:type "set-variable" :var-name "current-position-x" :var-value 850}}

           :senora-vaca-audio-touch-second-box {:type   "animation-sequence"
                                                :parent :first-word
                                                :next   []
                                                :data   {:type     "animation-sequence",
                                                         :target   "senoravaca",
                                                         :track    1,
                                                         :offset   52.453,
                                                         :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                                                         :data     [{:start 52.6, :end 53.467, :duration 0.867, :anim "talk"}
                                                                    {:start 54.36, :end 56.307, :duration 1.947, :anim "talk"}
                                                                    {:start 56.987, :end 59.173, :duration 2.186, :anim "talk"}],
                                                         :start    52.453,
                                                         :duration 6.987}}

           :pick-wrong                         {:type "sequence"
                                                :next [:audio-wrong]
                                                :data {:type "sequence"
                                                       :data ["audio-wrong"]}}

           :audio-wrong                        {:type   "audio"
                                                :parent :pick-wrong
                                                :next   []
                                                :data   {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}}})