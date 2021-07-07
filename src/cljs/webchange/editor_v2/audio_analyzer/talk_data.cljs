(ns webchange.editor-v2.audio-analyzer.talk-data
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer.config :refer [confidence-chunk-threshold]]
    [webchange.editor-v2.audio-analyzer.utils :refer [levenshtein-distance prepare-text]]
    [webchange.editor-v2.components.audio-wave-form.state :as state]))

(defn get-chunks
  [orig-text rec-text]
  (if (= (count orig-text) (count rec-text))
    rec-text
    (let [orig-items (doall (map (fn [item]
                                   {:text  item
                                    :match (remove nil? (map-indexed (fn [idx rec-item]
                                                                       (let [distance (levenshtein-distance item (:word rec-item))
                                                                             len (count item)
                                                                             conf (/ (- len distance) len)]
                                                                         (if (> conf confidence-chunk-threshold)
                                                                           idx
                                                                           nil)))
                                                                     rec-text))}
                                   ) orig-text))
          orig-items (reduce (fn [result item]
                               (let [used (:used result)
                                     match (:match item)
                                     num (apply min (vec (clojure.set/difference (set match) (set used))))]
                                 (-> result
                                     (update :items conj (-> item
                                                             (assoc :match num)))
                                     (update :used conj num)))
                               ) {:items [] :used []} orig-items)
          processed-items (reduce (fn [result item]
                                    (if (:match item)
                                      (do
                                        (let
                                          [stack-len (count (:stack result))
                                           stacked-items (if (< 0 stack-len)
                                                           (let
                                                             [margins (cond
                                                                        (> 0 (- (:match item) (:last result))) {:end   (:end (get rec-text (:last result)))
                                                                                                                :start (:start (get rec-text (:last result)))}
                                                                        (= 1 (- (:match item) (:last result))) {:end   (:end (get rec-text (:last result)))
                                                                                                                :start (:start (get rec-text (:match item)))}
                                                                        (< 1 (- (:match item) (:last result))) {:end   (:end (get rec-text (inc (:last result))))
                                                                                                                :start (:start (get rec-text (dec (:match item))))})
                                                              duration (- (:end margins) (:start margins))
                                                              item-duration (/ duration stack-len)]
                                                             (vec (doall (map-indexed (fn [idx item]
                                                                                        (-> item
                                                                                            (assoc :start (* (+ 1 idx) (:start margins)))
                                                                                            (assoc :end (+ item-duration (* (+ 1 idx) (:start margins)))))
                                                                                        ) (:stack result)))))
                                                           [])]
                                          (-> result
                                              (update :items vec)
                                              (update :items concat stacked-items)
                                              (update :items vec)
                                              (update :items conj (get rec-text (:match item)))
                                              (assoc :last (:match item))
                                              (update :idx inc)
                                              (assoc :stack []))))
                                      (do
                                        (-> result
                                            (update :idx inc)
                                            (update :stack conj item)))))
                                  {:items [] :stack [] :last 0 :idx 0} (:items orig-items))
          stack-len (count (:stack processed-items))
          end-fragment (:end (last rec-text))
          next-unprocessed (get rec-text (inc (:last processed-items)))
          start-stack (if next-unprocessed (:start next-unprocessed) (:end (last (:items processed-items))))
          time-duration (max 0.5 (- end-fragment start-stack))
          step-duration (if (> stack-len 0) (/ time-duration stack-len) 0)
          processed-items (-> processed-items
                              (update :items concat (map-indexed (fn [idx item]
                                                                   (-> item
                                                                       (assoc :start (+ (* step-duration idx) start-stack))
                                                                       (assoc :end (+ start-stack (* (+ 1 idx) step-duration))))
                                                                   ) (:stack processed-items))))]
      (:items processed-items))))

(defn- pack-talk-data
  [items]
  (map-indexed (fn [_idx item]
                 {:end   (:end item),
                  :start (:start item),
                  :anim  "talk"})
               items))

(defn- pack-chunks
  [items]
  (map-indexed (fn [idx item]
                 {:at       (:start item),
                  :end      (:end item),
                  :chunk    idx,
                  :start    (:start item),
                  :duration (- (:end item) (:start item))})
               items))

(defn- get-talk-data-for-text
  [text data region]
  (let [items (->> data
                   (filter (fn [item]
                             (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))))
                   (vec))
        prepared-text (-> text (prepare-text) (clojure.string/split " "))]
    (-> (get-chunks prepared-text items)
        (pack-talk-data))))

(defn get-chunks-for-text
  [text data region]
  (let [items (->> data
                   (filter (fn [item]
                             (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))))
                   (vec))
        prepared-text (-> text (prepare-text) (clojure.string/split " "))]
    (-> (get-chunks prepared-text items)
        (pack-chunks))))

(defn get-chunks-data-if-possible
  [text url region]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])]
    (get-chunks-for-text text script-data region)))

(defn get-talk-data-if-possible
  [text url region]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])]
    (get-talk-data-for-text text script-data region)))
