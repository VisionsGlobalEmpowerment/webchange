(ns webchange.lesson-builder.widgets.pages.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.blocks.stage.state :as stage-state]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.state-flipbook :as state]
    [webchange.lesson-builder.state-flipbook-screenshot :as screenshot]
    [webchange.ui.index :as ui]))

(defn- tech-pages-switch
  []
  (let [value @(re-frame/subscribe [::state/show-generated-pages?])
        handle-change #(re-frame/dispatch [::state/set-show-generated-pages %])]
    [ui/switch {:checked?  value
                :label     "Display Technical Pages"
                :color     "yellow-1"
                :on-change handle-change}]))

(defn- page-item
  []
  (r/create-class
    {:display-name "page-item"

     :component-did-mount
     (fn [this]
       (let [{:keys [idx preview]} (r/props this)]
         (when (nil? preview)
           (re-frame/dispatch [::screenshot/take-page-screenshot idx]))))

     :reagent-render
     (fn [{:keys [side title preview]}]
       [:div {:class-name (ui/get-class-name {"page-item"              true
                                              (str "page-item--" side) true})}
        [:div {:class-name "page-item--preview"}
         ^{:key preview}
         [ui/image {:src        preview
                    :class-name "page-item--preview-image"}]]
        [:div {:class-name "page-item--title"
               :title      title}
         title]])}))

(defn- stage-item
  [{:keys [idx left-page right-page title current-stage?]}]
  (let [handle-click #(re-frame/dispatch [::state/show-flipbook-stage idx])]
    [:div {:class-name (ui/get-class-name {"stage-item"                true
                                           "stage-item--selected"      current-stage?
                                           "stage-item--no-left-page"  (nil? left-page)
                                           "stage-item--no-right-page" (nil? right-page)})
           :on-click   handle-click}
     [:div {:class-name "stage-item--title"
            :title      title}
      title]
     (when (some? left-page)
       [page-item (merge left-page
                         {:side "left"})])
     [:div.stage-item--divider]
     (when (some? right-page)
       [page-item (merge right-page
                         {:side "right"})])]))

(defn- stages-list
  []
  (let [stages @(re-frame/subscribe [::state/activity-stages-filtered])]
    [:div.widget--activity-pages
     (for [{:keys [id] :as stage-data} stages]
       ^{:key id}
       [stage-item stage-data])]))

(defn activity-pages
  []
  (let [stage-ready? @(re-frame/subscribe [::stage-state/stage-ready?])
        stage-busy? @(re-frame/subscribe [::stage-state/stage-busy?])
        ready? (and stage-ready? (not stage-busy?))]
    [toolbox {:title      "Pages"
              :icon       "create"
              :class-name "widget--activity-pages--toolbox"
              :actions    (when ready?
                            [tech-pages-switch])}
     (if ready?
       [stages-list]
       [ui/loading-overlay])]))
