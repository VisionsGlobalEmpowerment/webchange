(ns webchange.editor.form-elements.controlled-input
  (:require
    [reagent.core :as r]
    [soda-ash.core :refer [FormInput]]))

(defn- get-value-from-props
  [props]
  (or (:value props) ""))

(defn- get-value-from-input
  [input]
  (.-value input))

(defn- update-state-with-props
  [state props]
  (let [new-value (get-value-from-props props)
        last-value (:last-value @state)]
    (when-not (= new-value last-value)
      (swap! state assoc :value new-value)
      (swap! state assoc :last-value new-value))))

(defn- update-state-with-value
  [state value]
  (swap! state assoc :value value)
  value)

(defn ControlledInput
  [{parse :parser}]
  (fn [props]
    (r/with-let [value (get-value-from-props props)
                 local-props (r/atom {:value      value
                                      :last-value value})
                 on-change (:on-change props)]
                (update-state-with-props local-props props)
                [FormInput (merge props
                                  {:value     (:value @local-props)
                                   :on-change #(some->> (get-value-from-input %2)
                                                        (update-state-with-value local-props)
                                                        (parse)
                                                        (on-change))})])))
