(ns webchange.admin.pages.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.dashboard.state :as state]
    [webchange.admin.pages.dashboard.views-world-map :refer [world-map]]
    [webchange.ui.index :as ui]))

(defn- overview
  []
  [ui/panel {:title      "Blue Brick School Overview"
             :icon       "info"
             :class-name "overview-panel"}
   "Overview Data"])

(defn- statistics
  []
  [ui/panel {:title      "Statistics"
             :icon       "statistics"
             :class-name "statistics-panel"}
   "Statistics Data"])

(defn- quick-links
  []
  (let [{:keys [accounts books]} @(re-frame/subscribe [::state/statistics])
        handle-accounts-click #(re-frame/dispatch [::state/open-accounts-page])
        handle-books-click #(re-frame/dispatch [::state/open-books-page])
        handle-create-book-click #(re-frame/dispatch [::state/open-create-book-page])]
    [ui/panel {:title              "Quick Links"
               :color              "blue-1"
               :class-name-content "quick-links-panel"}
     [ui/tab {:icon     "accounts"
              :counter  accounts
              :on-click handle-accounts-click}
      "Accounts"]
     [ui/tab {:icon     "book"
              :counter  books
              :on-click handle-books-click}
      "Books"]
     [ui/tab {:icon     "create"
              :on-click handle-create-book-click
              :action   "plus"}
      "Create a Book"]]))

(defn- countries
  []
  [ui/panel {:title              "Countries"
             :class-name         "countries-panel"
             :class-name-content "countries-panel-content"}
   [world-map {:title     "Schools"
               :countries ["us" "ni" "in" "gt"]}]
   [quick-links]])

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let []
      [:div.page--dashboard
       [overview]
       [statistics]
       [countries]])))
