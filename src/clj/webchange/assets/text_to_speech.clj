(ns webchange.assets.text-to-speech
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [config.core :refer [env]]
    [webchange.assets.core :as core]
    [webchange.course.core :as course]
    [webchange.course.walk :as course-walk])
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
  [sentences]
  (let [with-marks (fn [si sentence]
                     (->> (str/split sentence #" ")
                          (map-indexed (fn [i t] (str "<mark name=\"i-" si "-" i "\"/>" t)))
                          (str/join " ")))
        with-sentences (->> sentences
                            (map-indexed (fn [i s] (str "<s>" (with-marks i s) "</s>")))
                            (str/join ""))]
    (str
      "<speak><p>"
      with-sentences
      "</p></speak>")))

(defn- timepoints->transcript!
  [timepoints words audio-path]
  (let [timepoints (vec timepoints)
        result (map-indexed (fn [i t] {:word (get words i)
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

(def lang->code-map {"en" "en-US"
                     "zh" "cmn-CN"
                     "ta" "ta-IN"})

(def ssmsl-gender {"female" SsmlVoiceGender/FEMALE
                   "male" SsmlVoiceGender/MALE
                   "neutral" SsmlVoiceGender/NEUTRAL})

(defn- generate-voice!
  [{:keys [sentences lang gender]} audio-name]
  (let [path (str (env :upload-dir) (if (.endsWith (env :upload-dir) "/") "" "/") audio-name)
        relative-path (str "/upload/" audio-name)

        text (str/join " " sentences)
        words (str/split text #" ")
        json-path (env :google-application-credentials)
        credentials (-> (GoogleCredentials/fromStream (io/input-stream json-path))
                        (.createScoped ["https://www.googleapis.com/auth/cloud-platform"]))
        settings (-> (TextToSpeechSettings/newBuilder)
                     (.setCredentialsProvider  (FixedCredentialsProvider/create credentials))
                     (.build))
        lang-code (get lang->code-map lang (str lang "-" (str/upper-case lang)))]
    (with-open [c (TextToSpeechClient/create settings)
                out (io/output-stream path)]
      (let [ssml (words->ssml sentences)
            input (-> (SynthesisInput/newBuilder)
                      (.setSsml ssml)
                      (.build))
            voice (-> (VoiceSelectionParams/newBuilder)
                      (.setLanguageCode lang-code)
                      (.setSsmlGender (get ssmsl-gender gender SsmlVoiceGender/NEUTRAL))
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
        (timepoints->transcript! timepoints words path)
        (core/store-asset-hash! path)))
    {:url relative-path
     :type "audio"
     :size 1}))

(defn- ssmls-count
  [text-lines]
  (let [text (str/join " " text-lines)
        words-count (-> (str/split text #" ")
                        (count))
        sentence-length (count "<s></s>")
        sentence-count (count text-lines)
        text-length (count text)
        mark-length (count "<mark name\"i-99-99\"/>")
        speak-length (count "<speak><p></p></speak>")]
    (+ speak-length text-length (* words-count mark-length) (* sentence-count sentence-length))))

(defn- lines->texts
  [lines]
  (loop [texts []
         current-text []
         [head & tail :as lines-left] lines]
    (cond
      (nil? head)
      (conj texts current-text)

      (> (ssmls-count [head]) 5000)
      (recur texts
             current-text
             tail)

      (> (ssmls-count (conj current-text head)) 5000)
      (recur (conj texts current-text)
             []
             lines-left)

      :else
      (recur texts
             (conj current-text head)
             tail))))

(defn generate-voice-for
  [course-slug scene-slug data audio-name]
  (let [scene-data (course/get-scene-latest-version course-slug scene-slug)
        texts (->> (concat (course-walk/text-lines scene-data) (course-walk/dialog-lines scene-data))
                   (map :text)
                   (lines->texts))]
    (map-indexed (fn [i text] (generate-voice! (assoc data :sentences text) (str audio-name "-" i ".mp3"))) texts)))

(comment
  {:result [{:conf 1.0 :end 3.84 :start 3.12 :word "letters"}]
   :text "some text"
   :filepath "/upload/qqq.mp3"}


  (words->ssml ["Hello, World!"
                "My name is Ivan."
                "My name is Ivan."
                "Hello, World!"
                "Can we use duplicates?"])
  (generate-voice! {:sentences ["Hello, World!"
                                "My name is Ivan."
                                "My name is Ivan."
                                "Hello, World!"
                                "Can we use duplicates?"]
                    :lang "en"}
                   "output.mp3")

  (generate-voice! {:sentences ["Hello, World!"
                                "My name is Ivan."
                                "My name is Ivan."
                                "Hello, World!"
                                "Can we use duplicates?"]
                    :lang "en"
                    :gender "female"}
                   "output.mp3")
  
  (generate-voice! {:text "¡Bienvenido a Quick Kick Arena, donde es hora de rimar!"
                    :lang "es"}
                   "output.mp3")
  
  (generate-voice-for "rhyming-english-zswjvnhv" "rhyming-activity" {:lang "es"} "output")
  (generate-voice-for "rhyming-english-zswjvnhv" "rhyming-activity" {:lang "en" :gender "female"} "output")

  )
