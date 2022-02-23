(ns webchange.editor-v2.question.question-form.state.actions-utils
  (:require
    [webchange.editor-v2.translator.translator-form.state.utils :refer [insert-by-index
                                                                        remove-by-index]]))

(defn node->info
  [action-node]
  (let []
    {:type :question
     :path (:path action-node)
     :question-path (get-in action-node [:data :question-path])}))
