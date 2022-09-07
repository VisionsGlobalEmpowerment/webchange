(ns webchange.lesson-builder.widgets.pages.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.state-flipbook :as state]
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
  [{:keys [side title]}]
  [:div {:class-name (ui/get-class-name {"page-item"              true
                                         (str "page-item--" side) true})}
   [:div {:class-name "page-item--preview"}
    [ui/image {:class-name "page-item--preview-image"}]]
   [:div {:class-name "page-item--title"
          :title      title}
    title]])

(defn- stage-item
  [{:keys [idx left-page right-page title current-stage?] :as stage-data}]
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
  [toolbox {:title   "Pages"
            :icon    "create"
            :actions [tech-pages-switch]}
   [stages-list]])
