(ns webchange.lesson-builder.tools.question-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.check-list.views :refer [check-list]]
    [webchange.lesson-builder.components.info.views :refer [info]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.ui.index :as ui]))

(defn- question-type
  []
  (let [value @(re-frame/subscribe [::state/question-type])
        options @(re-frame/subscribe [::state/question-type-options])
        handle-change #(re-frame/dispatch [::state/set-question-type %])]
    [check-list {:items    options
                 :value    value
                 :on-click handle-change}]))

(defn question-params
  []
  [:div.question-form--question-params
   [:h1 "Edit Question"]
   [info "Name your question so you can find and drag it into the correct place in the Script editor."]
   [ui/input {:label "Question Name"}]
   [:h1 "Select Question Type"]
   [question-type]])

(defn question-options
  []
  (let []
    [toolbox]))
