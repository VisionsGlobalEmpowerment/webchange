(ns webchange.question.get-question-data
  (:require
    [webchange.utils.deep-merge :refer [deep-merge]]))

(def current-question-version 2)

(defn param-name->object-name
  [param-name question-id]
  (str question-id "-" param-name))

(def available-values
  {:mark-options ["thumbs-up" "ok" "thumbs-down"]})

(def default-question-data
  {:alias             "New question"
   :question-type     "multiple-choice-image"
   :layout            "vertical"
   :answers-number    "one"
   :correct-answers   ["option-1"]

   :task-type         "text-image"
   :task-text         {:text "Question placeholder" :font-size 48}
   :task-image        {:src "/images/questions/question.png" :image-size "contain"}

   :options-number    2
   :options-label     "audio-text"

   :option-1-value    "option-1"
   :option-1-text     {:text "Option 1" :font-size 48}
   :option-1-image    {:src "/images/questions/option1.png" :image-size "contain"}
   :option-2-value    "option-2"
   :option-2-text     {:text "Option 2" :font-size 48}
   :option-2-image    {:src "/images/questions/option2.png" :image-size "contain"}
   :option-3-value    "option-3"
   :option-3-text     {:text "Option 3" :font-size 48}
   :option-3-image    {:src "/images/questions/option3.png" :image-size "contain"}
   :option-4-value    "option-4"
   :option-4-text     {:text "Option 4" :font-size 48}
   :option-4-image    {:src "/images/questions/option4.png" :image-size "contain"}

   :mark-options      [(-> available-values :mark-options first)
                       (-> available-values :mark-options last)]

   :thumbs-up-value   "thumbs-up"
   :thumbs-up-text    {:text "Thumbs Up" :font-size 48}
   :thumbs-up-image   {:src "/images/questions/thumbs_up.png" :image-size "contain"}
   :ok-value          "ok"
   :ok-text           {:text "Ok" :font-size 48}
   :ok-image          {:src "/images/questions/ok.png" :image-size "contain"}
   :thumbs-down-value "thumbs-down"
   :thumbs-down-text  {:text "Thumbs Down" :font-size 48}
   :thumbs-down-image {:src "/images/questions/thumbs_down.png" :image-size "contain"}})

(defn- update-data
  [data version]
  (let [data-updaters {2 (fn [data]
                           (-> data
                               (assoc :options-label (:option-label data))
                               (dissoc :option-label)))}
        update-data-version (fn [current-data dist-version]
                              (let [updater (get data-updaters dist-version)]
                                (updater current-data)))]
    (loop [current-version version
           current-data data]
      (if (not= current-version current-question-version)
        (recur (inc current-version)
               (update-data-version current-data (inc current-version)))
        current-data))))

(defn form->question-data
  [form-data data-version]
  (let [version (if (some? data-version) data-version 1)]
    (->> (update-data form-data version)
         (deep-merge {:version current-question-version} default-question-data))))
