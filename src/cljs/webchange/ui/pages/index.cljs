(ns webchange.ui.pages.index
  (:require
    [webchange.ui.pages.buttons.views :as buttons]
    [webchange.ui.pages.cards.views :as cards]
    [webchange.ui.pages.colors.views :as colors]
    [webchange.ui.pages.icons.views :as icons]
    [webchange.ui.pages.tabs.views :as tabs]))

(defn- page-404
  []
  [:div "404"])

(def pages {:buttons buttons/page
            :cards   cards/page
            :colors  colors/page
            :icons   icons/page
            :tabs    tabs/page
            :404     page-404})
