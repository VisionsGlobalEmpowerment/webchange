(ns webchange.interpreter.renderer.scene.components.props-utils
  (:require
    [clojure.set :refer [difference]]
    [clojure.string :refer [capitalize]]
    [webchange.logger.index :as logger]))

(def skip-check-props [:object-name :group-name :parent :children :type :editable? :visible])

(defn- check-extra-props
  [entity-id current-props default-props exclude-prop-names]
  (let [current-names (-> current-props keys set)
        default-names (->> default-props
                           (reduce (fn [result [prop-name prop-data]]
                                     (conj result (if (contains? prop-data :alias)
                                                    (:alias prop-data)
                                                    prop-name)))
                                   [])
                           (concat skip-check-props exclude-prop-names)
                           (set))
        extra-props-names (difference current-names default-names)]
    (when-not (empty? extra-props-names)
      (logger/warn "There are extra props for" entity-id)
      (logger/trace-folded (str entity-id " extra props") extra-props-names))))

(defn- get-processed-prop
  [prop-name prop-data current-props]
  (let [current-prop-name (if (contains? prop-data :alias)
                            (:alias prop-data)
                            prop-name)
        current-prop-value (get current-props current-prop-name)
        has-default? (contains? prop-data :default)
        default-prop-value (:default prop-data)
        prop-passed? (and
                       (contains? current-props current-prop-name)
                       (not (and (:not-nil prop-data) (nil? current-prop-value))))]
    (if (or prop-passed? has-default?)
      (assoc {} prop-name (if prop-passed?
                            current-prop-value
                            default-prop-value))
      {})))

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
  [component-type current-props default-props {:keys [exclude-check]}]
  (-> (get-entity-id component-type current-props)
      (check-extra-props current-props default-props exclude-check))
  (get-processed-props current-props default-props))

(defn get-object-props
  [current-props default-props]
  (get-processed-props current-props default-props))
