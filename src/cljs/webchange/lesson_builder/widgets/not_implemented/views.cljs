(ns webchange.lesson-builder.widgets.not-implemented.views
  (:require
    [webchange.ui.index :as ui]))

(defn not-implemented
  [{:keys [text]}]
  [:div.widget--not-implemented
   [ui/icon {:icon       "close"
             :class-name "not-implemented--icon"}]
   "Not Implemented"
   (when (some? text)
     (str ": " text))])
