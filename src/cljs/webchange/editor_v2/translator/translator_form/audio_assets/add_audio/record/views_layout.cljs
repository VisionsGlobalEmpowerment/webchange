(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-layout
  (:require
    [reagent.core :as r]))

(defn- get-styles
  []
  (let [panels-height 58]
    {:title   {:height (str panels-height "px")}
     :actions {:height (str panels-height "px")}}))

(defn panel-layout
  [{:keys [actions]}]
  (let [this (r/current-component)
        styles (get-styles)]
    [:div
     [:div {:style (:title styles)}]
     (into [:div]
           (r/children this))
     [:div {:style (:actions styles)}
      actions]]))
