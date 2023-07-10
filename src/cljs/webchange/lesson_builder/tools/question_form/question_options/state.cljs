(ns webchange.lesson-builder.tools.question-form.question-options.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.question-form.state :as state]
    [webchange.question.get-question-data :refer [available-values]]
    [webchange.utils.list :refer [without-item]]))

(def answers-number-key :answers-number)
(def correct-answers-key :correct-answers)
(def answers-sequence-key :answers-sequence)
(def answers-types-key :answers-types)
(def mark-options-key :mark-options)
(def options-number-key :options-number)
(def task-type-key :task-type)
(def question-alias-key :alias)
(def question-type-key :question-type)

;; question alias

(re-frame/reg-sub
  ::question-alias
  :<- [::state/form-data]
  #(get % question-alias-key))

(re-frame/reg-event-fx
  ::set-question-alias
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (state/update-form-data db question-alias-key value)}))

;; question type

(re-frame/reg-sub
  ::question-type
  :<- [::state/form-data]
  #(get % question-type-key))

(re-frame/reg-sub
  ::question-type-options
  (fn []
    [{:text  "Multiple choice image"
      :value "multiple-choice-image"}
     {:text  "Multiple choice text"
      :value "multiple-choice-text"}
     {:text  "Multiple choice mixed"
      :value "multiple-choice-mix"}
     {:text  "Arrange Images"
      :value "arrange-images"}
     {:text  "Thumbs up & thumbs down"
      :value "thumbs-up-n-down"}]))

(re-frame/reg-event-fx
  ::set-question-type
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (state/update-form-data question-type-key value)
             (state/update-form-data correct-answers-key []))}))

;; task type

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
    (re-frame/subscribe [::question-type]))
  (fn [question-type]
    (let [task-types [{:text           "Text"
                       :value          "text"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "multiple-choice-mix" "thumbs-up-n-down" "arrange-images"]}
                      {:text           "Image"
                       :value          "image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]}
                      {:text           "Text with image"
                       :value          "text-image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]}
                      {:text           "Voice-over only"
                       :value          "voice-over"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "multiple-choice-mix" "thumbs-up-n-down" "arrange-images"]}]]
      (->> task-types
           (filter (fn [{:keys [question-types]}]
                     (some #{question-type} question-types)))
           (map (fn [{:keys [text value]}]
                  {:text  text
                   :value value}))))))

;; options number

(re-frame/reg-sub
  ::show-options-number?
  :<- [::question-type]
  (fn [question-type]
    (not= question-type "thumbs-up-n-down")))

(re-frame/reg-sub
  ::options-number
  :<- [::state/form-data]
  (fn [form-data]
    (get form-data options-number-key)))

(re-frame/reg-event-fx
  ::set-options-number
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (state/update-form-data options-number-key value)
             (state/update-form-data correct-answers-key []))}))

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
  :<- [::question-type]
  :<- [::answers-number]
  (fn [[question-type answers-number]]
    (and
     (not= question-type "arrange-images")
     (not= answers-number "any"))))

(defn- get-mark-option-text
  [form-data mark-option]
  (let [mark-option-text (-> (str mark-option "-text") (keyword))]
    (get-in form-data [mark-option-text :text])))

(re-frame/reg-sub
  ::correct-answers-options
  :<- [::state/form-data]
  :<- [::question-type]
  :<- [::options-number]
  :<- [::mark-options]
  (fn [[form-data question-type options-number mark-options]]
    (if (= question-type "thumbs-up-n-down")
      (->> mark-options
           (map (fn [value]
                  {:text  (get-mark-option-text form-data value)
                   :value value}))
           (sort-by :text))
      (->> (range options-number)
           (map inc)
           (map (fn [number]
                  (let [value-key (-> (str "option-" number "-value") (keyword))
                        value (get form-data value-key)]
                    {:text  (str "Option " number)
                     :value value})))
           (sort-by :text)))))

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

(re-frame/reg-sub
  ::show-answers-number?
  :<- [::question-type]
  (fn [question-type]
    (not= question-type "arrange-images")))

;; mark options

(re-frame/reg-sub
  ::show-mark-options?
  :<- [::question-type]
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
                               (or false))}))
         (sort-by :text))))

(re-frame/reg-event-fx
  ::set-mark-option
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ checked? value]]
    (let [new-value (-> (state/get-form-data db)
                        (get mark-options-key)
                        (toggle-list-item value checked?))]
      (cond-> {:db (state/update-form-data db mark-options-key new-value)}
              (false? checked?) (assoc :dispatch [::set-correct-answers-many checked? value])))))

;; correct-sequence

(re-frame/reg-sub
  ::show-correct-sequence?
  :<- [::question-type]
  (fn [question-type]
    (= question-type "arrange-images")))

(re-frame/reg-sub
  ::answers-sequence
  :<- [::state/form-data]
  :<- [::options-number]
  (fn [[form-data options-number]]
    (let [options (->> (range options-number)
                       (map inc)
                       (map (fn [number]
                              (let [value-key (-> (str "option-" number "-value") (keyword))
                                    value (get form-data value-key)]
                                {:text  (str "Option " number)
                                 :value value}))))]
      (->> (range options-number)
           (map (fn [number]
                  (let [value-key (-> (str "sequence-" number "-value") (keyword))
                        value (get-in form-data [answers-sequence-key value-key])]
                    {:idx number
                     :value (:value value)
                     :options options})))))))

(re-frame/reg-event-fx
  ::set-answer-sequence-item
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    (let [value-key (-> (str "sequence-" idx "-value") (keyword))
          new-value (-> (state/get-form-data db)
                        (get answers-sequence-key {})
                        (assoc value-key {:value value
                                          :position idx}))]
      {:db (state/update-form-data db answers-sequence-key new-value)})))

(re-frame/reg-sub
  ::show-answer-types?
  :<- [::question-type]
  (fn [question-type]
    (= question-type "multiple-choice-mix")))

(re-frame/reg-sub
  ::answers-types
  :<- [::state/form-data]
  :<- [::options-number]
  (fn [[form-data options-number]]
    (->> (range 1 (inc options-number))
         (map (fn [number]
                (let [value-key (-> (str "type-" number "-value") (keyword))
                      value (get-in form-data [answers-types-key value-key])]
                  {:idx number
                   :value value}))))))

(re-frame/reg-event-fx
  ::set-answer-type
  [(i/path state/path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    (let [value-key (-> (str "type-" idx "-value") (keyword))
          new-value (-> (state/get-form-data db)
                        (get answers-types-key {})
                        (assoc value-key value))]
      {:db (state/update-form-data db answers-types-key new-value)})))

(re-frame/reg-sub
  ::answers-type-options
  (fn []
    [{:text "Text"
      :value "text"}
     {:text "Image"
      :value "image"}]))

(comment
  @(re-frame/subscribe [::state/form-data])
  @(re-frame/subscribe [::answers-types]))
