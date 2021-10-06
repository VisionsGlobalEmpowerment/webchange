(ns webchange.text-to-speech
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [config.core :refer [env]])
  (:import
    (com.google.cloud.texttospeech.v1beta1 AudioConfig
                                           AudioEncoding
                                           SsmlVoiceGender
                                           SynthesisInput
                                           TextToSpeechClient
                                           VoiceSelectionParams
                                           TextToSpeechSettings
                                           SynthesizeSpeechRequest
                                           SynthesizeSpeechRequest$TimepointType)
    (com.google.auth.oauth2 GoogleCredentials)
    (com.google.api.gax.core FixedCredentialsProvider)))

(defn- words->ssml
  [words]
  (let [with-marks (->> words
                        (map-indexed (fn [i t] (str "<mark name=\"i-" i "\"/>" t)))
                        (str/join " "))]
    (str
      "<speak>"
      with-marks
      "</speak>")))

(defn- timepoints->transcript!
  [timepoints words audio-path]
  (let [result (map-indexed (fn [i t] {:word (get words i)
                                       :start (.getTimeSeconds t)
                                       :end (or
                                              (some-> (get timepoints (inc i)) (.getTimeSeconds))
                                              (+ (.getTimeSeconds t) 0.05))
                                       :conf 1}) timepoints)
        text (str/join " " words)
        filepath audio-path
        output-transcript-path (str (->> (str/split audio-path #"\.") (drop-last) (str/join ".")) "-transcript.json")]
    (log/debug output-transcript-path)
    (with-open [out (io/writer output-transcript-path)]
      (json/write {:result result
                   :text text
                   :filepath audio-path}
                  out))))

(defn generate-voice!
  ([text]
   (generate-voice! text "output.mp3"))
  ([text audio-path]
   (let [words (str/split text #" ")
         json-path (env :google-application-credentials)
         credentials (-> (GoogleCredentials/fromStream (io/input-stream json-path))
                         (.createScoped ["https://www.googleapis.com/auth/cloud-platform"]))
         settings (-> (TextToSpeechSettings/newBuilder)
                      (.setCredentialsProvider  (FixedCredentialsProvider/create credentials))
                      (.build))]
     (with-open [c (TextToSpeechClient/create settings)
                 out (io/output-stream audio-path)]
       (let [ssml (words->ssml words)
             input (-> (SynthesisInput/newBuilder)
                       (.setSsml ssml)
                       (.build))
             voice (-> (VoiceSelectionParams/newBuilder)
                       (.setLanguageCode "en-US")
                       (.setSsmlGender SsmlVoiceGender/NEUTRAL)
                       (.build))
             audio-config (-> (AudioConfig/newBuilder)
                              (.setAudioEncoding AudioEncoding/MP3)
                              (.build))
             request (-> (SynthesizeSpeechRequest/newBuilder)
                         (.setInput input)
                         (.setVoice voice)
                         (.setAudioConfig audio-config)
                         (.addEnableTimePointing SynthesizeSpeechRequest$TimepointType/SSML_MARK)
                         (.build))
             response (.synthesizeSpeech c request)
             timepoints (-> response .getTimepointsList)]
         (.writeTo (-> response .getAudioContent) out)
         (timepoints->transcript! timepoints words audio-path))))))

(comment
  {:result [{:conf 1.0 :end 3.84 :start 3.12 :word "letters"}]
   :text "some text"
   :filepath "/upload/qqq.mp3"}

  (let [audio-path "output.mp3"]
    )
  
  (text->ssml "Hello, World! My name is Ivan.")
  (generate-voice! "Hello, World! My name is Ivan. My name is Ivan. Hello, World! Can we use duplicates?")
  )
