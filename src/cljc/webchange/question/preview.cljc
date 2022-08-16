(ns webchange.question.preview
  (:require
    [webchange.question.create :as question]
    [webchange.question.get-question-data :refer [current-question-version default-question-data form->question-data]]
    [webchange.utils.scene-data :as scene-utils]))

(defn- add-background
  [activity-data {:keys [type] :as background-data}]
  (case type
    "background" (let [background-src (:src background-data)]
                   (-> activity-data
                       (update :assets conj {:url background-src :size 1 :type "image"})
                       (update :objects assoc :background {:type "background"
                                                           :src  background-src})
                       (update :scene-objects conj ["background"])))
    "layered-background" (let [{:keys [background decoration surface]} (:src background-data)]
                           (-> activity-data
                               (update :assets concat [{:url (:src background) :size 1 :type "image"}
                                                       {:url (:src decoration) :size 1 :type "image"}
                                                       {:url (:src surface) :size 1 :type "image"}])
                               (update :objects assoc :background {:type       "background"
                                                                   :background background
                                                                   :decoration decoration
                                                                   :surface    surface})
                               (update :scene-objects conj ["background"])))))

(defn get-scene-data
  ([form-data]
   (get-scene-data form-data {}))
  ([form-data {:keys [background]}]
   (let [empty-scene (cond-> scene-utils/empty-data
                             (some? background) (add-background background))]
     (->> (question/create (form->question-data form-data current-question-version)
                           {:action-name "question-action" :object-name "question"}
                           {:visible?                   true
                            :show-check-button?         true
                            :highlight-correct-options? true})
          (question/add-to-scene empty-scene)))))
