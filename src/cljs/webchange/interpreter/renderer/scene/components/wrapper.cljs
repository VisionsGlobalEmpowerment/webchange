(ns webchange.interpreter.renderer.scene.components.wrapper
  (:require
    [clojure.set :refer [difference]]
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

(defn- check-object-prop
  [wrapper]
  (when (-> wrapper :object nil?)
    (logger/warn "Wrapped object :object is not defined"))
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

(defn- check-interface
  [wrapper]
  (let [methods-with-interface (->> 'webchange.interpreter.renderer.scene.components.wrapper-interface
                                    (ns-publics)
                                    (keys)
                                    (map keyword)
                                    (set))
        wrapper-object-methods (->> wrapper
                                    (filter (fn [[_ value]]
                                              (fn? value)))
                                    (map first)
                                    (set))
        methods-without-interface (difference wrapper-object-methods methods-with-interface)]
    (when-not (empty? methods-without-interface)
      (logger/warn "Wrapped methods don't have interface:" (clj->js methods-without-interface)))
    wrapper))

(defn create-wrapper
  [wrapper-object]
  (-> wrapper-object
      (check-name-prop)
      (check-type-prop)
      (check-object-prop)
      (check-interface)
      (add-call-method)))
