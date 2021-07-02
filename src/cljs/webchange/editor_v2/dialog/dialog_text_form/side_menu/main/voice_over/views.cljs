(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-add-audio :refer [add-audio]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-audios-list :refer [audios-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-current-audio :refer [current-audio]]))

(defn form
  []
  (r/with-let [add-audio-open? (r/atom false)
               select-audio-open? (r/atom false)
               open-add-audio #(reset! add-audio-open? true)
               open-select-audio #(reset! select-audio-open? true)]
    [:div.voice-over-form
     [section-block {:title "Current selection"}
      [current-audio {:on-add-click open-add-audio
                      :on-select-click open-select-audio}]]
     [section-block {:title "Add audio"
                     :open? @add-audio-open?}
      [add-audio]]
     [section-block {:title "Select available audio"
                     :open? @select-audio-open?}
      [audios-list]]]))
