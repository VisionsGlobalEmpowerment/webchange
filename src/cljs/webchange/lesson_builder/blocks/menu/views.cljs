(ns webchange.lesson-builder.blocks.menu.views)

(defn block-menu
  [{:keys [class-name]}]
  [:div {:id         "block--menu"
         :class-name class-name}
   "menu"])
