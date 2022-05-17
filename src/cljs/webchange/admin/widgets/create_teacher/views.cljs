(ns webchange.admin.widgets.create-teacher.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.create-teacher.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn create-teacher
  []
  (r/create-class
    {:display-name "Create New Teacher"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [props]
       (let []
         [:div {:class-name "widget--create-teacher"}
          "Create Teacher"]))}))
