(ns webchange.ui-framework.test-page.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.index :refer [icon icon-button]]))

(defn- row
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-row
     [:span.test-page-row-title
      title ":"]
     (into [:div.test-page-row-wrapper]
           (r/children this))]))

(defn- block
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-element
     [:span.test-page-element-title
      title]
     (into [:div.test-page-element-wrapper]
           (r/children this))]))

(defn test-ui
  []
  [:div
   [row {:title "Icon"}
    [block {:title "With rotation"}
     [icon {:icon    "sync"
            :rotate? true}]]]
   [row {:title "Icon Button"}
    [block {:title "Default"}
     [icon-button {:icon "arrow-left"}]]
    [block {:title "Primary Color"}
     [icon-button {:icon  "mic"
                   :color "primary"}]]
    [block {:title "Disabled"}
     [icon-button {:icon      "arrow-right"
                   :disabled? true}]]
    [block {:title "Outlined"}
     [icon-button {:icon    "link"
                   :variant "outlined"}]]
    [block {:title "Play button"}
     [icon-button {:icon  "play"
                   :color "primary"}]]
    [block {:title "With text"}
     [icon-button {:icon    "link"
                   :variant "outlined"}
      "Copy Link"]]]])
