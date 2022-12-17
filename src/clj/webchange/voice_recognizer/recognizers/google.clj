(ns webchange.voice-recognizer.recognizers.google
  (:require
    [clojure.java.io :as io]
    [config.core :refer [env]])
  (:import
    (com.google.cloud.speech.v1 SpeechClient
                                SpeechSettings
                                RecognitionConfig
                                RecognitionConfig$AudioEncoding
                                StreamingRecognitionConfig
                                StreamingRecognizeRequest)
    (com.google.auth.oauth2 GoogleCredentials)
    (com.google.common.util.concurrent SettableFuture)
    (com.google.api.gax.core FixedCredentialsProvider)
    (com.google.api.gax.rpc ApiStreamObserver)
    (com.google.protobuf ByteString)
    (java.util ArrayList)))

(def settings (atom nil))

(defn init-credentials!
  []
  (let [json-path (env :google-application-credentials)
        credentials (-> (GoogleCredentials/fromStream (io/input-stream json-path))
                        (.createScoped ["https://www.googleapis.com/auth/cloud-platform"]))
        cred-provider (FixedCredentialsProvider/create credentials)
        speech-settings (-> (SpeechSettings/newBuilder)
                            (.setCredentialsProvider cred-provider)
                            (.build))]
    (reset! settings speech-settings)))

(defn- duration->seconds
  [duration]
  (let [seconds (.getSeconds duration)
        nanos (.getNanos duration)]
    (-> nanos
        (/ 1000000000)
        (+ seconds)
        (bigdec))))

(defn- process-response-word
  [word-info]
  {:start (duration->seconds (.getStartTime word-info))
   :end (duration->seconds (.getEndTime word-info))
   :word (.getWord word-info)})

(defn- process-response-alternative
  [alternative]
  {:text (.getTranscript alternative)
   :result (->> alternative .getWordsList (map process-response-word))})

(defn- model->language-code
  [model]
  (case model
    "english" "en-US"
    "spanish" "es-ES"
    "tamil" "ta-IN"))

(defn recognize
  [input model]
  (with-open [speech (SpeechClient/create @settings)
              out (java.io.ByteArrayOutputStream.)]
    (io/copy input out)
    (let [rec-config (-> (RecognitionConfig/newBuilder)
                         (.setEncoding RecognitionConfig$AudioEncoding/LINEAR16)
                         (.setLanguageCode (model->language-code model))
                         (.setSampleRateHertz 16000)
                         (.setModel "default")
                         (.setEnableWordTimeOffsets true)
                         (.build))
          config (-> (StreamingRecognitionConfig/newBuilder)
                     (.setConfig rec-config)
                     (.build))
          future-state (SettableFuture/create)
          response-observer (let [messages (ArrayList.)]
                              (proxy [ApiStreamObserver] []
                                (onNext [message] (.add messages message))
                                (onError [t] (.setException future-state t))
                                (onCompleted [] (.set future-state messages))))
          callable (.streamingRecognizeCallable speech)
          request-observer (.bidiStreamingCall callable response-observer)]
      (.onNext request-observer
               (-> (StreamingRecognizeRequest/newBuilder)
                   (.setStreamingConfig config)
                   (.build)))
      (.onNext request-observer
               (-> (StreamingRecognizeRequest/newBuilder)
                   (.setAudioContent (ByteString/copyFrom (.toByteArray out)))
                   (.build)))
      (.onCompleted request-observer)
      (let [responses (-> future-state .get)]
        (->> responses 
             (map (fn [response]
                    (let [result (-> response .getResultsList (.get 0))
                          alternatives (->> result
                                            .getAlternativesList
                                            (map process-response-alternative))]
                      (merge {:total-billed-time (-> response .getTotalBilledTime .getSeconds)
                              :alternatives alternatives}
                             (first alternatives)))))
             (reduce (fn [result response]
                       {:total-billed-time (max (:total-billed-time result) (:total-billed-time response))
                        :text (str (:text result) " " (:text response))
                        :result (concat (:result result) (:result response))
                        :alternatives (concat (:alternatives result) (:alternatives response))})))))))

(comment
  (init-credentials!)

  (with-open [input (io/input-stream "/home/ikhaldeev/test/test-file-small.wav")]
    (recognize input "english"))

  (with-open [input (io/input-stream "/home/ikhaldeev/test/test-file-big.wav")]
    (recognize input "english"))

  (io/input-stream "~/tmp.org")
  )
