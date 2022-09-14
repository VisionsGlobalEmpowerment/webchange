(ns webchange.lesson-builder.blocks.toolbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.toolbox.state :as state]
    [webchange.lesson-builder.tools.background-image.index :as background-image]
    [webchange.lesson-builder.tools.flipbook-add-page.views :as page-add]
    [webchange.lesson-builder.tools.question-form.index :as question-form]
    [webchange.lesson-builder.tools.voice-translate.index :as voice-translate]
    [webchange.lesson-builder.widgets.pages.views :refer [activity-pages]]
    [webchange.lesson-builder.widgets.welcome.views :refer [welcome]]
    [webchange.ui.index :as ui]))

(def toolboxes {:welcome          welcome
                :background-image background-image/toolbox
                :add-page         page-add/select-layout
                :pages            activity-pages
                :question-form    question-form/toolbox
                :voice-translate  voice-translate/toolbox})

(defn block-toolbox
  [{:keys [class-name]}]
  (let [current-widget @(re-frame/subscribe [::state/current-widget])
        component (get toolboxes current-widget)]
    [:div {:class-name (ui/get-class-name {"block-toolbox" true
                                           class-name      (some? class-name)})}
     [component]]))
