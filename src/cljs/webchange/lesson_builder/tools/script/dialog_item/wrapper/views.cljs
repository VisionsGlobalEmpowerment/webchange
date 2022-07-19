(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.state :as state]
    [webchange.utils.drag-and-drop :as drag-and-drop]
    [webchange.ui.index :as ui]))

(def drop-actions {"add-character-dialogue" #(re-frame/dispatch [::state/insert-new-phrase-action %])})

(defn item-wrapper
  [{:keys [actions class-name data parallel?]
    :or   {parallel? false}}]
  (let [drop-allowed? (fn [{:keys [action]}]
                        (->> (keys drop-actions)
                             (some #{action})))
        handle-drop (fn [{:keys [dragged] :as props}]
                      (let [handler (->> (get dragged :action)
                                         (get drop-actions))]
                        (handler props)))]
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
