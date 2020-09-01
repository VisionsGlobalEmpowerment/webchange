(ns webchange.common.text
  (:require
    [reagent.core :as r]
    [react-konva :refer [Shape Group Text Rect]]
    [konva :as k]
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]
    ))

(defonce dummy-context (-> (k/Util.createCanvasElement) (.getContext "2d")))

(defn within-context [fn]
  (.save dummy-context)
  (let [result (fn dummy-context)]
    (.restore dummy-context)
    result))

(defn context-font [{:keys [font-style font-size font-family]}]
  (str font-style " " font-size "px " font-family))

(defn measure-width [text props]
  (within-context
    (fn [context]
      (set! (.-font context) (context-font props))
      (-> context (.measureText text) .-width))))

(defn map-chunk-index [chunks]
  (map-indexed (fn [idx chunk] (assoc chunk :index idx)) chunks))

(defn fill-absent-chunks [chunks]
  (reduce
    (fn [acc chunk]
      (let [end (-> acc last :end (or 0))
            start (:start chunk)]
        (if (> start end)
          (-> acc (conj {:start end :end start}) (conj chunk))
          (conj acc chunk))))
    [] chunks))

(defn prepare-chunk [chunk props]
  (let [text (subs (:text props) (:start chunk) (:end chunk))
        width (measure-width text props)
        height (:font-size props)]
    (-> props
        (dissoc :x :y)
        (merge {:text text :width width :height height} chunk))))

(defn get-chunks [{:keys [chunks] :as props}]
  (->> chunks
       map-chunk-index
       fill-absent-chunks
       (map (fn [chunk] (prepare-chunk chunk props)))))

(defn empty-chunk? [chunk]
  (-> chunk :text clojure.string/trim empty?))

(defn trim-left-chunks [chunks]
  (let [left (first chunks)]
    (if (empty-chunk? left)
      (rest chunks)
      chunks)))

(defn trim-right-chunks [chunks]
  (let [right (last chunks)]
    (if (empty-chunk? right)
      (butlast chunks)
      chunks)))

(defn trim-chunks [chunks]
  (-> chunks trim-left-chunks trim-right-chunks))

(defn line-width [chunks]
  (->> chunks
       (map :width)
       (reduce + 0)))

(defn get-words [chunks]
  (loop [current []
         next (first chunks)
         tail (rest chunks)
         words []]
    (cond
      (nil? next) (conj words current)
      (and (empty-chunk? next) (empty? current)) (recur [next] (first tail) (rest tail) words)
      (empty-chunk? next) (recur [next] (first tail) (rest tail) (conj words current))
      :default (recur (conj current next) (first tail) (rest tail) words))))


(defn new-line [chunks rest]
  (let [trimmed (trim-chunks chunks)
        width (line-width trimmed)]
    {:line-chunks trimmed
     :width width
     :rest rest}))

(defn line [width words]
  (loop [words-to-process words
         current-width 0
         line-chunks []]
    (let [word (first words-to-process)
          tail (rest words-to-process)
          new-width (+ current-width (line-width word))]
      (cond
        (nil? word) (new-line line-chunks words-to-process)
        (< width new-width) (new-line line-chunks words-to-process)
        :default (recur tail new-width (concat line-chunks word))))))

(defn get-lines [{:keys [width] :as props}]
  (loop [words (-> props get-chunks get-words)
         lines []
         iter 0]
    (let [next-line (line width words)
          rest (:rest next-line)
          new-lines (conj lines (dissoc next-line :rest))]
      (if (empty? rest)
        new-lines
        (recur rest new-lines (inc iter))))))

(defn base-y [lines {:keys [height vertical-align font-size]}]
  (let [text-height (* (count lines) font-size)]
    (case vertical-align
      "middle" (/ (- height text-height) 2)
      "bottom" (- height text-height)
      0)))

(defn base-x [line {:keys [width align]}]
  (case align
    "center" (/ (- width (:width line)) 2)
    "right" (- width (:width line))
    0))

(defn chunk-transition-name [name index]
  (if index (str "chunk-" name "-" index)))

(defn chunked-text [scene-id name props]
  (let [defaults {:font-style "normal" :font-size 12 :font-family "Liberation Sans"}
        options (merge defaults props)
        lines (get-lines options)]
    [:> Group {}
     (let [current-y (atom (base-y lines options))]
       (for [line lines]
         ^{:key (str @current-y)}
         [:> Group {:x 0 :y @current-y}
          (let [current-x (atom (base-x line options))]
            (swap! current-y + (:font-size options))
            (for [chunk (:line-chunks line)]
              (let [x @current-x]
                (swap! current-x + (:width chunk))
                ^{:key (str x)}
                [:> Text (-> chunk
                             (merge {:x x :y 0 :transition (chunk-transition-name name (:index chunk))})
                             with-transition)])))]))]))