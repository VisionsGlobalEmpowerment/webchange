(ns webchange.editor-v2.activity-form.generic.components.history.views
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.parse-actions :refer [find-all-actions]]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.state.core :as core]
    [webchange.subs :as subs]))


(def modal-state-path [:editor-v2 :sandbox :restore-modal-state])
(def modal-versions-state-path [:editor-v2 :sandbox :restore-versions-state])
(def modal-template-state-path [:editor-v2 :sandbox :restore-template-state])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::versions
  (fn [db]
    (get-in db (concat modal-versions-state-path [:versions]) {})))

(re-frame/reg-sub
  ::update-available
  (fn [db]
    (let [activity-template-version (-> (subs/current-scene-data db) :metadata :template-version)
          template-version (get-in db (concat modal-template-state-path [:version]) 0)]
      (< activity-template-version template-version))))

;; Events
(re-frame/reg-event-fx
  ::reload-scene
  (fn [{:keys [_]} [_ name data]]
    {:pre [(string? name) (map? data)]}
    {:dispatch-n [[::core/set-scene-data {:scene-id   name
                                          :scene-data data}]
                  [::interpreter.events/set-scene name data]
                  [::interpreter.events/store-scene name data]]}))

(re-frame/reg-event-fx
  ::load-versions
  (fn [{:keys [db]} [_ course-slug scene-slug]]
    {:db         (assoc-in db [:loading :load-versions] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/courses/" course-slug "/scenes/" scene-slug "/versions")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-versions-success]
                  :on-failure      [:api-request-error :load-versions]}}))

(re-frame/reg-event-fx
  ::load-versions-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db modal-versions-state-path result)
     :dispatch-n (list [:complete-request :load-versions])}))

(re-frame/reg-event-fx
  ::restore-version
  (fn [{:keys [db]} [_ scene-version-id]]
    {:db         (assoc-in db [:loading :restore-scene-version] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/scene-versions/" scene-version-id "/restore")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::restore-version-success]
                  :on-failure      [:api-request-error :restore-scene-version]}}))

(re-frame/reg-event-fx
  ::restore-version-success
  (fn [_ [_ {:keys [name data]}]]
    {:dispatch-n (list [:complete-request :restore-scene-version]
                       [::reload-scene name data]
                       [::close])}))

(re-frame/reg-event-fx
  ::load-template
  (fn [{:keys [db]} [_ template-id]]
    (when template-id
      {:db         (assoc-in db [:loading :load-template] true)
       :http-xhrio {:method          :get
                    :uri             (str "/api/templates/" template-id "/metadata")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-template-success]
                    :on-failure      [:api-request-error :load-template]}})))

(re-frame/reg-event-fx
  ::load-template-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db modal-template-state-path result)
     :dispatch-n (list [:complete-request :load-template])}))

(re-frame/reg-event-fx
  ::update-template
  (fn [{:keys [db]} _]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)]
      {:db         (assoc-in db [:loading :update-template] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-slug "/update-template/" scene-slug)
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::update-template-success]
                    :on-failure      [:api-request-error :update-template]}})))

(re-frame/reg-event-fx
  ::update-template-success
  (fn [{:keys [db]} [_ {:keys [name data]}]]
    {:dispatch-n (list [:complete-request :update-template]
                       [::reload-scene name data]
                       [::close])}))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)
          template-id (-> (subs/current-scene-data db) :metadata :template-id)]
      {:db       (-> db
                     (assoc-in modal-state-path true)
                     (assoc-in modal-versions-state-path {})
                     (assoc-in modal-template-state-path {}))
       :dispatch-n (list [::load-versions course-slug scene-slug]
                         [::load-template template-id])})))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

;;

(defn- update-template-button
  []
  (let [update-available @(re-frame/subscribe [::update-available])]
    [:div
     [ui/button {:on-click #(re-frame/dispatch [::update-template])} "Update template"]
     (when update-available
       [ic/warning])]))

(defn- history-form
  []
  (let [versions @(re-frame/subscribe [::versions])]
    [:div
     [update-template-button]
     [ui/list
      (for [{:keys [id created-at description]} versions]
        ^{:key id}
        [ui/list-item
         [ui/grid {:container true
                   :spacing   8
                   :justify   "space-between"}
          [ui/grid {:item true :xs 10}
           [ui/typography created-at]
           (when description [ui/typography {:variant "caption"} description])]
          [ui/grid {:item true :xs 2}
           [ui/button {:size     "small"
                       :style    {:padding 0}
                       :on-click #(re-frame/dispatch [::restore-version id])}
            "Restore"]]]])]]))

(defn- history-modal
  []
  (let [open? @(re-frame/subscribe [::modal-state])
        close #(re-frame/dispatch [::close])]
    (when open?
      [ui/dialog
       {:open       true
        :on-close   close
        :full-width true}
       [ui/dialog-title
        "History"]
       [ui/dialog-content {:class-name "history-form"}
        [history-form]]
       [ui/dialog-actions
        [:div {:style {:position "relative"}}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click close}
          "Close"]]]])))

(defn history-button
  []
  [ui/form-control {:full-width true
                    :margin     "normal"}
   [ui/button
    {:on-click #(re-frame/dispatch [::open])}
    "History"]
   [history-modal]])
