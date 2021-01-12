(ns webchange.editor-v2.creation-progress.translation-progress.validate-action
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :as actions]
    [webchange.editor-v2.subs :as editor-subs]))

(defn- audio-defined?
  [action-data]
  (->> (select-keys action-data [:audio :start :duration :data])
       (some (fn [[_ value]] (nil? value)))
       (not)))

(defn- origin-text-defined?
  [action-data]
  (let [origin-text (:phrase-text action-data)
        default-phrase-text (-> actions/default-action actions/get-inner-action :phrase-text)]
    (and (not (empty? origin-text))
         (not (= origin-text default-phrase-text)))))

(defn phrase-origin-text-defined?
  [phrase-action-data]
  (-> phrase-action-data
      actions/get-inner-action
      origin-text-defined?))

(defn phrase-audio-defined?
  [phrase-action-data]
  (-> phrase-action-data
      actions/get-inner-action
      audio-defined?))

(defn- validate-activity-phrase-action
  [action-data]
  (and (phrase-audio-defined? action-data)
       (phrase-origin-text-defined? action-data)))

(defn- validate-concept-phrase-action
  [action-data]
  (let [dataset-items (map second @(re-frame/subscribe [::editor-subs/course-dataset-items]))
        item-field (->> action-data :from-var first :var-property keyword)]
    (every? (fn [dataset-item]
              (let [action-data (-> dataset-item
                                    (get-in [:data item-field])
                                    (get-in [:data 0]))]
                (validate-activity-phrase-action action-data)))
            dataset-items)))

(defn validate-phrase-action
  [action-data]
  (let [concept-action? (contains? action-data :from-var)]
    (if concept-action?
      (validate-concept-phrase-action action-data)
      (validate-activity-phrase-action action-data))))

(defn empty-dialog-action?
  [action-data]
  (let [default-action actions/default-action]
    (and (= (count (:data action-data)) 1)
         (= (-> action-data :data first) default-action))))

(defn validate-dialog-action
  [action-data]
  (or (empty-dialog-action? action-data)
      (every? validate-phrase-action (:data action-data))))
