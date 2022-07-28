(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.views
  (:require
    [camel-snake-kebab.extras :refer [transform-keys]]
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.dnd-actions :refer [drop-actions]]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.tools.script.dialog-item.state :as dialog-item-state]
    [webchange.utils.drag-and-drop :as drag-and-drop]
    [webchange.ui.index :as ui]))

(defn item-wrapper
  [{:keys [action-path actions class-name data parallel?]
    :or   {data      {}
           parallel? false}}]
  (let [data (merge {:path action-path}
                    data)
        drop-allowed? (fn [{:keys [action]}]
                        (->> (keys drop-actions)
                             (some #{action})))
        selected? @(re-frame/subscribe [::dialog-item-state/action-selected? action-path])
        handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::script-state/set-selected-action action-path]))
        handle-drop (fn [{:keys [dragged] :as props}]
                      (let [handler (->> (get dragged :action)
                                         (get drop-actions))]
                        (->> props
                             (transform-keys ->kebab-case-keyword)
                             (handler))))]
    [drag-and-drop/draggable {:class-name    (ui/get-class-name {"component--item-wrapper"           true
                                                                 "component--item-wrapper--parallel" parallel?
                                                                 "component--item-wrapper--selected" selected?})
                              :data          (assoc data :action "move-dialog-action")
                              :on-click      handle-click
                              :on-drop       handle-drop
                              :drag-control  ".component--item-wrapper--move-icon"
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
