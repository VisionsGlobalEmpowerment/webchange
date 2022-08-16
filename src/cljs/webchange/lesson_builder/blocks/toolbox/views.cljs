(ns webchange.lesson-builder.blocks.toolbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.toolbox.state :as state]
    [webchange.lesson-builder.tools.background-image.views :refer [background-image]]
    [webchange.lesson-builder.tools.voice-translate.views :refer [audio-editor]]
    [webchange.lesson-builder.tools.question-form.index :as question-form]
    [webchange.lesson-builder.widgets.welcome.views :refer [welcome]]
    [webchange.ui.index :as ui]))

(def toolboxes {:welcome           welcome
                :welcome-translate audio-editor
                :background-image  background-image
                :question-form     question-form/toolbox})

;;

(defn block-toolbox
  [{:keys [class-name]}]
  (let [current-widget @(re-frame/subscribe [::state/current-widget])
        component (get toolboxes current-widget)]
    [:div {:class-name (ui/get-class-name {"block-toolbox" true
                                           class-name      (some? class-name)})}
     [component]]))
