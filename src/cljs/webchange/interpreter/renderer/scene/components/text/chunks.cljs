(ns webchange.interpreter.renderer.scene.components.text.chunks
  (:require
    [webchange.interpreter.pixi :refer [TextMetrics TextStyle]]))

(defn- measure-width
  [text {:keys [font-weight font-size font-family]}]
  (let [style (TextStyle. (clj->js {:fontSize   font-size
                                    :fontFamily font-family
                                    :fontWeight font-weight}))
        metrics (.measureText TextMetrics text style)]
    (.-width metrics)))

(defn- map-chunk-index [chunks]
  (map-indexed (fn [idx chunk] (assoc chunk :index idx)) chunks))

(defn- split-chunk
  [{:keys [start text]}]
  (loop [rest-text text
         result []]
    (if-not (empty? rest-text)
      (let [last-end (if (empty? result) start (-> result last :end))
            new-line-index (clojure.string/index-of rest-text "\n")]
        (if new-line-index
          (let [first-part (subs rest-text 0 (inc new-line-index))
                rest-part (subs rest-text (inc new-line-index))]
            (recur rest-part (conj result {:start last-end
                                           :end   (+ last-end (count first-part))
                                           :text  first-part})))
          (recur "" (conj result {:start last-end
                                  :end   (+ last-end (count rest-text))
                                  :text  rest-text}))))
      result)))

(defn fill-absent-chunks
  [{:keys [text]} chunks]
  (reduce
    (fn [acc chunk]
      (let [end (-> acc last :end (or 0))
            start (:start chunk)]
        (if (> start end)
          (concat acc
                  (->> {:start end
                        :end   start
                        :text  (subs text end start)}
                       (split-chunk)
                       (map #(dissoc % :text)))
                  [chunk])
          (conj acc chunk))))
    []
    chunks))

(defn- set-chunk-text
  [{:keys [start end] :as chunk} {:keys [text]}]
  (assoc chunk :text (subs text start end)))

(defn- measure-chunk
  [{:keys [text] :as chunk} props]
  (let [width (measure-width text props)
        height (:font-size props)]
    (-> props
        (dissoc :x :y)
        (merge {:width  width
                :height height}
               chunk))))

(defn- get-chunks
  [{:keys [chunks] :as props}]
  (->> chunks
       map-chunk-index
       (fill-absent-chunks props)
       (map (fn [chunk] (-> chunk
                            (set-chunk-text props)
                            (measure-chunk props))))))

(defn- empty-chunk? [chunk]
  (if (get chunk :text)
    (-> chunk :text clojure.string/trim empty?)
    true))

(defn- trim-left-chunks [chunks]
  (let [left (first chunks)]
    (if (empty-chunk? left)
      (rest chunks)
      chunks)))

(defn- trim-right-chunks [chunks]
  (let [right (last chunks)]
    (if (empty-chunk? right)
      (butlast chunks)
      chunks)))

(defn- trim-chunks [chunks]
  (-> chunks trim-left-chunks trim-right-chunks))

(defn- line-width [chunks]
  (->> chunks
       (map :width)
       (reduce + 0)))

(defn- ends-with-new-line?
  [chunk]
  (let [last-symbol (-> chunk :text last)]
    (or
      (identical? \newline last-symbol)
      (identical? \return last-symbol))))

(defn- get-words [chunks]
  (loop [current []
         next (first chunks)
         tail (rest chunks)
         words []]
    (cond
      (nil? next) (conj words current)
      (ends-with-new-line? next) (recur [] (first tail) (rest tail) (conj words (conj current next)))
      (and (empty-chunk? next) (empty? current)) (recur [next] (first tail) (rest tail) words)
      (empty-chunk? next) (recur [next] (first tail) (rest tail) (conj words current))
      :default (recur (conj current next) (first tail) (rest tail) words))))

(defn- new-line [chunks rest]
  (let [trimmed (trim-chunks chunks)
        width (line-width trimmed)]
    {:line-chunks trimmed
     :width       width
     :rest        rest}))

(defn- line [width words]
  (loop [words-to-process words
         current-width 0
         line-chunks []]
    (let [word (first words-to-process)
          tail (rest words-to-process)
          new-width (+ current-width (line-width word))]
      (cond
        (nil? word) (new-line line-chunks words-to-process)
        (and (seq line-chunks) (< width new-width)) (new-line line-chunks words-to-process)
        (-> line-chunks last ends-with-new-line?) (new-line line-chunks words-to-process)
        :default (recur tail new-width (concat line-chunks word))))))

(defn- get-lines [{:keys [width] :as props}]
  (loop [words (-> props get-chunks get-words)
         lines []]
    (let [next-line (line width words)
          rest (:rest next-line)
          new-lines (conj lines (dissoc next-line :rest))]
      (if (empty? rest)
        new-lines
        (recur rest new-lines)))))

(defn- base-y [lines {:keys [height vertical-align font-size]}]
  (let [text-height (* (count lines) font-size)]
    (case vertical-align
      "middle" (/ (- height text-height) 2)
      "bottom" (- height text-height)
      0)))

(defn- base-x [line {:keys [width align]}]
  (case align
    "center" (/ (- width (:width line)) 2)
    "right" (- width (:width line))
    0))

(defn chunk-transition-name
  [name index]
  (if index
    ; chunk name corresponds to chunk definition path in scene data: {chunked-text-name}.chunks.{index}
    (str name ".chunks." index)))

(defn chunk-animated-variable [name]
  (str "chunk-" name "-animated"))

(defn lines-with-y
  [props]
  (let [lines (get-lines props)
        base-y (base-y lines props)]
    (->> lines
         (map-indexed (fn [idx line] (assoc line :y (+ base-y (* idx (:font-size props)))))))))

(defn chunks-with-x
  [line props]
  (reduce (fn [result chunk]
            (let [x (:x result)]
              {:x      (+ x (:width chunk))
               :chunks (conj (:chunks result) (assoc chunk :x x))}))
          {:x      (base-x line props)
           :chunks []}
          (:line-chunks line)))
