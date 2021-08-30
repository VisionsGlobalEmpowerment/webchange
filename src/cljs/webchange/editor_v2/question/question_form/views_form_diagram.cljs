(ns webchange.editor-v2.question.question-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.activity-tracks.track.views :refer [track]]
    [webchange.editor-v2.question.question-form.diagram.items-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.question.text.views-text-animation-editor :as chunks]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]))

(defn simple-dialog
  [{:keys [path]}]                                          ;; data coming in is a string
  (let [current-question-text-action @(re-frame/subscribe [::question-form.actions/current-question-action-info])
        handle-settings-click (fn [node-data]
                                (let [type (get-in node-data [:data :type])
                                      audio-data (case type
                                                   :question (get-in node-data [:data :action :data :audio-data])
                                                   :answer (get-in node-data [:data :answer :audio-data])
                                                   {})]
                                  (re-frame/dispatch [::translator-form.actions/set-current-phrase-action node-data])
                                  (re-frame/dispatch [::chunks/open audio-data (case type
                                                                                 :question (get-in node-data [:data :action :data])
                                                                                 :answer (get-in node-data [:data :answer])
                                                                                 {})])))

        scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        {:keys [nodes]} (get-diagram-items scene-data path)
        nodes-data (map (fn [{:keys [data]}]
                          (let [node-type (clojure.core/name (get-in data [:data :type]))]
                            (cond-> data
                                    :always (assoc :type node-type)
                                    :always (assoc :text (case node-type
                                                           "question" (get-in data [:data :action :data :text])
                                                           "answer" (get-in data [:data :answer :text])
                                                           "dialog" (or (get-in data [:data :action :phrase])
                                                                        (get-in data [:data :action :phrase-description]))
                                                           ""))
                                    :always (assoc :selected? (= (:path data)
                                                                 (:path current-question-text-action)))
                                    (or (= node-type "question")
                                        (= node-type "answer")) (assoc :addition-action {:icon    "settings"
                                                                                         :handler handle-settings-click}))))
                        nodes)
        handle-click (fn [node-data]
                       (re-frame/dispatch [::question-form.actions/set-current-question-action node-data]))]
    [track {:nodes         nodes-data
            :on-node-click handle-click}]))

(defn diagram-block
  []
  (let [action @(re-frame/subscribe [::question-form.actions/current-question-action-info])
        path (:question-path action)]
    [:div [simple-dialog {:path path}]]))
