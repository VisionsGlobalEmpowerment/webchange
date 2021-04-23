(ns webchange.editor-v2.layout.components.change-skin.state
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.renderer.state.db :as renderer-state]
    [webchange.editor-v2.layout.state :refer [path-to-db]]
    [webchange.subs :as subs]
    [webchange.interpreter.renderer.state.editor :as editor-renderer-state]
    [webchange.editor.events :as edit-scene]))

(re-frame/reg-event-fx
  ::load-characters
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :load-characters] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/courses/editor/character-skin")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-characters-success]
                  :on-failure      [:api-request-error :load-characters]}}))

(re-frame/reg-event-fx
  ::load-characters-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db (path-to-db [:characters]) result)
     :dispatch-n (list [:complete-request :load-characters])}))


(re-frame/reg-sub
  ::animation-selected?
  (fn [db]
    (let [name (editor-renderer-state/selected-object db)
          current-scene (subs/current-scene db)
          o (subs/scene-object db current-scene name)]
      (= "animation" (:type o)))))

(re-frame/reg-sub
  ::current-skin
  (fn [db]
    (let [name (editor-renderer-state/selected-object db)
          current-scene (subs/current-scene db)
          o (subs/scene-object db current-scene name)]
      (:skin o))))

(re-frame/reg-sub
  ::available-skins
  (fn [db]
    (let [name (editor-renderer-state/selected-object db)
          current-scene (subs/current-scene db)
          o (subs/scene-object db current-scene name)
          animation-name (:name o)
          characters (get-in db (path-to-db [:characters]) [])]
      (->> characters
           (filter #(= animation-name (:name %)))
           first
           :skins))))

(re-frame/reg-event-fx
  ::change-position
  (fn [{:keys [db]} [_ x y]]
    (let [name (editor-renderer-state/selected-object db)
          current-scene (subs/current-scene db)
          state (-> (subs/scene-object db current-scene name)
                    (assoc :x x :y y))]
      {:dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                      :target   name
                                                      :state    state}]
                         [::edit-scene/update-current-scene-object {:target name
                                                                    :state  state}]
                         [::edit-scene/save-current-scene current-scene])})))

(re-frame/reg-event-fx
  ::change-skin
  (fn [{:keys [db]} [_ skin]]
    (let [name (editor-renderer-state/selected-object db)
          current-scene (subs/current-scene db)
          state (-> (subs/scene-object db current-scene name)
                    (assoc :skin skin))
          current-scene (subs/current-scene db)
          animation-name (or (:scene-name state) (:name state))]
      {:set-skin {:state (get @(get-in db (renderer-state/path-to-db [:objects])) (keyword animation-name))
                  :skin skin}
       :dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                      :target   name
                                                      :state    state}]
                         [::edit-scene/update-current-scene-object {:target name
                                                                    :state  state}]
                         [::edit-scene/save-current-scene current-scene])})))
