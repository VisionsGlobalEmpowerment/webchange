(ns webchange.editor-v2.wizard.activity-template.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.activity-template.utils :refer [check-conditions]]
    [webchange.editor-v2.wizard.activity-template.question.views :refer [question-option]]
    [webchange.editor-v2.wizard.activity-template.views-answers :refer [answers-option]]
    [webchange.editor-v2.wizard.activity-template.views-characters :refer [characters-option]]
    [webchange.editor-v2.wizard.activity-template.views-image :refer [image-option]]
    [webchange.editor-v2.wizard.activity-template.views-lookup :refer [lookup-option]]
    [webchange.editor-v2.wizard.activity-template.views-lookup-image :refer [lookup-image-option]]
    [webchange.editor-v2.wizard.activity-template.views-pages :refer [pages-option]]
    [webchange.editor-v2.wizard.activity-template.views-string :refer [string-option]]
    [webchange.editor-v2.wizard.activity-template.views-strings-list :refer [strings-list-option]]
    [webchange.editor-v2.wizard.activity-template.views-video :refer [video-option]]
    [webchange.editor-v2.wizard.activity-template.views-video-ranges :refer [video-ranges-option]]
    [webchange.editor-v2.wizard.activity-template.view_delete :refer [delete-object-option]]))

(defn- undefined-option
  [{:keys [key option]}]
  [ui/chip {:label   (str "Unhandled option: <" key ">, type: <" (:type option) ">")
            :color   "secondary"
            :variant "outlined"}])

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
    "questions-no-image" [answers-option props]
    ;"question" [answers-option props]
    "question" [question-option props]
    "remove-editable-object" [delete-object-option props]
    "video" [video-option props]
    "video-ranges" [video-ranges-option props]
    [undefined-option props]))

(defn prepare-options
  "Support both versions of options declaration:
   1. {:options [{:key \"some-key\"
                  ...}]}
   2. {:options {:some-key {...}}}"
  [options]
  (if (sequential? options)
    (->> options
         (map (fn [{:keys [key] :as option}]
                [(keyword key) option])))
    options))

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

(defn options-form
  [{:keys [options metadata data validator]}]
  (let [filtered-options (filter-options @data metadata options)]
    (set-default-values! data options)
    [:div.template-form
     (for [[key option] filtered-options]
       ^{:key key}
       [:div.option-block
        [option-info {:key       key
                      :option    option
                      :data      data
                      :metadata  metadata
                      :validator validator}]])]))

(defn template
  [{:keys [template] :as props}]
  (let [options (prepare-options (get template :options []))]
    [options-form (-> props
                      (assoc :options options)
                      (dissoc template))]))
