(ns webchange.views
  (:require
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]
   [webchange.interpreter.components :refer [course]]
   [webchange.editor.components :refer [course] :rename {course editor}]
   [webchange.editor.events :as ee]
   [sodium.core :as na]
   ))

(defn main-panel []
  [course "test-course"])

(defn main-panel-editor []
  [:div {:class-name "ui container"}
   [:h2 {:class-name "ui dividing header"} "Editor"]
   [editor]
   [na/button {:content "Play" :on-click #(re-frame/dispatch [::ee/set-screen :play-scene])}]
   [na/button {:content "Editor" :on-click #(re-frame/dispatch [::ee/set-screen :editor])}]])