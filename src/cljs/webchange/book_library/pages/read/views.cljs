(ns webchange.book-library.pages.read.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.error-message.views :refer [error-message]]
    [webchange.book-library.components.loading-indicator.views :refer [loading-indicator]]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.read.icons :refer [icons]]
    [webchange.book-library.pages.read.state :as state]
    [webchange.i18n.translate :as i18n]
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
   [:div
    (get icons icon)]
   [:span text]])

(defn- menu
  []
  (let [show-menu? (not @(re-frame/subscribe [::state/reading-in-progress?]))
        show-buttons? @(re-frame/subscribe [::state/show-buttons?])
        loading? @(re-frame/subscribe [::state/show-loading?])
        error? @(re-frame/subscribe [::state/show-error?])

        handle-read-with-sound-click #(re-frame/dispatch [::state/read-with-sound])
        handle-read-without-sound-click #(re-frame/dispatch [::state/read-without-sound])
        handle-favorite-click #(print "favorite")]
    (when show-menu?
      [:div.menu-wrapper
       (into [:div.menu]
             (cond-> []
                     loading? (concat [[loading-indicator]])
                     error? (concat [[error-message]])
                     show-buttons? (concat [[menu-item {:text     @(re-frame/subscribe [::i18n/t [:read/to-me]])
                                                        :icon     "volume"
                                                        :big?     true
                                                        :on-click handle-read-with-sound-click}]
                                            [menu-item {:text     @(re-frame/subscribe [::i18n/t [:read/by-myself]])
                                                        :icon     "no-volume"
                                                        :on-click handle-read-without-sound-click}]
                                            #_[menu-item {:text     @(re-frame/subscribe [::i18n/t [:favorite]])
                                                          :icon     "heart"
                                                          :on-click handle-favorite-click}]])))])))

(defn page
  [{:keys [id book-id]}]
  (re-frame/dispatch [::state/init {:course-id id
                                    :book-id   book-id}])
  (fn []
    [layout {:class-name       "book-read-page"
             :document-title   @(re-frame/subscribe [::i18n/t [:read/read]])
             :show-toolbar?    false
             :show-navigation? false}
     [book]
     [menu]]))
