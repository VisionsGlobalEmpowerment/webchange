(ns webchange.ui-framework.components.select.utils)

(defn empty-string?
  [value]
  (and (string? value)
       (empty? value)))

(defn empty-seq?
  [value]
  (and (sequential? value)
       (empty? value)))

(defn empty-value?
  [value]
  (or (empty-string? value)
      (empty-seq? value)))

(defn- fix-enable-prop
  [options]
  (->> options
       (map (fn [{:keys [enable?] :as option}]
              (if (some? enable?)
                (-> option
                    (assoc :disabled? (not enable?))
                    (dissoc :enable?))
                option)))))

(defn- fix-empty-option
  [options {:keys [placeholder value]}]
  (let [has-empty-option? (some (fn [{:keys [value]}]
                                  (empty-string? value))
                                options)
        add-empty-option (fn [options] (concat [{:text placeholder :value "" :disabled? true}] options))]
    (if (and (empty-value? value)
             (not has-empty-option?))
      (add-empty-option options)
      options)))

(defn fix-options
  [options props]
  (-> options
      (fix-enable-prop)
      (fix-empty-option props)))

(defn- parse-int
  [str-value]
  (js/parseInt str-value 10))

(defn parse-value
  [value type]
  (if-not (empty? value)
    (case type
      "int" (parse-int value)
      value)
    value))
