(ns webchange.lesson-builder.blocks.toolbox.views)

(defn block-toolbox
  [{:keys [class-name]}]
  [:div {:id         "block--toolbox"
         :class-name class-name}
   "toolbox"])
