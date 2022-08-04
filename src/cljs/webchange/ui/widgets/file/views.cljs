(ns webchange.ui.widgets.file.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui.widgets.file.state :as state]
    [webchange.utils.uid :refer [get-uid]]))

(defn file
  []
  (let [uid (get-uid)]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (re-frame/dispatch [::state/init uid (r/props this)]))

       :component-will-unmount
       (fn [this]
         (re-frame/dispatch [::state/reset uid (r/props this)]))

       :reagent-render
       (fn [{:keys [class-name] :as props}]
         (let [loading? @(re-frame/subscribe [::state/loading? uid])
               handle-change #(re-frame/dispatch [::state/upload-audio uid %])]
           [ui/file (merge props
                           {:loading?   loading?
                            :on-change  handle-change
                            :class-name (ui/get-class-name {"bbs-widget--file" true
                                                            class-name         (some? class-name)})})]))})))
