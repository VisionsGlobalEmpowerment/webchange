(ns webchange.editor.form-elements.integer
  (:require
    [reagent.core :as r]
    [soda-ash.core :refer [FormInput]]))

(defn number-string?
  [value]
  (->> value
       (re-matches #"^[-+]?[0-9]+$")
       (boolean)))

(defn get-value-from-props
  [props]
  (or (:value props) ""))

(defn on-change-handler
  [value-string state callback]
  (swap! state assoc :value value-string)
  (when (number-string? value-string)
    (callback (js/parseInt value-string))))

(defn update-state-with-props
  [state props]
  (let [new-value (get-value-from-props props)
        last-value (:last-value @state)]
    (when-not (= new-value last-value)
      (swap! state assoc :value new-value)
      (swap! state assoc :last-value new-value))))

(defn IntegerInput
  [props]
  (r/with-let [value (get-value-from-props props)
               local-props (r/atom {:value      value
                                    :last-value value})
               on-change (:on-change props)]
              (update-state-with-props local-props props)
              [FormInput (merge props
                                {:value     (:value @local-props)
                                 :on-change #(on-change-handler (-> %2 .-value) local-props on-change)}
                                )]))
