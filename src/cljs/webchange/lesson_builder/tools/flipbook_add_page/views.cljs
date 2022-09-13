(ns webchange.lesson-builder.tools.flipbook-add-page.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.flipbook-add-page.state :as state]
    [webchange.lesson-builder.widgets.form-actions.views :refer [form-actions]]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- layout-form-item-block
  [{:keys [title]}]
  [:div {:class-name "layout-form-item-block"}
   [:h2 {:class-name "layout-form-item-block--title"}
    title]
   (->> (r/current-component)
        (r/children)
        (into [:div {:class-name "layout-form-item-block--content"}]))])

(defn- layout-form-image
  [{:keys [id]}]
  (let [{:keys [src]} @(re-frame/subscribe [::state/form-param id])
        handle-change #(re-frame/dispatch [::state/set-form-param id {:src (:url %)}])]
    [layout-form-item-block {:title "Image"}
     [select-image {:value     src
                    :on-change handle-change}]]))

(defn- layout-form-text
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/form-param id])
        handle-change #(re-frame/dispatch [::state/set-form-param id %])]
    [layout-form-item-block {:title "Text"}
     [ui/text-area {:value      value
                    :on-change  handle-change
                    :class-name "layout-form-item-block--text"}]]))

(defn- layout-form-item
  [{:keys [type] :as props}]
  (case type
    "image" [layout-form-image props]
    "text" [layout-form-text props]
    nil))

(defn layout-form
  []
  (let [layout-selected? @(re-frame/subscribe [::state/layout-selected?])
        layout-params @(re-frame/subscribe [::state/layout-params])
        save-button-disabled? @(re-frame/subscribe [::state/save-button-disabled?])
        handle-cancel #(re-frame/dispatch [::state/cancel])
        handle-save #(re-frame/dispatch [::state/save])]
    [:div {:class-name "widget--flipbook-page-layout--form"}
     [:div {:class-name "form-content"}
      [:h1 "Add Layout"]
      (if layout-selected?
        (for [{:keys [id] :as layout-param} layout-params]
          ^{:key id}
          [layout-form-item layout-param])
        [ui/info "Select layout"])]
     [form-actions {:on-cancel  handle-cancel
                    :on-save    handle-save
                    :save-props {:disabled? save-button-disabled?}}]]))

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
