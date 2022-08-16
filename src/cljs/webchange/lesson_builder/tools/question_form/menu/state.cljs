(ns webchange.lesson-builder.tools.question-form.menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.question-form.state :as state]))

(re-frame/reg-sub
  ::current-action
  :<- [::state/question-info]
  (fn [{:keys [question-index]}]
    (if (some? question-index) :edit :add)))

(re-frame/reg-sub
  ::menu-title
  :<- [::current-action]
  (fn [current-action]
    (case current-action
      :edit "Edit Question"
      :add "Add Question")))
