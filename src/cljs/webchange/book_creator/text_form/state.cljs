(ns webchange.book-creator.text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.text-form.utils :refer [get-page-data]]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.interpreter.renderer.state.editor :as interpreter]
    [webchange.state.state :as state]
    [webchange.utils.flipbook :as utils]))

(defn- text-name->page-index
  [object-name scene-data]
  (->> (utils/get-pages-data scene-data)
       (map-indexed vector)
       (some (fn [[idx {:keys [text]}]]
               (and (= (keyword text) object-name)
                    idx)))))

(re-frame/reg-sub
  ::current-text-object-data
  (fn []
    [(re-frame/subscribe [::stage/current-stage])
     (re-frame/subscribe [::interpreter/selected-object])
     (re-frame/subscribe [::state/scene-data])])
  (fn [[current-stage selected-object-name scene-data]]
    (when (some? selected-object-name)
      (->> (text-name->page-index selected-object-name scene-data)
           (get-page-data scene-data current-stage)))))
