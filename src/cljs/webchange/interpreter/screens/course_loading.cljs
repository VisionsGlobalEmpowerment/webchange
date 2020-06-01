(ns webchange.interpreter.screens.course-loading
  (:require
    [react-konva :refer [Group]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]))

(defn course-loading-screen []
  [:> Group
   [kimage {:src (get-data-as-url "/raw/img/bg.jpg")}]
   [:> Group {:x 628 :y 294}
    [kimage {:src "/raw/img/ui/logo.png"}]]])
