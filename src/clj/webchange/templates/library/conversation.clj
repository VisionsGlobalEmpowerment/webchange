(ns webchange.templates.library.conversation
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.characters :as characters]
    [webchange.templates.core :as core]
    [webchange.question.create :as question-object]
    [webchange.question.get-question-data :refer [form->question-data]]))

(def m {:id          26
        :name        "Conversation"
        :tags        ["Independent Practice"]
        :description "Conversation"
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   5}}
        :actions     {:add-dialog   {:title   "Add dialogue",
                                     :options {:dialog {:label       "Dialog name"
                                                        :description "Dialog name"
                                                        :placeholder "(ex. Conversation about ball)"
                                                        :type        "string"}}}
                      :add-question-object {:title   "Add question",
                                            :options {:question-page-object {:label         "Question"
                                                                             :type          "question-object"
                                                                             :answers-label "Answers"
                                                                             :max-answers   4}}}}})
(def t {:assets        [{:url "/raw/img/casa/background.jpg", :size 10 :type "image"}],
        :objects       {:background {:type "background", :src "/raw/img/casa/background.jpg"}},
        :scene-objects [["background"]],
        :actions       {:script        {:type   "workflow"
                                        :data   [{:type "start-activity"}
                                                 {:type "action" :id "dialog-main"}]
                                        :on-end "finish"}
                        :dialog-main   (dialog/default "Main")
                        :placeholder   {:type "empty" :duration 200}
                        :finish        {:type "finish-activity"}
                        :stop-activity {:type "stop-activity"}}
        :triggers      {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "script"}},
        :metadata      {:autostart         true
                        :tracks            [{:title "Sequence"
                                             :nodes [{:type      "dialog"
                                                      :action-id "dialog-main"}]}]
                        :next-action-index 0}})

(defn create-template
  [args]
  (-> (common/init-metadata m t args)
      (characters/add-characters (:characters args))))

(defn- get-next-action-index
  [activity-data]
  (get-in activity-data [:metadata :next-action-index]))

(defn- increase-next-action-index
  [activity-data]
  (update-in activity-data [:metadata :next-action-index] inc))

(defn- place-question
  [activity-data actions action-name]
  (-> activity-data
      (update :actions merge actions)
      (update-in [:metadata :tracks 0 :nodes] conj {:type "question" :action-id action-name})
      (update-in [:metadata :available-actions] concat [{:action action-name
                                                         :name   (str "Ask " action-name)}])))

(defn- place-dialog
  [activity-data actions action-name]
  (-> activity-data
      (update :actions merge actions)
      (update-in [:actions :script :data] conj {:type "action" :id action-name})
      (update-in [:metadata :tracks 0 :nodes] conj {:type "dialog" :action-id action-name})))

(defn- add-assets
  [activity-data assets]
  (-> activity-data
      (update :assets concat assets)))

(defn- add-dialog
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "dialog-" index)
        default-dialog (dialog/default (str "dialog-" index))]
    (cond-> activity-data
            :always (increase-next-action-index)
            :always (place-dialog {(keyword action-name) default-dialog} action-name))))

(defn- add-question
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "question-" index)
        question-actions (question/create (:question-page args) {:suffix      index
                                                                 :action-name action-name})
        question-assets (question/get-assets (:question-page args))]
    (-> activity-data
        (increase-next-action-index)
        (place-question question-actions action-name)
        (add-assets question-assets))))

(defn- add-question-object
  [activity-data {:keys [question-page-object]}]
  (let [index (get-next-action-index activity-data)
        next-action-name "script"
        action-name (str "question-" index)
        object-name (str "question-" index)
        question-data (question-object/create
                        (form->question-data question-page-object)
                        {:suffix           index
                         :action-name      action-name
                         :object-name      object-name
                         :next-action-name next-action-name})]
    (-> activity-data
        (increase-next-action-index)
        (question-object/add-to-scene question-data))))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog (add-dialog activity-data args)
    :add-question (add-question activity-data args)
    :add-question-object (add-question-object activity-data args)))

(core/register-template
  m create-template update-template)

