(ns webchange.lesson-builder.tools.flipbook-add-page.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.flipbook-add-page.state :as state]
    [webchange.ui.index :as ui]))

(defn layout-form
  []
  [:div
   "layout form"])

(defn- layout-selector-item
  [{:keys [icon selected? type value]}]
  (let [handle-click #(re-frame/dispatch [::state/set-current-layout value])]
    [:div {:class-name (ui/get-class-name {"layout-item"                   true
                                           (str "layout-item--type-" type) true
                                           "layout-item--selected"         selected?})
           :on-click   handle-click}
     [ui/layout-icon {:icon       icon
                      :class-name "layout-item--preview"}]
     (when selected?
       [ui/icon {:icon       "check"
                 :class-name "layout-item--select-mark"}])]))

(defn- layout-selector
  []
  (let [layouts @(re-frame/subscribe [::state/available-layouts])]
    [:div {:class-name "widget--flipbook-page-layout-selector"}
     (for [{:keys [value] :as layout-data} layouts]
       ^{:key value}
       [layout-selector-item layout-data])]))

(defn- layout-type-selector
  []
  (let [current-type @(re-frame/subscribe [::state/layout-type])
        handle-page-click #(re-frame/dispatch [::state/set-layout-type "page"])
        handle-spread-click #(re-frame/dispatch [::state/set-layout-type "spread"])]
    [:div {:class-name "widget--flipbook-page-layout-type-selector"}
     [ui/button {:color      "transparent"
                 :class-name (ui/get-class-name {"switcher-button"         true
                                                 "switcher-button--active" (= current-type "page")})
                 :on-click   handle-page-click}
      "Page"]
     [ui/button {:color      "transparent"
                 :class-name (ui/get-class-name {"switcher-button"         true
                                                 "switcher-button--active" (= current-type "spread")})
                 :on-click   handle-spread-click}
      "Spread"]]))

(defn select-layout
  []
  [toolbox {:title   "Add Layout"
            :icon    "create"
            :actions [layout-type-selector]}
   [layout-selector]])
