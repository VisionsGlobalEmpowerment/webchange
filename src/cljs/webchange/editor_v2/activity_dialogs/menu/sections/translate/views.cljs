(ns webchange.editor-v2.activity-dialogs.menu.sections.translate.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.activity-dialogs.menu.sections.translate.state :as state]
    [webchange.ui-framework.components.index :refer [button label select]]))

(defn form
  []
  (r/with-let [lang (r/atom "es")
               handle-translate #(re-frame/dispatch [::state/translate @lang])]
    (let [options @(re-frame/subscribe [::state/lang-options])]
      [:div.translate-form
       [section-block {:title "Translate"}
        [:div.select-language
         [select {:value     @lang
                  :options   options
                  :on-change #(reset! lang %)
                  :variant   "outlined"}]
         [button {:on-click   handle-translate
                  :variant    "outlined"
                  :class-name "translate-button"}
          "Translate"]]]])))
