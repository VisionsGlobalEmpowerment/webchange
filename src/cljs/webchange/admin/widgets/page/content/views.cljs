(ns webchange.admin.widgets.page.content.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.block.views :refer [block]]
    [webchange.ui.index :refer [get-class-name]]))

(def component-class-name "widget--side-bar-page--main-content")

(defn content
  [{:keys [class-name transparent?] :as props}]
  (->> (r/current-component)
       (r/children)
       (into [block (merge props
                           {:class-name         (get-class-name {component-class-name true
                                                                 (str component-class-name "--transparent") transparent?
                                                                 class-name           (some? class-name)})

                            :class-name-content "widget--side-bar-page--main-content--content"})])))
