(ns webchange.editor-v2.activity-dialogs.menu.sections.translate.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as re-frame]
   [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
   [webchange.editor-v2.activity-dialogs.menu.sections.translate.state :as state]
   [webchange.ui-framework.components.index :refer [input button]]))

(defn form
  []
  (r/with-let [lang (r/atom "es")
               handle-translate #(re-frame/dispatch [::state/translate @lang])]
    [:div.delay-form
     [section-block {:title "Translate"}
      [input {:value     @lang
              :on-change #(reset! lang %)}]
      [button {:on-click handle-translate
               :varient "outlined"} "Translate"]]]))
