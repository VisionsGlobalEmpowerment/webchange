(ns webchange.ui.pages.index
  (:require
    [webchange.ui.pages.buttons.views :as buttons]
    [webchange.ui.pages.cards.views :as cards]
    [webchange.ui.pages.chips.views :as chips]
    [webchange.ui.pages.colors.views :as colors]
    [webchange.ui.pages.complete-progresses.views :as complete-progresses]
    [webchange.ui.pages.forms.views :as forms]
    [webchange.ui.pages.headers.views :as headers]
    [webchange.ui.pages.icons.views :as icons]
    [webchange.ui.pages.images.views :as images]
    [webchange.ui.pages.lists.views :as lists]
    [webchange.ui.pages.switches.views :as switches]
    [webchange.ui.pages.tabs.views :as tabs]))

(defn- page-404
  []
  [:div "404"])

(def pages {:buttons             buttons/page
            :cards               cards/page
            :chips               chips/page
            :colors              colors/page
            :complete-progresses complete-progresses/page
            :forms               forms/page
            :headers             headers/page
            :icons               icons/page
            :images              images/page
            :lists               lists/page
            :switches            switches/page
            :tabs                tabs/page
            :404                 page-404})
