(ns webchange.lesson-builder.tools.question-form.index
  (:require
    [webchange.lesson-builder.tools.question-form.object-form.views :refer [question-object-form]]
    [webchange.lesson-builder.tools.question-form.menu.views :refer [question-menu]]
    [webchange.lesson-builder.tools.question-form.toolbox.views :refer [question-toolbox]]
    [webchange.lesson-builder.tools.question-form.state :as state]))

(def toolbox question-toolbox)

(def menu {:question-form   question-menu
           :question-option question-object-form})

(def data {:menu    true
           :toolbox true
           :focus   #{:toolbox :stage :menu}
           :init    [::state/init-state]
           :reset   [::state/reset-state]})
