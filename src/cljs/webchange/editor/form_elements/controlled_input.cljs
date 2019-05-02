(ns webchange.editor.form-elements.controlled-input
  (:require
    [reagent.core :as r]
    [soda-ash.core :refer [FormInput] :rename {FormInput form-input}]))

(defn get-value-from-props
  [props]
  (or (:value props) ""))

(defn get-value-from-event
  [event]
  (-> event .-target .-value))

(defn update-state-with-props
  [state props]
  (let [new-value (get-value-from-props props)
        last-value (:last-value @state)]
    (when-not (= new-value last-value)
      (swap! state assoc :value new-value)
      (swap! state assoc :last-value new-value))))

(defn update-state-with-value
  [state value]
  (swap! state assoc :value value)
  value)

(defn controlled-input-core
  [props {parse :parser
          state :app-state}]
  (r/with-let [on-change (:on-change props)]
              (update-state-with-props state props)
              [form-input (merge props
                                 {:value     (:value @state)
                                  :on-change #(some->> (get-value-from-event %)
                                                       (update-state-with-value state)
                                                       (parse)
                                                       (on-change))})]))

(defn controlled-input
  [props params]
  [controlled-input-core props (merge params
                                      {:app-state (r/atom {:value      ""
                                                           :last-value ""})})])