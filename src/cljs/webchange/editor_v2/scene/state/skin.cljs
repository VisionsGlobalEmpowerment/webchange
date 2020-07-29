(ns webchange.editor-v2.scene.state.skin
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.scene.state.db :refer [path-to-db]]
    [webchange.subs :as subs]
    [webchange.editor.subs :as editor-subs]
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
    (let [{:keys [scene-id name]} (editor-subs/selected-object db)
          o (subs/scene-object db scene-id name)]
      (->> o
           :states
           (map :type)
           (into #{(:type o)})
           (some #{"animation"})))))

(re-frame/reg-sub
  ::current-skin
  (fn [db]
    (let [{:keys [scene-id name]} (editor-subs/selected-object db)
          o (subs/scene-object db scene-id name)]
      (:skin o))))

(re-frame/reg-sub
  ::available-skins
  (fn [db]
    (let [{:keys [scene-id name]} (editor-subs/selected-object db)
          o (subs/scene-object db scene-id name)
          animation-name (:name o)
          characters (get-in db (path-to-db [:characters]) [])]
      (->> characters
           (filter #(= animation-name (:name %)))
           first
           :skins))))

(re-frame/reg-event-fx
  ::change-position
  (fn [{:keys [db]} [_ x y]]
    (let [{:keys [scene-id name]} (editor-subs/selected-object db)
          state (-> (subs/scene-object db scene-id name)
                    (assoc :x x :y y))
          current-scene (subs/current-scene db)]
      {:dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                      :target   name
                                                      :state    state}]
                         [::edit-scene/update-current-scene-object {:target name
                                                                    :state  state}]
                         [::edit-scene/save-current-scene current-scene])})))

(re-frame/reg-event-fx
  ::change-skin
  (fn [{:keys [db]} [_ skin]]
    (let [{:keys [scene-id name]} (editor-subs/selected-object db)
          state (-> (subs/scene-object db scene-id name)
                    (assoc :skin skin))
          current-scene (subs/current-scene db)
          animation-name (or (:scene-name state) (:name state))]
      {:set-skin {:state (get-in db [:scenes scene-id :animations animation-name])
                  :skin skin}
       :dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                      :target   name
                                                      :state    state}]
                         [::edit-scene/update-current-scene-object {:target name
                                                                    :state  state}]
                         [::edit-scene/save-current-scene current-scene])})))
