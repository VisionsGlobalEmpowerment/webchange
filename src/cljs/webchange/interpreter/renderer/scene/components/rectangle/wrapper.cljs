(ns webchange.interpreter.renderer.scene.components.rectangle.wrapper
  (:require
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object sprite props]
  (create-wrapper {:name name
                   :type type
                   :set-fill #(aset sprite "tint" %)
                   :set-border-color #(do
                                       (f/set-filter sprite "" {})
                                       (f/set-filter sprite "outline" {:color % :width (:border-width props)
                                                                       :quality 1}))
                   :set-on-click-handler #(utils/set-handler object "click" %)
                   :object object}))
