(ns webchange.admin.widgets.page.footer.views
  (:require
    [reagent.core :as r]))

(defn footer
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.widget--page--footer])))
