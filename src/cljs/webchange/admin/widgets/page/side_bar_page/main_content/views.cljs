(ns webchange.admin.widgets.page.side-bar-page.main-content.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.side-bar-page.block.views :refer [block]]
    [webchange.ui.index :refer [get-class-name]]))

(defn main-content
  [{:keys [class-name] :as props}]
  (->> (r/current-component)
       (r/children)
       (into [block (merge props
                           {:class-name (get-class-name {"widget--side-bar-page--main-content" true
                                                         class-name                            (some? class-name)})

                            :class-name-content "widget--side-bar-page--main-content--content"})])))
