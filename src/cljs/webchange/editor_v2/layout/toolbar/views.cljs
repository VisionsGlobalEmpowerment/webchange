(ns webchange.editor-v2.layout.toolbar.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.layout.logo.views :refer [logo]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:toolbar {:background-color (get-in-theme [:palette :background :darken])}})

(defn toolbar
  [{:keys [title]}]
  (let [styles (get-styles)]
    [ui/app-bar {:position "fixed"}
     [ui/toolbar {:style (:toolbar styles)}
      [logo]
      [ui/typography {:variant "h2"}
       title]]]))
