(ns webchange.interpreter.components-store
  (:require
    [webchange.interpreter.components.text :as text]))

;; ToDo: package into protocols
(defn get-component-api
  [type]
  (case type
    :text {:create text/create
           :set-attr text/set-attr
           :destroy text/destroy}))

(def components (atom {}))

(defn add-component
  [name type node]
  (let [component {:name name
                   :type type
                   :node node}]
    (swap! components assoc (keyword name) component)
    component))

(defn get-component
  [name]
  (get @components (keyword name)))

(defn create-component
  [name params]
  (let [type (->> params :type keyword)
        api (get-component-api type)
        node ((:create api) params)]
    (add-component name type node)))

(defn update-component
  [component params]
  (let [type (:type component)
        api (get-component-api type)]
    ((:set-attr api) (:node component) params)))

(defn remove-component
  [name]
  (let [component (get-component name)
        type (:type component)
        api (get-component-api type)]
    (swap! components dissoc (keyword name))
    ((:destroy api) (:node component))))
