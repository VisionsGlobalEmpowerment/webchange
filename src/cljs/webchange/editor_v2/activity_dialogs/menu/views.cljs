(ns webchange.editor-v2.activity-dialogs.menu.views
  (:require
   [re-frame.core :as re-frame]
   [webchange.editor-v2.activity-dialogs.menu.sections.delay.views :as delay]
   [webchange.editor-v2.activity-dialogs.menu.sections.effects.views :as effects]
   [webchange.editor-v2.activity-dialogs.menu.sections.phrase.views :as phrase]
   [webchange.editor-v2.activity-dialogs.menu.sections.text-animation.views :as text-animation]
   [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.views :as voice-over]
   [webchange.editor-v2.activity-dialogs.menu.sections.translate.views :as translate]
   [webchange.editor-v2.activity-dialogs.menu.state :as state]
   [webchange.editor-v2.text-animation-editor.views :refer [text-chunks-modal]]
   [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
   [webchange.ui-framework.components.index :refer [button]]
   [webchange.ui-framework.layout.right-menu.views-menu-section :refer [menu-section]]))

(defn activity-dialogs-menu
  []
  (let [sections @(re-frame/subscribe [::state/available-sections])
        handle-save #(re-frame/dispatch [::translator-form.form/save-changes])]
    [:div.dialogs-menu
     (for [{:keys [id name icon]} sections]
       ^{:key id}
       [menu-section {:title-icon icon
                      :title-text name}
        (case id
          :delay [delay/form]
          :effects [effects/form]
          :phrase [phrase/form]
          :text-animation [text-animation/form]
          :voice-over [voice-over/form]
          :translate [translate/form]
          nil)])
     [:div.menu-bottom-actions
      [button {:on-click   handle-save
               :size       "big"
               :class-name "apply-button"}
       "Apply"]]
     [text-chunks-modal]]))
