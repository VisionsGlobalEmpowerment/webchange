(ns webchange.editor-v2.diagram.scene-data-parser.parser-test-scene-1-parsed)

(def data {:box1                               {:type "object"
                                                :next [:click-on-box1]}
           :click-on-box1                      {:type "test-var-scalar"
                                                :next [:first-word
                                                       :pick-wrong]}
           :first-word                         {:type "sequence"
                                                :next [:show-first-box-word]}
           :show-first-box-word                {:type   "parallel"
                                                :parent :first-word
                                                :next   [:show-first-box-word-0
                                                         :show-first-box-word-1
                                                         :show-first-box-word-2
                                                         :show-first-box-word-3]}
           :show-first-box-word-0              {:type   "animation"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]}
           :show-first-box-word-1              {:type   "set-skin"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]}
           :show-first-box-word-2              {:type   "copy-variable"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]}
           :show-first-box-word-3              {:type   "add-animation"
                                                :parent :show-first-box-word
                                                :next   [:introduce-word]}

           :introduce-word                     {:type   "sequence"
                                                :parent :first-word
                                                :next   [:empty-big-copy-1]}
           :empty-big-copy-1                   {:type     "empty"
                                                :original :empty-big
                                                :parent   :introduce-word
                                                :next     [:vaca-this-is-var]}

           :vaca-this-is-var                   {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-small-copy-1]}

           :empty-small-copy-1                 {:type     "empty"
                                                :parent   :introduce-word
                                                :original :empty-small
                                                :next     [:vaca-can-you-say]}

           :vaca-can-you-say                   {:type   "animation-sequence"
                                                :parent :introduce-word
                                                :next   [:vaca-question-var]}

           :vaca-question-var                  {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-small-copy-2]}

           :empty-small-copy-2                 {:type     "empty"
                                                :parent   :introduce-word
                                                :original :empty-small
                                                :next     [:vaca-word-var]}

           :vaca-word-var                      {:type   "action"
                                                :parent :introduce-word
                                                :next   [:empty-big-copy-2]}

           :empty-big-copy-2                   {:type     "empty"
                                                :original :empty-big
                                                :parent   :introduce-word
                                                :next     [:bye-current-box]}

           :bye-current-box                    {:type   "sequence-data"
                                                :parent :first-word
                                                :next   [:bye-current-box-0]}

           :bye-current-box-0                  {:type   "parallel"
                                                :parent :bye-current-box
                                                :next   [:bye-current-box-0-0
                                                         :bye-current-box-0-1]}

           :bye-current-box-0-0                {:type   "animation"
                                                :parent :bye-current-box-0
                                                :next   [:bye-current-box-1]}

           :bye-current-box-0-1                {:type   "transition"
                                                :parent :bye-current-box-0
                                                :next   [:bye-current-box-1]}

           :bye-current-box-1                  {:type   "state"
                                                :parent :bye-current-box
                                                :next   [:set-current-box2]}

           :set-current-box2                   {:type   "parallel"
                                                :parent :first-word
                                                :next   [:set-current-box2-0
                                                         :set-current-box2-1]}

           :set-current-box2-0                 {:type   "set-variable"
                                                :parent :set-current-box2
                                                :next   [:senora-vaca-audio-touch-second-box]}
           :set-current-box2-1                 {:type   "set-variable"
                                                :parent :set-current-box2
                                                :next   [:senora-vaca-audio-touch-second-box]}

           :senora-vaca-audio-touch-second-box {:type   "animation-sequence"
                                                :parent :first-word
                                                :next   []}

           :pick-wrong                         {:type "sequence"
                                                :next [:audio-wrong]}

           :audio-wrong                        {:type   "audio"
                                                :parent :pick-wrong
                                                :next   []}})