(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.views
  (:require
    [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.add-audio.views :refer [add-audio]]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.audios-list.views :refer [audios-list]]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.views :refer [select-region]]))

(defn form
  []
  [:div.voice-over-form
   [section-block {:title "Current selection"}
    [select-region]]
   [section-block {:title "Add audio"}
    [add-audio]]
   [section-block {:title "Select available audio"}
    [audios-list]]])
