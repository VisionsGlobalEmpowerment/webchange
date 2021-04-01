(ns webchange.state.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.state :refer [path-to-db]]
    [webchange.state.state :as state]
    [webchange.utils.flipbook :as flipbook-utils]))

(defn current-stage
  [db]
  (get-in db (path-to-db [:current-stage]) 0))

(re-frame/reg-sub
  ::current-stage
  current-stage)

(re-frame/reg-sub
  ::current-pages
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::current-stage])])
  (fn [[scene-data current-stage-idx] [_ page-side]]
    (let [current-stage (flipbook-utils/get-stage-data scene-data current-stage-idx)
          pages (flipbook-utils/get-pages-data scene-data)
          current-pages-data (->> (:pages-idx current-stage)
                                  (map (fn [page-idx]
                                         (when (some? page-idx)
                                           (nth pages page-idx)))))]
      (if (some? page-side)
        (case page-side
          :left (first current-pages-data)
          :right (second current-pages-data))
        current-pages-data))))
