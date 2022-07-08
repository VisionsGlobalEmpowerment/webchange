(ns webchange.lesson-builder.blocks.stage.views)

(defn block-stage
  [{:keys [class-name]}]
  [:div {:id         "block--stage"
         :class-name class-name}
   "stage"])
