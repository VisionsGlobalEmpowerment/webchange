(ns webchange.lesson-builder.tools.question-form.toolbox.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.question-form.question-options.state :as question-state]))

(re-frame/reg-sub
  ::title
  :<- [::question-state/question-type]
  :<- [::question-state/question-type-options]
  (fn [[question-type options]]
    (or (some (fn [{:keys [text value]}]
                (and (= value question-type) text))
              options)
        "New Question")))
