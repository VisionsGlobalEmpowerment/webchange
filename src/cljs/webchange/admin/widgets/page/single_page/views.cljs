(ns webchange.admin.widgets.page.single-page.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.page.header.views :as header-views]
    [webchange.ui.index :refer [get-class-name]]))

(defn single-page
  [{:keys [background-image? class-name class-name-content form-container? header]}]
  [:div {:class-name (get-class-name {"widget--single-page"                        true
                                      "widget--single-page--with-background-image" background-image?
                                      "widget--single-page--form-container"        form-container?
                                      class-name                                   (some? class-name)})}
   (when (some? header)
     [header-views/header header])
   (->> (r/current-component)
        (r/children)
        (into [:div {:class-name (get-class-name {"widget--single-page--content" true
                                                  class-name-content             (some? class-name-content)})}]))])
