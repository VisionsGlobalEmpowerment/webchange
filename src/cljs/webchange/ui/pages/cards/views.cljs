(ns webchange.ui.pages.cards.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def horizontal-variants (->> [{:icon    "school"
                                :text    "Schools"
                                :counter 24
                                :actions [{:text     "View Schools"
                                           :on-click #(print "View Schools")}]}
                               {:icon    "games"
                                :text    "Activities"
                                :counter 315}
                               {:icon       "students"
                                :text       "Students"
                                :counter    128
                                :background "transparent"}
                               {:icon            "teachers"
                                :icon-background "yellow-2"
                                :text            "Teacher"
                                :counter         13}]
                              (map #(assoc % :type "horizontal"))))

(defn- component
  [props]
  [ui/card (assoc props :title (js/JSON.stringify (clj->js props)))])

(defn page
  []
  [:div#page--cards
   [layout {:title "Cards"}
    [:h2 "Horizontal cards"]
    [panel {:class-name "cards-panel"}
     (for [[idx props] (map-indexed vector horizontal-variants)]
       ^{:key (str "variant-" idx)}
       [component props])]]])
