(ns webchange.common.svg-path
  (:require
    [react-konva :refer [Path]]))

(defn svg-path
  [props]
  [:> Path props])
