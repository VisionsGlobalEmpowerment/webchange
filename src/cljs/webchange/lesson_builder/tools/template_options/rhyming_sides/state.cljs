(ns webchange.lesson-builder.tools.template-options.rhyming-sides.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.menu.state :as menu]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (js/console.log (get-in db [:form :balls]))
    {:db (update-in db [:form :balls] #(into [] %))}))

(re-frame/reg-event-fx
  ::pick-side
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ side]]
    (let [side-name (case side
                      :left "Left Side"
                      :right "Right Side")]
      {:db       (-> db
                     (assoc :side side))
       :dispatch [::menu/open-component :rhyming-side {:title side-name}]})))

(re-frame/reg-sub
  ::word
  :<- [path-to-db]
  (fn [db]
    (let [side (get db :side)]
      (get-in db [:form side]))))

(re-frame/reg-event-fx
  ::set-word
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [side (get db :side)]
      {:db (assoc-in db [:form side] value)})))

(re-frame/reg-sub
  ::answers
  :<- [path-to-db]
  (fn [db]
    (let [side-name (-> (get db :side) name)
          balls (get-in db [:form :balls])]
      (->> balls
           (map-indexed vector)
           (map #(assoc (second %) :idx (first %)))
           (remove :deleted)
           (filter #(= (:side %) side-name))))))

(comment
  (concat [{:side 1}
           {:side 2}]
          [{:side 3}])
  (-> {:form {:balls [{:side 1}
                      {:side 2}]}}
      (update-in [:form :balls] concat [{:side 3}])))

(re-frame/reg-event-fx
  ::add-answer
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [side-name (-> (get db :side) name)]
      {:db (-> db
               (update-in [:form :balls] concat [{:side side-name}])
               (update-in [:form :balls] #(into [] %)))})))

(defn- remove-by-index
  [list index]
  (vec (concat (subvec list 0 index)
               (subvec list (inc index)))))

(re-frame/reg-event-fx
  ::delete-answer
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    (let [answer (get-in db [:form :balls idx])]
      (if (:object answer)
        {:db (assoc-in db [:form :balls idx :deleted] true)}
        {:db (update-in db [:form :balls] remove-by-index idx)}))))

(re-frame/reg-event-fx
  ::set-answer-word
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    {:db (assoc-in db [:form :balls idx :text] value)}))

(re-frame/reg-event-fx
  ::set-answer-img
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx value]]
    {:db (assoc-in db [:form :balls idx :img] value)}))

