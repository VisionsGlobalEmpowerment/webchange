(ns webchange.interpreter.renderer.scene.components.svg-path
  (:require
    [clojure.string :as s]
    [webchange.renderer.letters-path :refer [letters-path
                                             alphabet-path
                                             alphabet-traceable-path]]))

(defn- right-from
  [comb path {:keys [x width]}]
  (let [atoms (s/split comb #" ")
        new-x (+ 10 x width)]
    (str path " M " new-x " " (s/join " " (nthrest atoms 2)))))

(defn- left-from
  [comb path {:keys [width]}]
  (let [atoms (s/split path #" ")
        new-x (+ (js/parseInt (second atoms)) width)]
    (str comb
         " "
         "M " new-x " " (s/join " " (nthrest atoms 2)))))

(defn- around
  [path {:keys [x width]} left {left-width :width} right]
  (let [atoms (s/split path #" ")
        new-x (+ (js/parseInt (second atoms)) left-width)
        right-atoms (s/split right #" ")
        new-right-x (+ 10 x width left-width)]
    (str left
         " "
         "M " new-x " " (s/join " " (nthrest atoms 2))
         " "
         "M " new-right-x " " (s/join " " (nthrest right-atoms 2)))))

(def combining-fns
  {"்" (fn [path {:keys [x width]}]
        (str path " M " (/ (+ 20 x width) 2) " 80 l 0 0.1"))
   "ா" (fn [path bbox]
         (let [comb "M 0 191 191 v -82 h 77 m -32 0 v 84"]
           (right-from comb path bbox)))
   "ி" (fn [path _] path)
   "ூ" (fn [path _] path)
   "ு" (fn [path _] path)
   "ீ" (fn [path _] path)
   "ெ" (fn [path _]
         (let [comb "M 49 191 c -24 -15 -20 -48 11 -48 c 30 0 32 49 0 49 c -85 -1 -65 -130 1 -130 c 62 0 56 36 56 130"]
           (left-from comb path {:width 120})))
   "ே" (fn [path _]
         (let [comb "M 64 191 c -22 -15 -15 -39 11 -39 c 33 0 33 40 0 40 c -85 0 -86 -130 0 -130 c 33 0 33 41 0 41 c -26 0 -31 -27 -8 -40"]
           (left-from comb path {:width 120})))
   "ை" (fn [path _]
         (let [comb "M 7 161 c 21 -23 47 -11 45 10 c -3 35 -49 28 -45 -10 c 0 -68 111 -68 111 5 c 0 38 -39 38 -39 0 c 1 -55 83 -76 76 -3 c -1 19 -2 22 -5 32"]
           (left-from comb path {:width 120})))
   "ொ" (fn [path bbox]
         (let [left "M 49 191 c -24 -15 -20 -48 11 -48 c 30 0 32 49 0 49 c -85 -1 -65 -130 1 -130 c 62 0 56 36 56 130"
               rigth "M 0 191 191 v -82 h 77 m -32 0 v 84"]
           (around path bbox left {:width 120} rigth)))
   "ோ" (fn [path bbox]
         (let [left "M 64 191 c -22 -15 -15 -39 11 -39 c 33 0 33 40 0 40 c -85 0 -86 -130 0 -130 c 33 0 33 41 0 41 c -26 0 -31 -27 -8 -40"
               rigth "M 0 191 191 v -82 h 77 m -32 0 v 84"]
           (around path bbox left {:width 120} rigth)))
   "ௌ" (fn [path bbox]
         (let [left "M 49 191 c -24 -15 -20 -48 11 -48 c 30 0 32 49 0 49 c -85 -1 -65 -130 1 -130 c 62 0 56 36 56 130"
               rigth "M 7 158 c 31 -21 42 -3 44 15 c 5 29 -51 31 -44 -15 c 9 -72 70 -47 75 -33 v 68 v -81 h 77 m -34 0 v 81"]
           (around path bbox left {:width 120} rigth)))
   })

(defn- get-bbox
  [data]
  (let [svg-node (.createElementNS js/document "http://www.w3.org/2000/svg" "svg")
        path-node (.createElementNS js/document "http://www.w3.org/2000/svg" "path")
        body (.-body js/document)]
    (.setAttribute path-node "d" data)
    (.appendChild svg-node path-node)
    (.appendChild body svg-node)
    (let [bbox {:x (.-x (.getBBox svg-node))
                :y (.-y (.getBBox svg-node))
                :width (.-width (.getBBox svg-node))
                :height (.-height (.getBBox svg-node))}]
      (.removeChild body svg-node)
      bbox)))

(defn move-path-left [data]
  (let [atoms (s/split data #" ")
        {x :x} (get-bbox data)
        new-x (- (js/parseInt (second atoms)) x)
        offset 10
        path-rest (s/join " " (nthrest atoms 2))
        new-data (str "M " (+ offset (Math/round new-x)) " " path-rest)]
    new-data))

(defn- combining?
  [x]
  (let [letters (->> (keys combining-fns) (into #{}))]
    (some letters x)))

(defn- get-letter-svg
  [letter]
  (let [shape (move-path-left (get-in letters-path [letter :shape]))
        trace (move-path-left (get-in letters-path [letter :trace]))]
    {:shape shape
     :trace trace
     :bbox (get-bbox shape)}))

(defn- get-combining-svg
  [path]
  (let [[letter combining] path
        letter-svg (get-letter-svg letter)
        comb-fn (get combining-fns combining)
        shape (comb-fn (:shape letter-svg) (:bbox letter-svg))
        trace (comb-fn (:trace letter-svg) (:bbox letter-svg))]
    {:shape shape
     :trace trace
     :bbox (get-bbox shape)}))

(defn get-svg-path
  ([path]
   (get-svg-path path {}))
  ([path {:keys [trace?] :or {trace? false}}]
   (let [source (if trace? alphabet-traceable-path alphabet-path)
         with-combining? (fn [[_ x]] (combining? x))]
     (cond
       (contains? source path) (move-path-left (get source path))
       (with-combining? path) (if trace?
                                (-> (get-combining-svg path) :trace)
                                (-> (get-combining-svg path) :shape))
       :else path))))

(defn- letter->svg
  [letter]
  (let [with-combining? (fn [[_ x]] (combining? x))]
    (cond
      (contains? letters-path letter) (get-letter-svg letter)
      (with-combining? letter) (get-combining-svg letter)
      :else {:shape ""
             :trace ""})))

(defn text->svg-letters
  [text]
  (->> text
       (reduce (fn [a l] (if (combining? l)
                           (let [prev (last a)
                                 new-a (->> (drop-last a)
                                            (into []))]
                             (conj new-a (str prev l)))
                           (conj a l)))
               [])
       (remove s/blank?)
       (mapv letter->svg)))

(comment
  (text->svg-letters "நாய்க்")
  (text->svg-letters "னூ")
  (get-svg-path "க்" {:trace? true})


  (concat [] '("a") ["b"])
  )
