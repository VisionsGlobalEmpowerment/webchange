(ns webchange.lesson-builder.widgets.pages.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.state-flipbook :as flipbook-state]
    [webchange.lesson-builder.state-flipbook-screenshot :as screenshot]
    [webchange.lesson-builder.widgets.pages.state :as state]
    [webchange.ui.index :as ui]))

(defn- tech-pages-switch
  []
  (let [value @(re-frame/subscribe [::flipbook-state/show-generated-pages?])
        handle-change #(re-frame/dispatch [::flipbook-state/set-show-generated-pages %])]
    [ui/switch {:checked?  value
                :label     "Display Technical Pages"
                :color     "yellow-1"
                :on-change handle-change}]))

(defn- add-page-item
  [{:keys [side title]}]
  (let [handle-click #(re-frame/dispatch [::state/add-page])]
    [:div {:class-name   (ui/get-class-name {"page-item"              true
                                             (str "page-item--" side) true})
           :on-click     handle-click
           :data-test-id "flipbook-add-page"}
     [:div {:class-name (ui/get-class-name {"page-item--preview"     true
                                            "page-item--add-preview" true})}
      [ui/icon {:icon       "plus"
                :class-name "page-item--add-image"}]]
     [:div {:class-name "page-item--title"
            :title      title}
      title]]))

(defn drag-event->page-offset
  [event]
  (let [event (if (some? (.-nativeEvent event))
                (.. event -nativeEvent)
                event)
        target (.-target event)
        offset-x (.-offsetX event)
        target-width (.. target -clientWidth)]
    (.preventDefault event)
    (if (< offset-x (/ target-width 2))
      0
      1)))

(defn- page-item
  []
  (r/create-class
    {:display-name "page-item"

     :component-did-mount
     (fn [this]
       (let [{:keys [idx preview]} (r/props this)]
         (when (and (nil? preview)
                    (number? idx))
           (re-frame/dispatch [::screenshot/take-page-screenshot idx]))))

     :reagent-render
     (fn [{:keys [id idx side title preview] :as props}]
       (let [current-page-over (re-frame/subscribe [::state/current-page-over])]
         (if-not (= id :add)
           [:div {:class-name (ui/get-class-name {"page-item"              true
                                                  (str "page-item--" side) true
                                                  "page-item-drag-over" (= idx @current-page-over)})
                  :draggable true
                  :on-drag-start #(re-frame/dispatch [::state/drag-start-page idx])
                  :on-drop #(re-frame/dispatch [::state/drag-drop-page])
                  :on-drag-over #(re-frame/dispatch [::state/drag-enter-page idx (drag-event->page-offset %)])}
            [:div {:class-name "page-item--preview"}
             ^{:key preview}
             [ui/image {:src        preview
                        :class-name "page-item--preview-image"}]]
            [:div {:class-name "page-item--title"
                   :title      title}
             title]]
           [add-page-item props])))}))

(defn- stage-item
  [{:keys [idx left-page right-page title current-stage?]}]
  (let [handle-click #(when (number? idx)
                        (re-frame/dispatch [::flipbook-state/show-flipbook-stage idx]))]
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
  (let [stages @(re-frame/subscribe [::state/stages])]
    [:div.widget--activity-pages
     {:on-drop #(re-frame/dispatch [::state/drag-drop-page])
      :on-drag-over #(.preventDefault %)}
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
