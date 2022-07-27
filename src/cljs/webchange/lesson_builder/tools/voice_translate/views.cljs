(ns webchange.lesson-builder.tools.voice-translate.views
  (:require
    [webchange.lesson-builder.components.block.views :refer [block]]
    [webchange.lesson-builder.components.info.views :refer [info]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.widgets.audio-add.views :refer [audio-add]]
    [webchange.lesson-builder.widgets.audio-list.views :refer [audio-list]]
    [webchange.lesson-builder.widgets.welcome-translate.views :refer [welcome-translate]]))

(defn audio-manager
  []
  [:div.tool--voice-translate--audio-manager
   [:h1.audio-manager--header
    "Voice & Translate 2"]
   [block {:title  "Add Audio"
           :border "none"}
    [audio-add]]
   [block {:title "Select Audio"}
    [info "Record audio or upload audio and the file will automatically show in this box to select and edit when done."]
    [audio-list]]])

(defn audio-editor
  []
  ;[toolbox {:title "Voice & Translate Steps:"
  ;          :icon  "translate"}
  ; "audio-editor"]
  [welcome-translate])
