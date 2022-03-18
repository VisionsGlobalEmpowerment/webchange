(ns webchange.interpreter.renderer.scene.components.svg_path
  (:require
   [webchange.renderer.letters-path :refer [alphabet-path alphabet-traceable-path]]))

(defn move-path-left [data]
  (let [svg-node (.createElementNS js/document "http://www.w3.org/2000/svg" "svg")
        path-node (.createElementNS js/document "http://www.w3.org/2000/svg" "path")
        body (.-body js/document)
        atoms (.split data " ")]
    (.setAttribute path-node "d" data)
    (.appendChild svg-node path-node)
    (.appendChild body svg-node)
    (let [new-x (- (js/parseInt (second atoms))
                   (.-x (.getBBox svg-node)))
          offset 10
          path-rest (clojure.string/join " " (nthrest atoms 2))
          new-data (str "M " (+ offset (Math/round new-x)) " " path-rest)]
      (.removeChild body svg-node)
      new-data)))

(defn get-svg-path
  ([path]
   (get-svg-path path {}))
  ([path {:keys [trace?] :or {trace? false}}]
   (let [source (if trace? alphabet-traceable-path alphabet-path)]
     (if (contains? source path)
       (move-path-left (get source path))
       path))))
