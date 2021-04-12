(ns webchange.book-creator.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.text-form.utils :refer [get-page-data]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.interpreter.renderer.state.editor :as interpreter]
    [webchange.state.state :as state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.utils.flipbook :as utils]))

(defn path-to-db
  [relative-path]
  (concat [:book-creator] relative-path))

(defn- get-all-children
  [{:keys [objects] :as scene-data} object-name]
  (let [{:keys [type children]} (get objects object-name)]
    (if (= type "group")
      (->> (map keyword children)
           (reduce (fn [all-children child]
                     (concat all-children [child] (get-all-children scene-data child)))
                   []))
      [])))

(defn- has-child
  [scene-data parent-name child-name]
  (let [children (get-all-children scene-data parent-name)]
    (some #{child-name} children)))

(defn- text-name->page-index
  [object-name scene-data]
  (->> (utils/get-pages-data scene-data)
       (map-indexed vector)
       (some (fn [[idx {:keys [object]}]]
               (and (has-child scene-data (keyword object) object-name)
                    idx)))))

(re-frame/reg-sub
  ::current-object-data
  (fn []
    [(re-frame/subscribe [::state-flipbook/current-stage])
     (re-frame/subscribe [::interpreter/selected-object])
     (re-frame/subscribe [::state/scene-data])])
  (fn [[current-stage selected-object-name scene-data]]
    (when (some? selected-object-name)
      (->> (text-name->page-index selected-object-name scene-data)
           (get-page-data scene-data current-stage selected-object-name)))))
