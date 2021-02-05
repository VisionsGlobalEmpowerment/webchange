(ns webchange.editor-v2.layout.components.object-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.editor-v2.layout.utils :refer [get-activity-type]]
    [webchange.state.state :as state]))

(defn- scene-data->objects-list
  [scene-data]
  (->> (:objects scene-data)
       (filter (fn [[_ object-data]] (= "text" (:type object-data))))))

(defn- page-idx->text-object
  [scene-data page-idx flipbook-name]
  (when (some? page-idx)
    (let [pages (get-in scene-data [:objects (keyword flipbook-name) :pages])
          page (nth pages page-idx)
          text-name (-> page (get :text) (keyword))
          text-data (get-in scene-data [:objects text-name])]
      (when (and (some? text-name)
                 (some? text-data))
        [text-name text-data]))))

(defn- flipbook-data->objects-list
  [scene-data current-stage]
  (if (some? current-stage)
    (let [{:keys [stages flipbook-name]} (get scene-data :metadata {})
          stage (nth stages current-stage)
          [left-page-idx right-page-idx] (:pages-idx stage)]
      (->> (cond-> []
                   (some? left-page-idx) (conj (page-idx->text-object scene-data left-page-idx flipbook-name))
                   (some? right-page-idx) (conj (page-idx->text-object scene-data right-page-idx flipbook-name)))
           (remove nil?)))
    []))

(re-frame/reg-sub
  ::text-objects
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::stage/current-stage])])
  (fn [[scene-data current-stage]]
    (-> (get-activity-type scene-data)
        (case
          "flipbook" (flipbook-data->objects-list scene-data current-stage)
          (scene-data->objects-list scene-data)))))
