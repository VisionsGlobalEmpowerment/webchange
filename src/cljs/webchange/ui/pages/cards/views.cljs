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

(def vertical-variants (->> [{:text            "Classes"
                              :icon            "classes"
                              :icon-background "blue-1"
                              :counter         20
                              :background      "yellow-2"
                              :actions         [{:text     "Manage Classes"
                                                 :on-click #(print "Manage Classes")}
                                                {:text     "Add Class"
                                                 :chip     "plus"
                                                 :color    "blue-1"
                                                 :on-click #(print "Add Class")}]}
                             {:text            "Courses"
                              :icon            "courses"
                              :icon-background "blue-1"
                              :counter         20
                              :background      "green-2"
                              :actions         [{:text     "Manage Courses"
                                                 :on-click #(print "Manage Courses")}]}]
                            (map #(assoc % :type "vertical"))))

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
       [component props])]
    [:h2 "Vertical cards"]
    [panel {:class-name "cards-panel stretch"}
     (for [[idx props] (map-indexed vector vertical-variants)]
       ^{:key (str "variant-" idx)}
       [component props])]]])
