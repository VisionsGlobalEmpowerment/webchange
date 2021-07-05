(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-add-audio :refer [add-audio]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-audios-list :refer [audios-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-current-audio :refer [current-audio]]))

(defn form
  []
  [:div.voice-over-form
   [section-block {:title "Current selection"}
    [current-audio]]
   [section-block {:title "Add audio"}
    [add-audio]]
   [section-block {:title "Select available audio"}
    [audios-list]]])
