(ns webchange.lesson-builder.tools.script.track-selector.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.script.block.views :refer [block]]
    [webchange.lesson-builder.tools.script.track-selector.state :as state]
    [webchange.ui.index :as ui]))

(defn- current-value
  [{:keys [on-click open?]}]
  (let [current-track @(re-frame/subscribe [::state/current-track])]
    [:div {:class-name (ui/get-class-name {"track-selector--current-value" true
                                           "track-selector--current-value--open" open?})
           :on-click   on-click}
     [:span current-track]
     [ui/icon {:icon "drop"}]]))

(defn- options-list-item
  [{:keys [on-click text value]}]
  (let [handle-click #(on-click value)]
    [:div {:class-name "track-selector--options-list-item"
           :on-click   handle-click}
     text]))

(defn- options-list
  [{:keys [on-option-click open?]}]
  (let [track-options @(re-frame/subscribe [::state/track-options])]
    [:div {:class-name (ui/get-class-name {"track-selector--options-list"       true
                                           "track-selector--options-list--open" open?})}
     (for [{:keys [value] :as option} track-options]
       ^{:key value}
       [options-list-item (assoc option :on-click on-option-click)])]))

(defn track-selector
  []
  (r/with-let [_ (re-frame/dispatch [::state/init])
               open? (r/atom false)
               toggle-open #(swap! open? not)
               handle-option-click #(do (reset! open? false)
                                        (re-frame/dispatch [::state/set-current-track %]))]
    [:div {:class-name (ui/get-class-name {"track-selector"       true
                                           "track-selector--open" @open?})}
     [current-value {:open?    @open?
                     :on-click toggle-open}]
     [options-list {:open?           @open?
                    :on-option-click handle-option-click}]]))
