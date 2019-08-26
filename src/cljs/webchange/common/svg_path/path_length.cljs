(ns webchange.common.svg-path.path-length)

(defn path-length
  "Get SVG path length in local coordinates."
  [path]
  (let [path-element (.createElementNS js/document "http://www.w3.org/2000/svg" "path")]
    (.setAttribute path-element "d" path)
    (.getTotalLength path-element)))


