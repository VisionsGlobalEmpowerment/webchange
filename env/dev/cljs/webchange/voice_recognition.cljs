(ns webchange.voice-recognition
  (:require [webchange.vr-test-data :refer [stored-results]]
            [webchange.utils.audio-analyzer.region-data :as r]))

(defn- get-script-region-text
  [script region]
  (->> script
       (filter #(>= (:start %) (:start region)))
       (filter #(<= (:end %) (:end region)))
       (map :word)
       (clojure.string/join " ")))

(comment
  (let [text "Good job!"
        script (-> stored-results first second)
        region (r/get-region-data-if-possible {:text text :script script})]
    (get-script-region-text script (:region-data region)))

  (let [text "Good job! You helped our friend be polite by saying thank you. You also helped them tell their friend they were having fun, which will probably make him want to keep playing with them longer."
        script (-> stored-results first second)
        region     (r/get-start-end-for-text text script)]
    (get-script-region-text script region))

  (let [text "Good job! You helped our friend be polite by saying thank you. You also helped them tell their friend they were having fun, which will probably make him want to keep playing with them longer."
        script (-> webchange.vr-test-data.stored-results first second)
        result (r/get-start-end-options-for-text text script)]
    (map #(get-script-region-text script %) result)))
