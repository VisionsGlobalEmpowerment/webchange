(ns webchange.lesson-builder.tools.question-form.question-option.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.question-form.question-option.state :as state]
    [webchange.lesson-builder.widgets.object-form.views :refer [object-form]]))

(defn question-option
  []
  (let [object-data @(re-frame/subscribe [::state/object-data])
        form-param @(re-frame/subscribe [::state/form-param])
        handle-change #(re-frame/dispatch [::state/handle-data-change form-param %])]
    ^{:key form-param}
    [:div
     [:h1 "Edit question option"]
     [object-form {:data      object-data
                   :on-change handle-change}]]))
