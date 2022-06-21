(ns webchange.admin.widgets.page.side-bar-page.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.side-bar-page.block.views :as block-views]
    [webchange.admin.widgets.page.side-bar-page.main-content.views :as main-content-views]
    [webchange.admin.widgets.page.side-bar-page.side-bar.views :as side-bar-views]

    [webchange.ui.index :refer [get-class-name]]))

(def block block-views/block)
(def main-content main-content-views/main-content)
(def side-bar side-bar-views/side-bar)

(defn side-bar-page
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name {"widget--side-bar-page" true
                                                 class-name              (some? class-name)})}])))
