(ns webchange.ui.components.select.utils)

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
  (or (nil? value)
      (empty-string? value)
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

(defn- add-empty-option
  [options {:keys [placeholder required?]}]
  (concat [{:text      (cond-> placeholder
                               required? (str " *"))
            :value     ""
            :disabled? true}]
          options))

(defn- fix-empty-option
  [options {:keys [placeholder value] :as props}]
  (let [has-empty-option? (some (fn [{:keys [value]}]
                                  (empty-string? value))
                                options)]
    (if (and (empty-value? value)
             (not has-empty-option?)
             (some? placeholder))
      (add-empty-option options props)
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
