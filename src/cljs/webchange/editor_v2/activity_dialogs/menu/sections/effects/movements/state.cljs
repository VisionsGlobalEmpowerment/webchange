(ns webchange.editor-v2.activity-dialogs.menu.sections.effects.movements.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.common.character-movements :refer [get-action-text]]
    [webchange.editor-v2.activity-dialogs.menu.sections.effects.state :as parent-state]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-translator]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:movements])
       (parent-state/path-to-db)))


;; Character

(def current-character-path (path-to-db [:current-character]))

(re-frame/reg-sub
  ::current-character
  (fn [db]
    (get-in db current-character-path)))

(re-frame/reg-event-fx
  ::set-current-character
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-character-path value)}))

(re-frame/reg-sub
  ::available-characters-options
  (fn []
    [(re-frame/subscribe [::state-translator/available-animation-targets])])
  (fn [[available-animation-targets]]
    (->> available-animation-targets
         (map (fn [target]
                {:text  target
                 :value target})))))

;; Movements

(def current-movement-path (path-to-db [:current-movement]))

(re-frame/reg-sub
  ::current-movement
  (fn [db]
    (get-in db current-movement-path)))

(re-frame/reg-event-fx
  ::set-current-movement
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc-in db current-movement-path value)
     :dispatch [::reset-current-target]}))

(re-frame/reg-sub
  ::available-movements-options
  (fn []
    [{:text  "Go to"
      :value "go-to"}
     {:text  "Pick up item"
      :value "pick-up"}
     {:text  "Give item"
      :value "give"}]))

;; Target

(def current-target-path (path-to-db [:current-target]))

(re-frame/reg-sub
  ::current-target
  (fn [db]
    (get-in db current-target-path)))

(re-frame/reg-event-fx
  ::set-current-target
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-target-path value)}))

(re-frame/reg-event-fx
  ::reset-current-target
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-current-target nil]}))

(re-frame/reg-sub
  ::available-targets-options
  (fn []
    [(re-frame/subscribe [::current-character])
     (re-frame/subscribe [::current-movement])
     (re-frame/subscribe [::state-translator/objects-data])])
  (fn [[current-character current-movement objects-data]]
    (if (and (some? current-character)
             (some? current-movement))
      (->> objects-data
           (filter (fn [[object-name {:keys [type name]}]]
                     (case current-movement
                       "go-to" (and (not= (clojure.core/name object-name) current-character)
                                    (not= type "background")
                                    (not= type "layered-background"))
                       "pick-up" (= type "image")
                       "give" (and (not= (clojure.core/name object-name) current-character)
                                   (= type "animation")
                                   (= name "child")))))
           (map (fn [[name {:keys [scene-name]}]]
                  {:text  (-> (or scene-name name)
                              (clojure.core/name))
                   :value (clojure.core/name name)})))
      [])))

;; Options

(re-frame/reg-sub
  ::dnd-options
  (fn []
    [(re-frame/subscribe [::current-character])
     (re-frame/subscribe [::current-target])
     (re-frame/subscribe [::current-movement])
     (re-frame/subscribe [::state-translator/objects-data])])
  (fn [[character target movement objects-data]]
    (if (and (some? movement)
             (some? character)
             (some? target))
      [{:text      (get-action-text movement {:character-name character
                                              :character-data (->> character keyword (get objects-data))
                                              :target-name    target
                                              :target-data    (->> target keyword (get objects-data))})
        :movement  movement
        :character character
        :target    target}]
      [])))
