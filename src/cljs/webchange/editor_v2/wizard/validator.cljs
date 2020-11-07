(ns webchange.editor-v2.wizard.validator
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:wrapper {:position "relative"
             :width    0
             :height   0}
   :message {:position "absolute"
             :left     "13px"
             :width    "300px"
             :color    (get-in-theme [:palette :secondary :main])}})

(defn- validate
  [data validation-data validation-map]
  (doseq [[field validators] validation-map]
    (let [value (if (= field :root)
                  @data
                  (get @data field))
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
  ([data]
   (init data {} nil))
  ([data validation-map]
   (init data validation-map nil))
  ([data validation-map parent-validator]
   (let [validation-data (r/atom {})
         child-validators (atom [])
         instance (atom {})]
     (swap! instance merge
            {:valid?        (fn []
                              (let [self-valid? (valid? data validation-data validation-map)
                                    children-valid? (->> @child-validators
                                                         (map (fn [{:keys [valid?]}] (valid?)))
                                                         (doall)
                                                         (every? identity))]
                                (and self-valid? children-valid?)))
             :connect       (fn [child-validator]
                              (swap! child-validators conj child-validator))
             :detach        (fn [child-validator]
                              (swap! child-validators (fn [validators]
                                                        (remove #{child-validator} validators))))
             :error-message (fn [props]
                              [error-message (merge props
                                                    {:validation-data validation-data})])
             :destroy       (fn []
                              (when (some? parent-validator)
                                (let [{:keys [detach]} parent-validator]
                                  (detach @instance))))})

     (when (some? parent-validator)
       (let [{:keys [connect]} parent-validator]
         (connect @instance)))
     @instance)))

(defn connect-data
  ([parent-data path]
   (connect-data parent-data path {}))
  ([parent-data path default-data]
   (connect-data parent-data path default-data nil))
  ([parent-data path default-data persistent-data]
   (let [fixed-path (if (sequential? path) path [path])
         data (r/atom (if (some? persistent-data)
                        persistent-data
                        (get-in @parent-data fixed-path default-data)))]
     (swap! parent-data assoc-in fixed-path @data)
     (add-watch data :connect-data
                (fn [_ _ _ new-state]
                  (swap! parent-data assoc-in fixed-path new-state)))
     data)))
