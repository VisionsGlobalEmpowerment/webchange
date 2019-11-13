(ns webchange.editor.common.actions.action-types)

(def action-types
  [{:key :action :value :action :text "Action"}
   {:key :scene :value :scene :text "Scene"}
   {:key :state :value :state :text "State"}
   {:key :animation :value :animation :text "Animation"}
   {:key :audio :value :audio :text "Audio"}
   {:key :empty :value :empty :text "Empty"}
   {:key :sequence :value :sequence :text "Sequence"}
   {:key :sequence-data :value :sequence-data :text "Sequence Data"}
   {:key :parallel :value :parallel :text "Parallel"}
   {:key :remove-flows :value :remove-flows :text "Remove flows"}

   {:key :add-alias :value :add-alias :text "Add alias"}
   {:key :add-animation :value :add-animation :text "Add animation"}
   {:key :start-animation :value :start-animation :text "Start animation"}
   {:key :remove-animation :value :remove-animation :text "Remove animation"}
   {:key :set-skin :value :set-skin :text "Set skin"}
   {:key :animation-props :value :animation-props :text "Animation props"}
   {:key :animation-sequence :value :animation-sequence :text "Animation sequence"}
   {:key :transition :value :transition :text "Transition"}
   {:key :placeholder-audio :value :placeholder-audio :text "Placeholder audio"}
   {:key :test-transitions-collide :value :test-transitions-collide :text "Test transitions collide"}

   {:key :dataset-var-provider :value :dataset-var-provider :text "Dataset var provider"}
   {:key :lesson-var-provider :value :lesson-var-provider :text "Lesson var provider"}
   {:key :vars-var-provider :value :vars-var-provider :text "Vars var provider"}
   {:key :test-var :value :test-var :text "Test var"}
   {:key :test-var-scalar :value :test-var-scalar :text "Test var scalar"}
   {:key :test-var-list :value :test-var-list :text "Test var list"}
   {:key :test-value :value :test-value :text "Test value"}
   {:key :case :value :case :text "Case"}
   {:key :counter :value :counter :text "Counter"}
   {:key :set-variable :value :set-variable :text "Set variable"}
   {:key :set-progress :value :set-progress :text "Set progress"}
   {:key :copy-variable :value :copy-variable :text "Copy variable"}
   {:key :text-animation :value :text-animation :text "Text animation"}])
