(ns webchange.lesson-builder.tools.script.block.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn block
  [{:keys [class-name class-name--content footer on-click title collapse-state]}]
  (r/with-let [expanded? (r/atom (= collapse-state :expanded))
               toggle-expanded #(swap! expanded? not)]
    (let [block-content-hidden? (and collapse-state (not @expanded?))
          show-footer? (and (some? footer) (not (and collapse-state (not @expanded?))))]
      [:div (cond-> {:class-name (ui/get-class-name {"component--block" true
                                                     class-name         (some? class-name)})}
                    (fn? on-click) (assoc :on-click on-click))
       (when (some? title)
         [:div {:class-name "component--header"
                :on-click toggle-expanded}
          (when collapse-state
            [ui/icon {:color "grey-4"
                      :icon       (if @expanded? "caret-up" "caret-down")
                      :class-name "fold--icon"}])
          title])
       (->> (r/current-component)
            (r/children)
            (into [:div {:class-name (ui/get-class-name {"block--content"    true
                                                         "block--content-hidden" block-content-hidden?
                                                         class-name--content (some? class-name--content)})}]))
       (when show-footer?
         footer)])))
