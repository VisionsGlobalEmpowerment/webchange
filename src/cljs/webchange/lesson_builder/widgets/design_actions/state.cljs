(ns webchange.lesson-builder.widgets.design-actions.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.menu.state :as menu]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.template-options.state :as template-options]
    [webchange.lesson-builder.tools.select-view.state :as select-view]))

(def path-to-db :lesson-builder/design-actions)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::actions
  :<- [::state/activity-data]
  (fn [activity-data]
    (let [has-template-options? (-> activity-data
                                    (template-options/get-template-options)
                                    (seq))
          has-views?  (-> activity-data
                          (select-view/get-views)
                          (seq))]
      (cond-> []
              has-template-options? (conj {:id        :template-options
                                           :text      "Template Options"
                                           :icon      "template"
                                           :menu-item :template-options})
              has-views? (conj {:id      :select-view
                                :text    "Views"
                                :icon    "build"
                                :content :select-view})
              :always (concat [{:id      :activity-actions
                                :text    "Build"
                                :icon    "build"
                                :content :activity-actions}
                               {:id      :effects-add
                                :text    "Add Effects"
                                :icon    "effects"
                                :content :effects-add}
                               {:id        :voice-translate
                                :text      "Voice & Translate"
                                :icon      "translate"
                                :menu-item :voice-translate}
                               {:id      :question-options
                                :text    "Question Options"
                                :icon    "question"
                                :content :question-options}])))))

;; active menu item

(def active-menu-item-key :active-menu-item)

(defn- get-active-menu-item
  [db]
  (get db active-menu-item-key))

(defn- toggle-active-menu-item
  [db value]
  (let [current-value (get-active-menu-item db)]
    (->> (if (= value current-value) nil value)
         (assoc db active-menu-item-key))))

(re-frame/reg-sub
  ::active-menu-item?
  :<- [path-to-db]
  (fn [db [_ item-id]]
    (-> (get-active-menu-item db)
        (= item-id))))


;; events

(re-frame/reg-event-fx
  ::handle-item-click
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [content menu-item]}]]
    (cond
      (= menu-item :voice-translate) {:dispatch [:layout/open-tool :voice-translate]}
      (some? menu-item) {:dispatch [::menu/open-component menu-item]}
      (some? content) {:db (toggle-active-menu-item db content)}
      :default {})))

(re-frame/reg-event-fx
  ::open-add-image-menu
  (fn []
    {:dispatch [::menu/open-component :image-add]}))

(re-frame/reg-event-fx
  ::open-add-character-menu
  (fn []
    {:dispatch [::menu/open-component :character-add]}))
