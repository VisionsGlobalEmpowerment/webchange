(ns webchange.editor-v2.dialog.utils.dialog-action
  (:require
    [webchange.utils.scene-action-data :as action-utils]))

(def empty-action-path action-utils/empty-action-path)
(def inner-action-path action-utils/inner-action-path)

(def default-phrase-text "New action")
(def default-action {:type "sequence-data"
                     :data [{:type     "empty"
                             :duration 0}
                            {:type               "animation-sequence"
                             :phrase-text        default-phrase-text
                             :phrase-placeholder "Enter phrase text"
                             :audio              nil}]})

(def default-concept-action {:type "sequence-data"
                             :data [default-action]})

(def text-animation-action {:type "sequence-data"
                            :data [{:type     "empty"
                                    :duration 0}
                                   {:type        "text-animation"
                                    :animation   "color"
                                    :fill        0x00B2FF
                                    :phrase-text "Text animation"
                                    :audio       nil}]})

(def skip-effects {:start-skip-region {:text "Start skip"
                                       :value "start-skip-region"}
                   :end-skip-region {:text "End skip"
                                     :value "end-skip-region"}})
(defn get-empty-action
  [action]
  (get-in action action-utils/empty-action-path))

(defn get-inner-action
  [action]
  (get-in action action-utils/inner-action-path))

(defn update-inner-action
  [action data-patch]
  (update-in action action-utils/inner-action-path merge data-patch))

(defn update-inner-concept-action
  [action data-patch]
  (update-in action [:data 0 :data action-utils/inner-action-position] merge data-patch))

(defn get-dialog-node
  [action-data]
  {:type "sequence-data"
   :data [{:type     "empty"
           :duration 0}
          action-data]})

(defn- get-action-data
  [{:keys [action-name action-data]}]
  {:pre [(string? action-name)
         (or (nil? action-data)
             (map? action-data))]}
  (get-dialog-node (cond-> {:type "action"
                            :id   action-name}
                           (some? action-data) (merge action-data))))

(defn get-effect-action-data
  [{:keys [action-name]}]
  {:pre [(string? action-name)]}
  (get-action-data {:action-name action-name
                    :action-data {}}))

(defn get-emotion-action-data
  [props]
  (-> (action-utils/create-add-animation-action props)
      (get-dialog-node)))

(defn get-remove-emotion-action-data
  [props]
  (-> (action-utils/create-remove-animation-action props)
      (get-dialog-node)))

(defn get-inner-action-type
  [action-data]
  (-> (get-inner-action action-data)
      (get :type)))

(defn dialog-phrase-action?
  [action-data]
  (-> (get-inner-action-type action-data)
      (= "animation-sequence")))

(defn text-animation-action?
  [action-data]
  (-> (get-inner-action action-data)
      (action-utils/text-animation-action?)))

(defn effect-action?
  [action-data]
  (-> (get-inner-action-type action-data)
      (= "action")))
