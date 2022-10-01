(ns webchange.lesson-builder.layout.script.views
  (:require
    [webchange.lesson-builder.tools.script.views :refer [script]]))

(defn block-script
  [{:keys [class-name]}]
  [:div {:id         "block--script"
         :class-name class-name}
   [script]])
