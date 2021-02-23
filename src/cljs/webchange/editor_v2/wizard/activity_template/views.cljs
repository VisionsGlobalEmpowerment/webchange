(ns webchange.editor-v2.wizard.activity-template.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.activity-template.utils :refer [check-conditions]]
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
   1. {:options [{:key \"some-key\"
                  ...}]}
   2. {:options {:some-key {...}}}"
  [template]
  (if (sequential? (:options template))
    (->> (:options template)
         (map (fn [{:keys [key] :as option}]
                [(keyword key) option])))
    (:options template)))

(defn- filter-options
  [data metadata options]
  (->> options
       (filter (fn [[_ {:keys [conditions]}]]
                 (if (some? conditions)
                   (check-conditions conditions data metadata)
                   true)))))

(defn- set-default-values!
  [data options]
  (doseq [[key {:keys [default]}] options]
    (when (and (some? default)
               (not (some? (get @data key))))
      (swap! data assoc key default))))

(defn template
  [{:keys [template metadata data validator]}]
  (let [options (template->options template)]
    (set-default-values! data options)
    [ui/grid {:container   true
              :justify     "space-between"
              :spacing     24
              :align-items "center"}
     (for [[key option] (filter-options @data metadata options)]
       ^{:key key}
       [ui/grid {:item true :xs 12}
        [option-info {:key       key
                      :option    option
                      :data      data
                      :metadata  metadata
                      :validator validator}]])]))
