(ns webchange.interpreter.renderer.scene.components.text.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.text.utils :as utils]
    [webchange.interpreter.renderer.scene.components.utils :refer [emit]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]))

(defn wrap
  [type name text-object chunks]
  (create-wrapper {:name          name
                   :type          type
                   :object        text-object
                   :chunks        chunks
                   :set-text      (fn [value]
                                    (aset text-object "text" value)
                                    (emit text-object "textChanged"))
                   :set-highlight (fn [highlight]
                                    (let [highlight-filter-set (f/has-filter-by-name text-object "glow")]
                                      (if (and (not highlight) highlight-filter-set) (f/set-filter text-object "" {}))
                                      (if (and highlight (not highlight-filter-set))
                                        (f/set-filter text-object "glow" {}))))

                   :set-permanent-pulsation (fn [permanent-pulsation]
                                              (let [pulsation-filter-set (f/has-filter-by-name text-object "pulsation")]
                                                (if (and (not permanent-pulsation) pulsation-filter-set) (f/set-filter text-object "" {}))
                                                (if (and permanent-pulsation (not pulsation-filter-set))
                                                  (f/set-filter text-object "pulsation" (assoc permanent-pulsation :no-interval true)))))
                   :set-fill      (fn [value]
                                    (utils/set-fill text-object value))
                   :get-fill      (fn []
                                    (utils/get-fill text-object))
                   :set-font-size (fn [font-size]
                                    (utils/set-font-size text-object font-size))}))
