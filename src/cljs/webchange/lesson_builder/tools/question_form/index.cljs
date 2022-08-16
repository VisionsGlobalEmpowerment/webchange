(ns webchange.lesson-builder.tools.question-form.index
  (:require
    [webchange.lesson-builder.tools.question-form.question-option.views :refer [question-option]]
    [webchange.lesson-builder.tools.question-form.question-options.views :refer [question-options]]
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.lesson-builder.tools.question-form.views :as views]))

(def toolbox question-options)
(def menu {:question-form   views/question-params
           :question-option question-option})

(def data {:menu    true
           :toolbox true
           :focus   #{:toolbox :stage :menu}
           :init    [::state/init-state]
           :reset   [::state/reset-state]})
