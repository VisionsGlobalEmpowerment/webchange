(ns webchange.interpreter.renderer.common-wrapper
  (:require
    [webchange.logger :as logger]))

(defn- check-name-prop
  [wrapper]
  (when (-> wrapper :name nil?)
    (logger/warn "Wrapped object :name is not defined"))
  wrapper)

(defn- check-type-prop
  [wrapper]
  (when (-> wrapper :type nil?)
    (logger/warn "Wrapped object :type is not defined"))
  wrapper)

(defn- add-call-method
  [wrapper]
  (assoc wrapper
    :call
    (fn [method-name & params]
      (let [method (get wrapper method-name)]
        (if-not (nil? method)
          (apply method params)
          (logger/warn (str "Method " method-name " is not implemented for " (:type wrapper))))))))

(defn create-wrapper
  [wrapper-object]
  (-> wrapper-object
      (check-name-prop)
      (check-type-prop)
      (add-call-method)))
