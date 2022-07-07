(ns webchange.lesson-builder.pages.create.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.page.views :as page]
    [webchange.lesson-builder.pages.create.state :as state]
    [webchange.ui.index :as ui]))

(defn- create-activity-card
  []
  [:div.create-activity-card
   [ui/card {:class-name "create-activity-card"
             :type            "vertical"
             :background      "green-2"
             :icon-background "blue-1"
             :icon "games"
             :actions [{:text     "Create Activity"
                        :on-click #(re-frame/dispatch [::state/create-activity])}]}]])

(defn- create-book-card
  []
  [:div.create-book-card
   [ui/card {:class-name "create-book-card"
             :type            "vertical"
             :background      "green-2"
             :icon-background "blue-1"
             :icon "book"
             :actions [{:text     "Create Book"
                        :on-click #(re-frame/dispatch [::state/create-book])}]
             }]])

(defn page
  []
  [page/single-page {:class-name        "page--create-index"
                     :header            {:title    "Create"
                                         :icon     "create"
                                         :icon-color "blue-2"
                                         :controls [[:div.header-text "What do you want to create?"]]}
                     :background-image? true}
   [:div.wizard-content
    [:div.wizard-cards
     [create-activity-card]
     [create-book-card]]]])
