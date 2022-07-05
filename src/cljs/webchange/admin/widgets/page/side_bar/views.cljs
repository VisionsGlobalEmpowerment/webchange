(ns webchange.admin.widgets.page.side-bar.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.block.views :refer [block]]
    [webchange.ui.index :refer [get-class-name]]))

(def component-class-name "widget--side-bar-page--side-bar")

(defn side-bar
  [{:keys [class-name] :as props}]
  (->> (r/current-component)
       (r/children)
       (into [block (merge props
                           {:class-name (get-class-name {component-class-name true
                                                         class-name           (some? class-name)})})])))
