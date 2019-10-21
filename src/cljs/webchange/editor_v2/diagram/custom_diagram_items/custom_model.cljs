(ns webchange.editor-v2.diagram.custom-diagram-items.custom-model
  (:require
    ["@projectstorm/react-diagrams" :refer [Toolkit NodeModel DefaultPortModel]]))

;; Custom version of of "DefaultNodeModel" from
;; https://github.com/projectstorm/react-diagrams/blob/v5.3.2/src/defaults/models/DefaultNodeModel.ts

(defn uid
  []
  (.UID Toolkit))

(defn init
  [entity {:keys [name] :as props}]
  (set! (.-name entity) name)
  (set! (.-props entity) (clj->js props)))

(defn custom-model []
  (js/Reflect.construct NodeModel #js ["custom"] custom-model))

(set! (.. custom-model -prototype)
      (js/Object.create (.-prototype NodeModel)))

(set! (.. custom-model -prototype -constructor)
      custom-model)

(set! (.. custom-model -prototype -addInPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. true (uid) label)]
            (.addPort this port)))))

(set! (.. custom-model -prototype -addOutPort)
      (fn [label]
        (this-as this
          (let [port (DefaultPortModel. false (uid) label)]
            (.addPort this port)))))

(set! (.. custom-model -prototype -getInPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(= (.-in %) true) ports)))))

(set! (.. custom-model -prototype -getOutPorts)
      (fn []
        (this-as this
          (let [ports (->> this .-ports js->clj vals)]
            (filter #(not (.-in %)) ports)))))

(set! (.. custom-model -prototype -serialize)
      (fn []
        (this-as this
          (let [parent-result (js->clj (js/Reflect.apply (.. NodeModel -prototype -serialize) this #js []))
                size {:width (.-width this)
                      :height (.-height this)}]
            (clj->js (merge parent-result size))))))

(defn get-custom-model
  ([]
   (get-custom-model {:name "Untitled"}))
  ([props]
   (let [entity (custom-model.)]
     (init entity props)
     entity)))
