(ns webchange.ui-framework.layout.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:ui-framework])))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ content-el]]
    {:dispatch [::set-layout-state {:content-el content-el}]}))

;; State

(def layout-state-path (path-to-db [:layout-state]))

(defn get-layout-state
  [db]
  (get-in db layout-state-path))

(re-frame/reg-event-fx
  ::set-layout-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db layout-state-path data)}))

(defn get-content-el
  [db]
  (-> (get-layout-state db)
      (get :content-el)))

;; Scroll

(re-frame/reg-event-fx
  ::scroll-content
  (fn [{:keys [db]} [_ position]]
    (let [content-el (get-content-el db)]
      (when (some? content-el)
        {:scroll-element {:el       content-el
                          :position position}}))))

(re-frame.core/reg-fx
  :scroll-element
  (fn [{:keys [el position]}]
    (let [{:keys [x y] :or {x 0 y 0}} position]
      (.scrollTo el x y))))
