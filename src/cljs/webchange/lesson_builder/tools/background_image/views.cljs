(ns webchange.lesson-builder.tools.background-image.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.background-image.state :as state]
    [webchange.lesson-builder.widgets.image-library.views :refer [image-library]]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]
    [webchange.ui.index :as ui]))

(defn- images-collection
  []
  (let [{:keys [type]} @(re-frame/subscribe [::state/form-content])
        handle-click #(re-frame/dispatch [::state/select-background-image {:type  type
                                                                           :image %}])]
    [image-library {:type     type
                    :on-click handle-click}]))

(defn- image-picker
  [{:keys [on-click src title]}]
  [:div {:class-name "background-image--image-picker"}
   [:div.image-picker--header title]
   [:div {:class-name "image-picker--image-wrapper"
          :on-click   on-click}
    (when (some? src)
      [ui/image {:src        src
                 :class-name "image-picker--image"}])
    [ui/icon {:icon       "plus"
              :class-name "image-picker--add-icon"}]]])

(defn- layered-background-form
  [{:keys [class-name]}]
  (let [{:keys [background decoration surface]} @(re-frame/subscribe [::state/current-background-data])
        handle-background-click #(re-frame/dispatch [::state/show-image-library "background"])
        handle-surface-click #(re-frame/dispatch [::state/show-image-library "surface"])
        handle-decoration-click #(re-frame/dispatch [::state/show-image-library "decoration"])]
    [:div {:class-name (ui/get-class-name {"background-image--layered-form" true
                                           class-name                       (some? class-name)})}
     [image-picker {:src      background
                    :title    "Choose Background"
                    :on-click handle-background-click}]
     [image-picker {:src      surface
                    :title    "Choose Surface"
                    :on-click handle-surface-click}]
     [image-picker {:src      decoration
                    :title    "Choose Decorations"
                    :on-click handle-decoration-click}]]))

(defn- single-background-form
  [{:keys [class-name]}]
  (let [{:keys [single-background]} @(re-frame/subscribe [::state/current-background-data])
        handle-lick #(re-frame/dispatch [::state/show-image-library "single-background"])]
    [:div {:class-name (ui/get-class-name {"background-image--layered-form" true
                                           class-name                       (some? class-name)})}
     [image-picker {:src      single-background
                    :title    "Choose Background"
                    :on-click handle-lick}]]))

(defn- actions
  []
  (let [handle-cancel-click #(re-frame/dispatch [::state/cancel])
        handle-save-click #(re-frame/dispatch [::state/save])]
    [:div.background-image--actions
     [ui/button {:color    "blue-1"
                 :on-click handle-cancel-click}
      "Cancel"]
     [ui/button {:on-click handle-save-click}
      "Save"]]))

(defn- background-type-switcher
  []
  (let [current-type @(re-frame/subscribe [::state/current-background-type])
        handle-single-click #(re-frame/dispatch [::state/set-background-type "background"])
        handle-layered-click #(re-frame/dispatch [::state/set-background-type "layered-background"])]
    [:div.widget--background-type-switcher
     [ui/button {:color      "transparent"
                 :class-name (ui/get-class-name {"switcher-button"         true
                                                 "switcher-button--active" (= current-type "background")})
                 :on-click   handle-single-click}
      "Single Image"]
     [ui/button {:color      "transparent"
                 :class-name (ui/get-class-name {"switcher-button"         true
                                                 "switcher-button--active" (= current-type "layered-background")})
                 :on-click   handle-layered-click}
      "Layered Background"]]))

(defn background-image
  []
  (r/create-class
    {:display-name "Change Background Image"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [] (re-frame/dispatch [::state/reset]))

     :reagent-render
     (fn []
       (let [{:keys [component]} @(re-frame/subscribe [::state/form-content])]
         [toolbox {:title   "Change Background"
                   :icon    "create"
                   :actions background-type-switcher}
          [:div.widget--background-image
           [:div.background-image--content
            (case component
              "layered-background" [layered-background-form {:class-name "background-image--content"}]
              "background" [single-background-form {:class-name "background-image--content"}]
              "image-library" [images-collection {:type "background"}]
              [ui/loading-overlay])]
           [actions]]]))}))


