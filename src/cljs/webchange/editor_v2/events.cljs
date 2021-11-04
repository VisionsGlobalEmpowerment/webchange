(ns webchange.editor-v2.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]
    [webchange.editor-v2.translator.state.window :as translator.window]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.question.state.window :as question.window]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]
    [webchange.editor-v2.text-animation-editor.chunks-editor.state :as translator.text]
    [webchange.state.state-course :as state-course]
    [webchange.state.warehouse :as warehouse]
    [webchange.subs :as subs]))

(re-frame/reg-event-fx
  ::init-editor
  (fn [{:keys [db]} [_ course-id scene-id]]
    {:db          (-> db
                      (assoc :loaded-course course-id)
                      (assoc :current-course course-id))
     :dispatch-n (list [::state-course/load-course-data course-id]
                       [::load-lesson-sets course-id]
                       [::ie/load-lessons course-id]
                       [::state-course/load-course-info course-id]
                       (when scene-id
                         [::ie/set-current-scene scene-id]))}))

(re-frame/reg-event-fx
  ::load-lesson-sets
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-lesson-sets
                {:course-slug course-slug}
                {:on-success [::load-lesson-sets-success]}]}))

(re-frame/reg-event-fx
  ::load-lesson-sets-success
  (fn [{:keys [db]} [_ {:keys [datasets items lesson-sets]}]]
    (let [course-has-concepts? (-> (and (empty? datasets)
                                        (empty? items)
                                        (empty? lesson-sets))
                                   (not))]
      {:db         (-> db
                       (assoc-in [:editor :course-has-concepts?] course-has-concepts?)
                       (assoc-in [:editor :course-datasets] datasets)
                       (assoc-in [:editor :course-dataset-items] items)
                       (assoc-in [:editor :course-lesson-sets] lesson-sets))
       :dispatch-n (list [:complete-request :lesson-sets])})))

(re-frame/reg-event-fx
  ::show-translator-form
  (fn [{:keys [_]} [_ action-node]]
    {:dispatch-n (list [::translator-form.actions/set-current-dialog-action action-node]
                       [::translator.window/open])}))

(re-frame/reg-event-fx
  ::show-configure-object-form
  (fn [{:keys [_]} [_ object-info]]
    {:dispatch-n (list [::translator.text/set-current-dialog-text object-info]
                       [::translator.text/open])}))

(re-frame/reg-event-fx
  ::edit-course-info
  (fn [{:keys [db]} [_ data]]
    (let [course-id (:id data)]
      {:db         (-> db
                       (assoc-in [:loading :edit-course-info] true))
       :http-xhrio {:method          :put
                    :uri             (str "/api/courses/" course-id "/info")
                    :params          (select-keys data [:name :slug :image-src :lang])
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::edit-course-info-success]
                    :on-failure      [:api-request-error :edit-course-info]}})))

(re-frame/reg-event-fx
  ::edit-course-info-success
  (fn [{:keys [db]} _]
    {:dispatch-n (list [:complete-request :edit-course-info])}))

(re-frame/reg-event-fx
  ::show-dialog-translator-form
  (fn [{:keys [_]} [_ action-node params]]
    {:dispatch-n (list [::translator-form.actions/set-current-dialog-action action-node]
                       [::dialog.window/open params])}))

(re-frame/reg-event-fx
  ::show-question-form
  (fn [{:keys [_]} [_ action-node]]
    {:dispatch-n (list [::question-form.actions/set-current-question-action
                        (-> action-node
                            (assoc-in [:data :question-path] [(first (:path action-node))]))]
                       [::question.window/open])}))

(re-frame/reg-event-fx
  ::show-translator-form-by-id
  (fn [{:keys [db]} [_ action-id params]]
    (let [action-data (subs/current-scene-action db action-id)
          node-data {:data action-data
                     :path [action-id]}]
      (if (= "dialog" (get-in node-data [:data :editor-type]))
        {:dispatch [::show-dialog-translator-form node-data params]}
        {:dispatch [::show-translator-form node-data]}))))
