(ns webchange.parent.widgets.layout.views
  (:require
    [reagent.core :as r]
    [webchange.parent.widgets.layout.banner.views :refer [banner]]
    [webchange.parent.widgets.layout.footer.views :refer [footer]]
    [webchange.parent.widgets.layout.header.views :refer [header]]))

(defn layout
  []
  [:div.parent--layout
   [header]
   [banner]
   (->> (r/current-component)
        (r/children)
        (into [:div.parent--layout--content]))
   [footer]])
