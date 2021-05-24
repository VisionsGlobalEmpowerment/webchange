(ns webchange.game-changer.steps.select-background.views
  (:require
    [reagent.core :as r]
    [webchange.common.background-selector.views-background-selector :refer [background-selector]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]))

(defn select-background
  [{:keys [data]}]
  (r/with-let [background-data (connect-data data [:background])]
    (let [{:keys [scene-slug course-slug]} (get @data :activity {})]
      [background-selector {:scene-slug      scene-slug
                            :course-slug     course-slug
                            :on-change       #(reset! background-data %)
                            :change-on-init? true}])))
