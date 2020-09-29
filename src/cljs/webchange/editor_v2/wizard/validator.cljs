(ns webchange.editor-v2.wizard.validator
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:wrapper {:position "relative"
             :width    0
             :height   0}
   :message {:position "absolute"
             :left     "20px"
             :width    "300px"
             :color    (get-in-theme [:palette :secondary :main])}})

(defn- validate
  [data validation-data validation-map]
  (doseq [[field validators] validation-map]
    (let [value (get @data field)
          result (->> validators
                      (map (fn [validator] (validator value)))
                      (remove empty?))]
      (if-not (empty? result)
        (swap! validation-data assoc field (->> result (map #(str "* " %)) (clojure.string/join "; ")))
        (swap! validation-data dissoc field)))))

(defn- valid?
  [data validation-data validation-map]
  (validate data validation-data validation-map)
  (empty? @validation-data))

(defn- get-error
  [validation-data field-name]
  (get @validation-data field-name))

(defn- has-error?
  [validation-data field-name]
  (some? (get-error validation-data field-name)))

(defn- error-message
  [{:keys [field-name validation-data]}]
  (let [styles (get-styles)]
    (when (has-error? validation-data field-name)
      [:div {:style (:wrapper styles)}
       [ui/form-helper-text {:style (:message styles)}
        (get-error validation-data field-name)]])))

(defn init
  [data validation-data validation-map]
  {:valid?        (fn [] (valid? data validation-data validation-map))
   :error-message (fn [props]
                    [error-message (merge props
                                          {:validation-data validation-data})])})
