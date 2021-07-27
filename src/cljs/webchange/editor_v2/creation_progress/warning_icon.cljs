(ns webchange.editor-v2.creation-progress.warning-icon
  (:require
    [cljs-react-material-ui.icons :as ic]
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.utils.deep-merge :refer [deep-merge]]))

(defn- get-styles
  []
  {:main {:color (get-in-theme [:palette :warning :default])}})

(defn warning-icon
  [{:keys [styles] :or {styles {}}}]
  (let [styles (-> (get-styles) (deep-merge styles))]
    [ic/warning {:style (:main styles)}]))
