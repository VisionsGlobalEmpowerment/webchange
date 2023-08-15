(ns webchange.interpreter.renderer.scene.components.svg-path
  (:require
    [clojure.string :as s]
    [webchange.renderer.letters-path :refer [alphabet-path
                                             alphabet-traceable-path]]))

(def combining-fns
  {"்" (fn [path {:keys [x width]}]
        (str path " M " (/ (+ 20 x width) 2) " 80 l 0 0.1"))
   "ா" (fn [path {:keys [x width]}]
         (str path " M " (+ 10 x width) " 191 v -82 h 77 m -32 0 v 84"))
   "ி" (fn [path _] path)
   "ூ" (fn [path _] path)
   "ு" (fn [path _] path)
   "ீ" (fn [path _] path)
   "ெ" (fn [path _]
         (let [atoms (s/split path #" ")
               new-x (+ (js/parseInt (second atoms)) 120)]
           (str "M 49 191 c -24 -15 -20 -48 11 -48 c 30 0 32 49 0 49 c -85 -1 -65 -130 1 -130 c 62 0 56 36 56 130"
                " "
                "M " new-x " "(s/join " " (nthrest atoms 2)))))})

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

(defn- get-combining-svg
  [source path]
  (let [[letter combining] path
        letter-svg (move-path-left (get source letter))
        comb-fn (get combining-fns combining)]
    (comb-fn letter-svg (get-bbox letter-svg))))

(defn get-svg-path
  ([path]
   (get-svg-path path {}))
  ([path {:keys [trace?] :or {trace? false}}]
   (let [source (if trace? alphabet-traceable-path alphabet-path)
         with-combining? (fn [[_ x]] (combining? x))]
     (cond
       (contains? source path) (move-path-left (get source path))
       (with-combining? path) (get-combining-svg source path)
       :else path))))

(defn text->svg-letters
  [text {:keys [trace?] :or {trace? false}}]
  (->> text
       (reduce (fn [a l] (if (combining? l)
                           (let [prev (last a)
                                 new-a (->> (drop-last a)
                                            (into []))]
                             (conj new-a (str prev l)))
                           (conj a l)))
               [])
       (mapv #(get-svg-path % {:trace? trace?}))))

(comment
  (text->svg-letters "நாய்க்" {:trace? true})
  (text->svg-letters "னூ" {:trace? true})
  (get-svg-path "க்" {:trace? true})


  (concat [] '("a") ["b"])
  )
