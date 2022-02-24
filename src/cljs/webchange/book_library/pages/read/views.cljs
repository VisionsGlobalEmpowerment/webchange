(ns webchange.book-library.pages.read.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.book-library.pages.read.icons :refer [icons]]
    [webchange.book-library.pages.read.state :as state]
    [webchange.interpreter.components :refer [stage-wrapper]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- book
  []
  (let [{:keys [id data]} @(re-frame/subscribe [::state/book])]
    (when (some? data)
      [:div.book-wrapper
       [stage-wrapper {
                       ;:mode
                       :scene-id   id
                       :scene-data data
                       ;:dataset-items []
                       ;:on-ready
                       ;:reset-resources?
                       }]])))

(defn- menu-item
  [{:keys [big? icon on-click text]}]
  [:button {:class-name (get-class-name {"big" big?})
            :on-click   on-click}
   (get icons icon)
   text])

(defn- menu
  []
  (let [show-menu? @(re-frame/subscribe [::state/show-menu?])
        handle-read-with-sound-click #(print "read-with-sound")
        handle-read-without-sound-click #(print "read-without-sound")
        handle-favorite-click #(print "favorite")]
    (when show-menu?
      [:div.menu-wrapper
       [:div.menu
        [menu-item {:text     "Read to me"
                    :icon     "volume"
                    :big?     true
                    :on-click handle-read-with-sound-click}]
        #_[menu-item {:text     "Read by myself"
                    :icon     "no-volume"
                    :on-click handle-read-without-sound-click}]
        #_[menu-item {:text     "Favorite"
                    :icon     "heart"
                    :on-click handle-favorite-click}]]])))

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
     [menu]]))
