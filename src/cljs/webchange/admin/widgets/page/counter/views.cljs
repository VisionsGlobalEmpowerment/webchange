(ns webchange.admin.widgets.page.counter.views
  (:require
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn counter
  [{:keys [class-name data]}]
  [:div {:class-name (get-class-name {"widget--counter"                              true
                                      (str "widget--counter--columns-" (count data)) true
                                      class-name                                     (some? class-name)})}
   (for [[idx card-data] (map-indexed vector data)]
     ^{:key idx}
     [ui/card (merge {:type            "vertical"
                      :background      "yellow-2"
                      :icon-background "blue-1"}
                     card-data)])])
