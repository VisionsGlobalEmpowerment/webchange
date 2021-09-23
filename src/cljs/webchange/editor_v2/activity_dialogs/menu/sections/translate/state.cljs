(ns webchange.editor-v2.activity-dialogs.menu.sections.translate.state
  (:require
   [re-frame.core :as re-frame]
   [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]
   [webchange.interpreter.events :as interpreter.events]
   [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
   [webchange.state.core :as core]
   [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::translate
  (fn [{:keys [db]} [_ lang]]
    (let [course-slug (core/current-course-id db)
          scene-slug (core/current-scene-id db)]
      {:dispatch [::warehouse/translate-activity {:course-slug course-slug
                                                  :scene-slug  scene-slug
                                                  :lang  lang}
                  {:on-success [::translate-success nil]}]})))

(re-frame/reg-event-fx
  ::translate-success
  (fn [{:keys [_]} [_ on-success {:keys [name data] :as response}]]
    {:dispatch-n (cond-> [[::core/set-scene-data {:scene-id   name
                                                  :scene-data data}]
                          [::interpreter.events/set-scene name data]
                          [::interpreter.events/store-scene name data]
                          [::translator-form.scene/init-state]
                          [::state-stage/reset-stage]]
                   (some? on-success) (conj (conj on-success response)))}))
