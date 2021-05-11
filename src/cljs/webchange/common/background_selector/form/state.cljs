(ns webchange.common.background-selector.form.state
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [re-frame.core :as re-frame]
    [webchange.common.background-selector.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:background-form])
       (parent-state/path-to-db)
       (vec)))

(def available-backgrounds-path (path-to-db [:available-backgrounds]))
(def background-type-path (path-to-db [:current-background-type]))
(def background-values-path (path-to-db [:current-background-values]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]}]
    {:dispatch [::warehouse/load-backgrounds {:on-success [::set-available-backgrounds]}]}))

(re-frame/reg-event-fx
  ::set-available-backgrounds
  (fn [{:keys [db]} [_ available-backgrounds]]
    {:db (assoc-in db available-backgrounds-path available-backgrounds)}))

(re-frame/reg-sub
  ::available-backgrounds
  (fn [db]
    (get-in db available-backgrounds-path [])))

(re-frame/reg-sub
  ::background-options
  (fn []
    [(re-frame/subscribe [::available-backgrounds])])
  (fn [[available-backgrounds] [_ type]]
    (let [current-type (clojure.core/name type)]
      (->> available-backgrounds
           (filter (fn [{:keys [type]}]
                     (= type current-type)))
           (map (fn [{:keys [path thumbnail-path]}]
                  {:value     path
                   :thumbnail thumbnail-path}))))))

;; Background Types

(re-frame/reg-sub
  ::available-background-types
  (fn [_]
    [{:text  "Single image"
      :value "background"}
     {:text  "Layered background"
      :value "layered-background"}]))
