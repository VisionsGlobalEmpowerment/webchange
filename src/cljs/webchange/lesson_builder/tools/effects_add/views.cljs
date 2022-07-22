(ns webchange.lesson-builder.tools.effects-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.fold.views :refer [fold]]
    [webchange.lesson-builder.tools.effects-add.character-emotions.views :refer [character-emotions]]
    [webchange.lesson-builder.tools.effects-add.character-movements.views :refer [character-movements]]
    [webchange.lesson-builder.tools.effects-add.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]
    [webchange.ui.index :as ui]))

(def effect-components {"character-emotions"  character-emotions
                        "character-movements" character-movements})

(defn- effects-list-item
  [{:keys [action-type text]}]
  [draggable {:text   text
              :action "add-effect"
              :data   {:action-type action-type}}])

(defn- effects-list
  [{:keys [effects]}]
  [draggable-list {}
   (for [{:keys [action-type] :as effect-data} effects]
     ^{:key action-type}
     [effects-list-item effect-data])])

(defn- effects-group
  [{:keys [component title effects]}]
  [fold {:title              title
         :expanded?          false
         :height-restricted? false}
   (cond
     (some? effects) [effects-list {:effects effects}]
     (some? component) [(get effect-components component)])])

(defn effects-add
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [available-effects @(re-frame/subscribe [::state/available-effects])
          animations-loading? @(re-frame/subscribe [::state/animations-loading?])]
      [:div.widget--effects-add
       (when animations-loading?
         [ui/loading-overlay])
       (for [{:keys [text] :as effects-group-data} available-effects]
         ^{:key text}
         [effects-group effects-group-data])])))
