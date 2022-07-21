(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.views
  (:require
    [camel-snake-kebab.extras :refer [transform-keys]]
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.dnd-actions :refer [drop-actions]]
    [webchange.utils.drag-and-drop :as drag-and-drop]
    [webchange.ui.index :as ui]))

(defn item-wrapper
  [{:keys [actions class-name data parallel?]
    :or   {parallel? false}}]
  (let [drop-allowed? (fn [{:keys [action]}]
                        (and (some? data)
                             (->> (keys drop-actions)
                                  (some #{action}))))
        handle-drop (fn [{:keys [dragged] :as props}]
                      (let [handler (->> (get dragged :action)
                                         (get drop-actions))]
                        (->> props
                             (transform-keys ->kebab-case-keyword)
                             (handler))))]
    [drag-and-drop/draggable {:class-name    (ui/get-class-name {"component--item-wrapper"           true
                                                                 "component--item-wrapper--parallel" parallel?})
                              :data          data
                              :on-drop       handle-drop
                              :drop-allowed? drop-allowed?}
     [ui/icon {:icon       "move"
               :class-name "component--item-wrapper--move-icon"}]
     (->> (r/current-component)
          (r/children)
          (into [:div {:class-name (ui/get-class-name {"component--item-wrapper--content" true
                                                       class-name                         (some? class-name)})}]))
     (when-not (empty? actions)
       [:div.component--item-wrapper--actions
        (for [[idx action] (map-indexed vector actions)]
          ^{:key idx}
          [ui/button (merge {:class-name "component--item-wrapper--action"}
                            action)])])]))
