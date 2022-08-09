(ns webchange.lesson-builder.tools.question-form.index
  (:require
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.lesson-builder.tools.question-form.views :as views]))

(def data {:menu    views/question-params
           :toolbox views/question-options
           :focus   #{:toolbox :stage :menu}
           :init    [::state/init-state]
           :reset   [::state/reset-state]})
