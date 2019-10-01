(ns webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-model
  (:require
    ["@projectstorm/react-diagrams" :refer [Toolkit NodeModel DefaultPortModel]]
    [webchange.editor-v2.diagram-builder.colors :refer [get-action-color]]))

;; Custom version of of "DefaultNodeModel" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/models/DefaultNodeModel.ts

(defn uid
  []
  (.UID Toolkit))

(defn init
  [entity {:keys [name] :as props}]
  (set! (.-name entity) name)
  (set! (.-props entity) (clj->js props)))

(defn action-model []
  (js/Reflect.construct NodeModel #js ["action"] action-model))

(set! (.. action-model -prototype)
      (js/Object.create (.-prototype NodeModel)))

(set! (.. action-model -prototype -constructor)
      action-model)

(set! (.. action-model -prototype -addInPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. true (uid) label)]
            (.addPort this port)))))

(set! (.. action-model -prototype -addOutPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. false (uid) label)]
            (.addPort this port)))))

(set! (.. action-model -prototype -getInPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(= (.-in %) true) ports)))))

(set! (.. action-model -prototype -getOutPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(not (.-in %)) ports)))))


(defn get-action-model
  ([]
   (get-action-model {:name "Untitled"
                      :color (get-action-color)}))
  ([props]
   (let [entity (action-model.)]
     (init entity props)
     entity)))

;(set! (.. DiamondPortModel -prototype -serialize)
;      (fn []
;        (this-as this
;          (clj->js (merge
;                     (js->clj (js/Reflect.apply (.. PortModel -prototype -serialize) this #js []))
;                     {:position (js->clj (.-position this))}))
;          )))
