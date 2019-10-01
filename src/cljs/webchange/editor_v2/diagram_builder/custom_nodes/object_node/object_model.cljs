(ns webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-model
  (:require
    ["@projectstorm/react-diagrams" :refer [Toolkit NodeModel DefaultPortModel]]
    [webchange.editor-v2.diagram-builder.colors :refer [get-object-color]]))

;; Custom version of of "DefaultNodeModel" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/models/DefaultNodeModel.ts

(defn uid
  []
  (.UID Toolkit))

(defn init
  [entity {:keys [name color]}]
  (set! (.-name entity) name)
  (set! (.-color entity) color))

(defn object-model []
  (js/Reflect.construct NodeModel #js ["object"] object-model))

(set! (.. object-model -prototype)
      (js/Object.create (.-prototype NodeModel)))

(set! (.. object-model -prototype -constructor)
      object-model)

(set! (.. object-model -prototype -addInPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. true (uid) label)]
            (.addPort this port)))))

(set! (.. object-model -prototype -addOutPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. false (uid) label)]
            (.addPort this port)))))

(set! (.. object-model -prototype -getInPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(= (.-in %) true) ports)))))

(set! (.. object-model -prototype -getOutPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(not (.-in %)) ports)))))


(defn get-object-model
  ([]
    (get-object-model {:name "Untitled"
                       :color (get-object-color)}))
  ([props]
   (let [entity (object-model.)]
     (init entity props)
     entity)))
