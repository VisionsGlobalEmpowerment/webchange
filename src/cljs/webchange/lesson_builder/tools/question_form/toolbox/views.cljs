(ns webchange.lesson-builder.tools.question-form.toolbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.question-form.question-options.views :as q]
    [webchange.lesson-builder.tools.question-form.toolbox.state :as state]))

(defn question-toolbox
  []
  (let [title @(re-frame/subscribe [::state/title])]
    [toolbox {:title title
              :icon  "question"}
     [:div.question-form--question-toolbox
      [q/task-type]
      [q/answers-number]
      [q/options-number]
      [q/mark-options]
      [q/correct-answers]]]))