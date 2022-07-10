(ns webchange.admin.widgets.page.footer.views
  (:require
    [reagent.core :as r]))

(def component-class-name "widget--page--footer")

(defn footer
  []
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name component-class-name}])))
