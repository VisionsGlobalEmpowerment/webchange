(ns webchange.game-changer.steps.fill-template.template-options)

(defn- data->template
  [data]
  (get data :template {}))

(defn- template->options
  [template-data]
  (get template-data :options []))

(defn- get-character-option
  [options]
  (some (fn [{:keys [type] :as option}]
          (and (= type "characters") option))
        options))

(defn filter-options
  [options filter-types]
  (filter (fn [[_ {:keys [type]}]]
            (->> filter-types (some #{type}) not))
          options))

(defn data->options
  [data]
  (-> data data->template template->options))

(defn data->character-option
  [data]
  (-> data data->template template->options get-character-option))
