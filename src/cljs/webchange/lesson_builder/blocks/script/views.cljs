(ns webchange.lesson-builder.blocks.script.views)

(defn block-script
  [{:keys [class-name]}]
  [:div {:id         "block--script"
         :class-name class-name}
   "script"])
