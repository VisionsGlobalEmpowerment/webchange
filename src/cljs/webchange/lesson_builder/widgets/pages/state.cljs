(ns webchange.lesson-builder.widgets.pages.state
  (:require
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.state-flipbook :as flipbook]
    [webchange.utils.flipbook :as flipbook-utils]))

(def path-to-db :widgets/activity-pages)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; shoe technical pages?

(def show-tech-pages-key :show-tech-pages?)

(defn- get-show-tech-pages
  [db]
  (get db show-tech-pages-key true))

(re-frame/reg-sub
  ::show-tech-pages?
  :<- [path-to-db]
  get-show-tech-pages)

(re-frame/reg-event-fx
  ::set-show-tech-pages
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db show-tech-pages-key value)}))

(defn- page-idx->data
  [activity-data remove-generated? page-idx]
  (when (some? page-idx)
    (let [{:keys [generated?]} (flipbook-utils/get-page-data activity-data page-idx)]
      (when-not (and generated? remove-generated?)
        {:title (str "Page " page-idx)}))))

(re-frame/reg-sub
  ::stages
  :<- [::state/activity-data]
  :<- [::flipbook/current-stage]
  :<- [::show-tech-pages?]
  (fn [[activity-data current-stage-idx show-tech-pages?]]
    (->> (flipbook-utils/get-stages-data activity-data)
         (map (fn [{:keys [idx name pages-idx]}]
                {:id         (->> pages-idx
                                  (map #(or % "*"))
                                  (join "-")
                                  (str idx "-"))
                 :idx        idx
                 :title      name
                 :selected?  (= idx current-stage-idx)
                 :left-page  (->> pages-idx (first) (page-idx->data activity-data (not show-tech-pages?)))
                 :right-page (->> pages-idx (last) (page-idx->data activity-data (not show-tech-pages?)))}))
         (filter (fn [{:keys [left-page right-page]}]
                   (or left-page right-page))))))

(re-frame/reg-event-fx
  ::select-stage
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    (let [show-tech-pages? (get-show-tech-pages db)]
      {:dispatch [::flipbook/show-flipbook-stage idx {:hide-generated-pages? (not show-tech-pages?)}]})))
