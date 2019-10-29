(ns webchange.editor-v2.diagram.custom-diagram-items.custom-factory
  (:require
    ["@projectstorm/react-diagrams" :refer [AbstractNodeFactory]]
    [reagent.core :as r]
    [webchange.editor-v2.diagram.modes.widget-event-handlers :refer [get-widget-event-handlers]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-model :refer [get-custom-model]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-widget.widget :refer [custom-widget]]))

(defn init
  [entity diagram-mode]
  (set! (.-mode entity) diagram-mode))

(defn custom-factory []
  (js/Reflect.construct AbstractNodeFactory #js ["custom"] custom-factory))

(set! (.. custom-factory -prototype)
      (js/Object.create (.-prototype AbstractNodeFactory)))

(set! (.. custom-factory -prototype -constructor)
      custom-factory)

(set! (.. custom-factory -prototype -generateReactWidget)
      (fn [_ node]
        (this-as this
          (r/as-element [custom-widget (merge (get-widget-event-handlers (.-mode this))
                                              {:node node}) ]))))

(set! (.. custom-factory -prototype -getNewInstance)
      (fn []
        (get-custom-model)))

(defn get-custom-factory
  [diagram-mode]
  (let [entity (custom-factory.)]
    (init entity diagram-mode)
    entity))
