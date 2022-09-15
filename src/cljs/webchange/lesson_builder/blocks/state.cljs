(ns webchange.lesson-builder.blocks.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.menu.state :as menu-state]
    [webchange.lesson-builder.blocks.toolbox.state :as toolbox-state]
    [webchange.lesson-builder.tools.background-image.index :as background-image]
    [webchange.lesson-builder.tools.flipbook-add-page.index :as flipbook-add-page]
    [webchange.lesson-builder.tools.question-form.index :as question-form]
    [webchange.lesson-builder.tools.voice-translate.index :as voice-translate]))

(def path-to-db :lesson-builder/layout)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; focused blocks

(def focused-blocks-key :focused-blocks)

(defn- set-focused-blocks
  [db value]
  (assoc db focused-blocks-key value))

(re-frame/reg-sub
  ::focused-blocks
  :<- [path-to-db]
  #(get % focused-blocks-key #{}))

;; general

(def states
  {:default                 {:toolbox :default
                             :menu    :default
                             :focus   #{}}
   :change-background-image {:toolbox :background-image
                             :focus   #{:toolbox :stage}}
   :edit-question           {:toolbox :question-options
                             :menu    :question-params}})

(re-frame/reg-event-fx
  ::set-state
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ state-id]]
    (let [state (get states state-id)]
      {:db         (cond-> db
                           (contains? state :focus) (set-focused-blocks (:focus state)))
       :dispatch-n (cond-> []
                           (contains? state :menu) (conj [::menu-state/open-component (:menu state) {:on-back [:layout/reset-state]}])
                           (contains? state :toolbox) (conj [::toolbox-state/set-current-widget (:toolbox state)]))})))

(re-frame/reg-event-fx
  :layout/reset-state
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-state :default]}))

(def tools {:background-image background-image/data
            :add-page         flipbook-add-page/data
            :question-form    question-form/data
            :voice-translate  voice-translate/data})

(re-frame/reg-event-fx
  ::open-tool
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ tool-id tool-props]]
    (let [tool-data (get tools tool-id)
          destroy-event (when (contains? tool-data :reset)
                          (conj (:reset tool-data) tool-props))]
      {:db         (cond-> db
                           (contains? tool-data :focus) (set-focused-blocks (:focus tool-data)))
       :dispatch-n (cond-> []
                           (contains? tool-data :menu)
                           (conj [::menu-state/open-component tool-id {:on-back (cond-> [[:layout/reset-state]]
                                                                                        (some? destroy-event) (conj destroy-event))}])

                           (contains? tool-data :toolbox)
                           (conj [::toolbox-state/set-current-widget tool-id])

                           (contains? tool-data :init)
                           (concat [(conj (:init tool-data) (merge tool-props
                                                                   {:reset [:layout/reset-state]}))]))})))
