(ns webchange.lesson-builder.tools.voice-translate.index
  (:require
    [webchange.lesson-builder.tools.voice-translate.views :refer [audio-editor audio-manager]]))

(def menu audio-manager)
(def toolbox audio-editor)

(def data {:menu    true
           :toolbox true
           :focus   #{:toolbox :script :menu}})
