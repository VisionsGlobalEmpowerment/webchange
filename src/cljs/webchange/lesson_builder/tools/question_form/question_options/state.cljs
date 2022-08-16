(ns webchange.lesson-builder.tools.question-form.question-options.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.question.get-question-data :refer [available-values]]
    [webchange.utils.list :refer [without-item]]))

(re-frame/reg-sub
  ::title
  :<- [::state/question-type]
  :<- [::state/question-type-options]
  (fn [[question-type options]]
    (or (some (fn [{:keys [text value]}]
                (and (= value question-type) text))
              options)
        "New Question")))

;; task type

(def task-type-key :task-type)

(re-frame/reg-sub
  ::task-type
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data task-type-key)))

(re-frame/reg-event-fx
  ::set-task-type
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (state/update-form-data db task-type-key value)}))

(re-frame/reg-sub
  ::task-type-options
  (fn []
    (re-frame/subscribe [::state/question-type]))
  (fn [question-type]
    (let [task-types [{:text           "Text"
                       :value          "text"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]}
                      {:text           "Image"
                       :value          "image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]}
                      {:text           "Text with image"
                       :value          "text-image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]}
                      {:text           "Voice-over only"
                       :value          "voice-over"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]}]]
      (->> task-types
           (filter (fn [{:keys [question-types]}]
                     (some #{question-type} question-types)))
           (map (fn [{:keys [text value]}]
                  {:text  text
                   :value value}))))))

;; options number

(re-frame/reg-sub
  ::show-options-number?
  :<- [::state/question-type]
  (fn [question-type]
    (not= question-type "thumbs-up-n-down")))

(def options-number-key :options-number)

(re-frame/reg-sub
  ::options-number
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data options-number-key)))

(re-frame/reg-event-fx
  ::set-options-number
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (state/update-form-data db options-number-key value)}))

(re-frame/reg-sub
  ::options-number-options
  (fn []
    (->> (range 2 5)
         (map (fn [number]
                {:text  number
                 :value number})))))

;; correct answer

(defn- toggle-list-item
  [list item value]
  (if value
    (conj list item)
    (without-item list item)))

(re-frame/reg-sub
  ::show-correct-answers?
  :<- [::answers-number]
  (fn [answers-number]
    (not= answers-number "any")))

(re-frame/reg-sub
  ::correct-answers-type
  :<- [::answers-number]
  (fn [answers-number]
    (case answers-number
      "one" "one"
      "many" "many"
      nil)))

(defn- get-mark-option-text
  [form-data mark-option]
  (let [mark-option-text (-> (str mark-option "-text") (keyword))]
    (get-in form-data [mark-option-text :text])))

(re-frame/reg-sub
  ::correct-answers-options
  :<- [::state/form-data]
  :<- [::state/question-type]
  :<- [::options-number]
  :<- [::mark-options]
  (fn [[form-data question-type options-number mark-options]]
    (if (= question-type "thumbs-up-n-down")
      (->> mark-options
           (map (fn [value]
                  {:text  (get-mark-option-text form-data value)
                   :value value})))
      (->> (range options-number)
           (map inc)
           (map (fn [number]
                  (let [value-key (-> (str "option-" number "-value") (keyword))
                        value (get form-data value-key)]
                    {:text  (str "Option " number)
                     :value value})))))))

(def correct-answers-key :correct-answers)

;; correct answers one

(re-frame/reg-sub
  ::correct-answers-one
  :<- [::state/form-data]
  (fn [form-data]
    (->> correct-answers-key
         (get form-data)
         (first))))

(re-frame/reg-sub
  ::correct-answers-one-options
  :<- [::correct-answers-options]
  (fn [answers-options]
    answers-options))

(re-frame/reg-event-fx
  ::set-correct-answers-one
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (state/update-form-data db correct-answers-key [value])}))

;; correct answers many

(re-frame/reg-sub
  ::correct-answers-many
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data correct-answers-key)))

(re-frame/reg-sub
  ::correct-answers-many-options
  :<- [::correct-answers-many]
  :<- [::correct-answers-options]
  (fn [[current-value answers-options]]
    (->> answers-options
         (map (fn [{:keys [value] :as option}]
                (merge option
                       {:checked? (->> current-value (some #{value}) (some?))}))))))

(re-frame/reg-event-fx
  ::set-correct-answers-many
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ checked? value]]
    (let [new-value (-> (state/get-form-data db)
                        (get correct-answers-key)
                        (toggle-list-item value checked?))]
      {:db (state/update-form-data db correct-answers-key new-value)})))


;; answers number

(def answers-number-key :answers-number)

(re-frame/reg-sub
  ::answers-number
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data answers-number-key)))

(re-frame/reg-event-fx
  ::set-answers-number
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (state/update-form-data answers-number-key value)
             (state/update-form-data :correct-answers []))}))

(re-frame/reg-sub
  ::answers-number-options
  (fn []
    [{:text  "One"
      :value "one"}
     {:text  "Many"
      :value "many"}
     {:text  "Any"
      :value "any"}]))

;; mark options

(def mark-options-key :mark-options)

(re-frame/reg-sub
  ::show-mark-options?
  :<- [::state/question-type]
  (fn [question-type]
    (= question-type "thumbs-up-n-down")))

(re-frame/reg-sub
  ::mark-options
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data mark-options-key)))

(re-frame/reg-sub
  ::available-mark-options
  :<- [::state/form-data]
  :<- [::mark-options]
  (fn [[form-data current-value]]
    (->> (:mark-options available-values)
         (map (fn [value]
                {:text     (get-mark-option-text form-data value)
                 :value    value
                 :checked? (-> (some #{value} current-value)
                               (or false))})))))

(re-frame/reg-event-fx
  ::set-mark-option
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ checked? value]]
    (let [new-value (-> (state/get-form-data db)
                        (get mark-options-key)
                        (toggle-list-item value checked?))]
      {:db (state/update-form-data db mark-options-key new-value)})))
