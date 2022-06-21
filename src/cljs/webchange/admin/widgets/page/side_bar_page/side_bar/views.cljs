(ns webchange.admin.widgets.page.side-bar-page.side-bar.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.side-bar-page.block.views :refer [block]]
    [webchange.ui.index :refer [get-class-name]]))

(defn side-bar
  [{:keys [class-name] :as props}]
  (->> (r/current-component)
       (r/children)
       (into [block (merge props
                           {:class-name (get-class-name {"widget--side-bar-page--side-bar" true
                                                         class-name                        (some? class-name)})})])))
