(ns webchange.interpreter.renderer.scene.components.props-utils
  (:require
    [clojure.set :refer [difference]]
    [clojure.string :refer [capitalize]]
    [webchange.logger :as logger]))

(def skip-check-props [:object-name :parent :children :type])

(defn- check-extra-props
  [entity-id current-props default-props]
  (let [current-names (-> current-props keys set)
        default-names (-> default-props keys (concat skip-check-props) set)
        extra-props-names (difference current-names default-names)]
    (when-not (empty? extra-props-names)
      (logger/warn "There are extra props for" entity-id)
      (logger/debug-folded (str entity-id " extra props") extra-props-names))))

(defn- get-processed-prop
  [prop-name prop-data current-props]
  (let [current-prop-name (if (contains? prop-data :alias)
                            (:alias prop-data)
                            prop-name)
        current-prop-value (get current-props current-prop-name)
        default-prop-value (:default prop-data)
        prop-passed? (contains? current-props current-prop-name)]
    (assoc {} prop-name (if prop-passed?
                          current-prop-value
                          default-prop-value))))

(defn- get-processed-props
  [current-props default-props]
  (reduce (fn [result [prop-name prop-data]]
            (->> (get-processed-prop prop-name prop-data current-props)
                 (merge result)))
          (select-keys current-props skip-check-props)
          default-props))

(defn- get-entity-id
  [component-type current-props]
  (str (capitalize component-type) " <" (:object-name current-props) ">"))

(defn get-props
  [component-type current-props default-props]
  (-> (get-entity-id component-type current-props)
      (check-extra-props current-props default-props))
  (get-processed-props current-props default-props))
