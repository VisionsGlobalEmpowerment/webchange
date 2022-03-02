(ns webchange.book-library.pages.read.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.loading-indicator.views :refer [loading-indicator]]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.read.icons :refer [icons]]
    [webchange.book-library.pages.read.state :as state]
    [webchange.interpreter.components :refer [interpreter]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- book
  []
  (let [book-loaded? @(re-frame/subscribe [::state/book-loaded?])
        stage-ready? @(re-frame/subscribe [::state/stage-ready?])
        set-stage-ready #(re-frame/dispatch [::state/set-stage-ready true])]
    [:div {:class-name (get-class-name {"book-wrapper" true
                                        "hidden"       (not stage-ready?)})}
     (when book-loaded?
       [interpreter {:mode        ::modes/book-reader
                     :stage-props {:show-loader-screen? false
                                   :force-show-scene?   true}
                     :on-ready    set-stage-ready}])]))

(defn- menu-item
  [{:keys [big? icon on-click text]}]
  [:button {:class-name (get-class-name {"big" big?})
            :on-click   on-click}
   (get icons icon)
   text])

(defn- menu
  []
  (let [show-menu? @(re-frame/subscribe [::state/show-menu?])
        handle-read-with-sound-click #(re-frame/dispatch [::state/read-with-sound])
        handle-read-without-sound-click #(re-frame/dispatch [::state/read-without-sound])
        handle-favorite-click #(print "favorite")]
    (when show-menu?
      [:div.menu-wrapper
       [:div.menu
        [menu-item {:text     "Read to me"
                    :icon     "volume"
                    :big?     true
                    :on-click handle-read-with-sound-click}]
        [menu-item {:text     "Read by myself"
                    :icon     "no-volume"
                    :on-click handle-read-without-sound-click}]
        #_[menu-item {:text     "Favorite"
                      :icon     "heart"
                      :on-click handle-favorite-click}]]])))

(defn- loading
  []
  (let [show-loading? @(re-frame/subscribe [::state/show-loading?])]
    (when show-loading?
      [:div.loading-indicator-wrapper
       [loading-indicator]])))

(defn page
  [{:keys [id book-id]}]
  (re-frame/dispatch [::state/init {:course-id id
                                    :book-id   book-id}])
  (fn []
    [layout {:class-name       "book-read-page"
             :document-title   "Read"
             :show-toolbar?    false
             :show-navigation? false}
     [book]
     [menu]
     [loading]]))
