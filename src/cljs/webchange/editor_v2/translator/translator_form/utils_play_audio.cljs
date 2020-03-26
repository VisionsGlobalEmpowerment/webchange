(ns webchange.editor-v2.translator.translator-form.utils-play-audio
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.core :refer [load-assets]]))

(defn- audios->sources
  [audios-list]
  (->> audios-list
       (map :src)
       (filter #(not (= % "empty")))))

(defn- sources->assets
  [urls]
  (map (fn [url]
         {:type "audio"
          :size 1
          :url  url})
       urls))

(defn- audio->action
  [{:keys [src start duration]}]
  (if-not (= src "empty")
    {:type     "audio"
     :id       src
     :start    start
     :duration duration}
    {:type     "empty"
     :duration duration}))

(defn- get-audios-sequence-action
  [audios-list callback]
  {:type       "sequence-data"
   :unique-tag :custom-phrase-playing
   :data       (conj (->> audios-list
                          (map audio->action)
                          (vec))
                     {:type     "callback"
                      :callback callback})})

;; ToDo: use in dialog playing

(defn play-audios-list
  ([audios-list]
   (play-audios-list audios-list {}))
  ([audios-list callbacks]
   (let [on-loading (or (:on-loading callbacks) #())
         on-playing (or (:on-playing callbacks) #())
         on-finish (or (:on-finish callbacks) #())
         action (get-audios-sequence-action audios-list on-finish)]
     (load-assets (-> audios-list audios->sources sources->assets)
                  #(on-loading %)
                  #(do (on-playing)
                       (re-frame/dispatch [::ce/execute-action action]))))))
