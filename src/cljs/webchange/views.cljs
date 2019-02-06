(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.components :refer [editor]]
    [webchange.editor.events :as ee]
    [webchange.auth.views :refer [login-form register-form]]
    [sodium.core :as na]
    [soda-ash.core :as sa]
    [reagent.core :as r]))

(defn course-switch
  [course-name]
  [na/grid {:vertical-align "middle" :columns 5 :centered? true}
   [na/grid-row {}
    [na/grid-column {}]]
   [na/grid-row {}
    [na/grid-column {}
     [na/segment {}
      [na/header {:as "h1" :content "Pick course"}]
      [na/divider {}]
      [na/button {:basic? true :content "English" :on-click #(re-frame/dispatch [::events/redirect :course :id "demo"])}]
      [na/button {:basic? true :content "Español" :on-click #(reset! course-name "reading")}]]]]
   [na/grid-row {}
    [na/grid-column {}]]])

;; course

(defn course-panel []
  (let [course-name (r/atom nil)]
    (fn []
      (if @course-name
        [course @course-name]
        [course-switch course-name]))))

;; editor
(defn main-panel-editor []
  [editor])

(defn editor-panel [course-id]
  (re-frame/dispatch [::ee/init-editor course-id])
  [editor])

;; main

(defn- panels [panel-name route-params]
  (case panel-name
    :home [course-panel]
    :register-user [register-form]
    :login [login-form]
    :course [course (:id route-params)]
    :course-editor [editor-panel (:id route-params)]
    [:div "test"]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [panels handler route-params]))

(defn home []
  [na/grid {:vertical-align "middle" :columns 5 :centered? true}
   [na/grid-row {}
    [na/grid-column {}]]
   [na/grid-row {}
    [na/grid-column {}
     [na/segment {}
      [na/header {:as "h1" :content "Welcome"}]]]]
   [na/grid-row {}
    [na/grid-column {}]]])

(defn main-panel-login []
  (let [active-page @(re-frame/subscribe [::subs/active-route])]
    (case active-page
      :register-user [register-form]
      :home [home]
      [login-form])))