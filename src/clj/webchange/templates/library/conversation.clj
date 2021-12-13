(ns webchange.templates.library.conversation
  (:require
    [webchange.templates.common-actions :refer [add-character get-next-action-index increase-next-action-index]]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]
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
                                                        :type        "string"}}}}})
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
  (reduce (fn [scene-data {:keys [name skeleton]}]
            (add-character scene-data {:name       skeleton
                                       :scene-name name}))
          (common/init-metadata m t args)
          (:characters args)))

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
            :always (increase-next-action-index index) 
            :always (place-dialog {(keyword action-name) default-dialog} action-name))))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog (add-dialog activity-data args)))

(core/register-template
  m create-template update-template)

