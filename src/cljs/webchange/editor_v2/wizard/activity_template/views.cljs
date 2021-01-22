(ns webchange.editor-v2.wizard.activity-template.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.activity-template.views-answers :refer [answers-option]]
    [webchange.editor-v2.wizard.activity-template.views-characters :refer [characters-option]]
    [webchange.editor-v2.wizard.activity-template.views-image :refer [image-option]]
    [webchange.editor-v2.wizard.activity-template.views-lookup :refer [lookup-option]]
    [webchange.editor-v2.wizard.activity-template.views-lookup-image :refer [lookup-image-option]]
    [webchange.editor-v2.wizard.activity-template.views-pages :refer [pages-option]]
    [webchange.editor-v2.wizard.activity-template.views-string :refer [string-option]]
    [webchange.editor-v2.wizard.activity-template.views-strings-list :refer [strings-list-option]]))

(defn- option-info
  [{:keys [option] :as props}]
  (case (:type option)
    "characters" [characters-option props]
    "image" [image-option props]
    "lookup" [lookup-option props]
    "lookup-image" [lookup-image-option props]
    "pages" [pages-option props]
    "string" [string-option props]
    "strings-list" [strings-list-option props]
    "questions-no-image" [answers-option props false]
    nil))

(defn- template->options
  "Support both versions of options declaration:
   1. {:options [{:key :some-key
                  ...}]}
   2. {:options {:some-key {...}}}"
  [template]
  (if (sequential? (:options template))
    (->> (:options template)
         (map (fn [{:keys [key] :as option}]
                [key option])))
    (:options template)))

(defn- check-condition
  [{:keys [key state value]} data]
  (case (keyword state)
    :not-in (let []
              (not (some #{(get data key)} value)))
    true))

(defn- filter-options
  [data options]
  (->> options
       (filter (fn [[_ {:keys [conditions]}]]
                 (if (some? conditions)
                   (every? (fn [condition] (check-condition condition data)) conditions)
                   true)))))

(defn template
  [{:keys [template data validator]}]
  (let [options (->> template
                     (template->options)
                     (filter-options @data))]
    [ui/grid {:container   true
              :justify     "space-between"
              :spacing     24
              :align-items "center"}
     (for [[key option] options]
       ^{:key key}
       [ui/grid {:item true :xs 12}
        [option-info {:key       key
                      :option    option
                      :data      data
                      :validator validator}]])]))
