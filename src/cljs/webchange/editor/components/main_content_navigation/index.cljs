(ns webchange.editor.components.main-content-navigation.index
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [webchange.editor.events :as events]
    [webchange.interpreter.events :as ie]))

(def items {
            :play-scene     {:label   "Play !!!"
                             :handler #(re-frame/dispatch [::events/set-main-content :play-scene])}
            :editor         {:label   "Editor"
                             :handler #(do (re-frame/dispatch [::ie/set-current-scene (:scene-id %)])
                                           (re-frame/dispatch [::events/set-main-content :editor]))}
            :actions        {:label   "Actions"
                             :handler #(re-frame/dispatch [::events/set-main-content :actions])}
            :scene-source   {:label   "Source"
                             :handler #(re-frame/dispatch [::events/set-main-content :scene-source])}
            :scene-versions {:label   "Versions"
                             :handler #(re-frame/dispatch [::events/open-current-scene-versions])}
            })

(defn- map-items-to-list
  [items-map]
  (map #(merge (% items-map) {:key %}) (keys items-map)))

(defn- get-menu-item
  [{:keys [key label handler]} params]
  ^{:key key} [sa/Button {:content  label
                          :on-click #(handler params)}])

(defn main-content-navigation
  [scene-id]
  [:div
   (for [item (map-items-to-list items)]
     (get-menu-item item {:scene-id scene-id}))
   ])
