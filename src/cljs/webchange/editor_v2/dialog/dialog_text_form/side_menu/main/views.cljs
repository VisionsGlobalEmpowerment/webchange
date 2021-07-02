(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.delay.views :as delay]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.views :as effects]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.phrase.views :as phrase]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.text-animation.views :as text-animation]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views :as voice-over]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as state]))

(defn- no-section
  []
  [:div.no-section
   [:div.icon "←"]
   [:div.message "Select Menu section"]])

(defn main
  []
  (let [{:keys [id name] :as current-section} @(re-frame/subscribe [::state/current-section])]
    (into [:div.main]
          (if (some? current-section)
            [[:div.section-title name]
             (case id
               :delay [delay/form]
               :effects [effects/form]
               :phrase [phrase/form]
               :text-animation [text-animation/form]
               :voice-over [voice-over/form]
               nil)]
            [[no-section]]))))
