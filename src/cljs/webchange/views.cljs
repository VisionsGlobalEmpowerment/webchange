(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.components :refer [editor]]
    [sodium.core :as na]
    [reagent.core :as r]))

(defn course-switch
  [course-name]
  [na/segment {}
   [na/button {:basic? true :content "English" :on-click #(reset! course-name "demo")}]
   [na/button {:basic? true :content "Español" :on-click #(reset! course-name "reading")}]])

(defn main-panel []
  (let [course-name (r/atom nil)]
    (fn []
      (if @course-name
        [course @course-name]
        [course-switch course-name]))))

(defn main-panel-editor []
  [editor])