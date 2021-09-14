(ns webchange.voice
  (:require
   [webchange.common.voice-recognition.voice-recognition :as vr]))

(comment

  (let [path "/upload/HJLJLDMDIGVNQRIS.mp3"]
    (vr/try-voice-recognition-audio path "english"))
  )
