(ns webchange.templates.library.conversation
  (:require
    [webchange.templates.common-actions :refer [add-character add-question]]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]))

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

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog activity-data ;noop
    :add-question-object (add-question activity-data args)))

(core/register-template
  m create-template update-template)

