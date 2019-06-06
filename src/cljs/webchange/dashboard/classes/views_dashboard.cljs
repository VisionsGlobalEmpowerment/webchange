(ns webchange.dashboard.classes.views-dashboard
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.classes.views-common :refer [class-modal]]))

(defn- add-new-class-dashboard-item
  [{:keys [on-click]}]
  [:div.classes-list-item.add-new {:on-click on-click}
   [:div.classes-list-item_body "+ New"]])

(defn- classes-dashboard-item
  [{:keys [name] :as class} {:keys [on-edit on-remove]}]
  [:div.classes-list-item
   [:div.classes-list-item_header name]
   [:div.classes-list-item_body]
   [:div.classes-list-item_actions
    [ui/icon-menu {:icon-button-element (r/as-element [ui/icon-button (ic/navigation-more-horiz)])
                   :anchor-origin       {:horizontal "left" :vertical "top"}
                   :target-origin       {:horizontal "left" :vertical "top"}}
     [ui/menu-item {:primary-text "Edit"
                    :on-click     #(on-edit class)}]
     [ui/menu-item {:primary-text "Remove"
                    :on-click     #(on-remove class)}]]]])

(defn classes-dashboard
  []
  (r/with-let [_ (re-frame/dispatch [::classes-events/load-classes])]
              (let [current-class (r/atom {})
                    modal-state (r/atom {:open        false
                                         :title       ""
                                         :handle-save #()})]
                (fn []
                  (let [classes @(re-frame/subscribe [::classes-subs/classes-list])
                        open-modal #(swap! modal-state assoc :open true)
                        close-modal #(swap! modal-state assoc :open false)
                        add-class (fn [class-data] (re-frame/dispatch [::classes-events/add-class class-data]))
                        edit-class (fn [class-data] (re-frame/dispatch [::classes-events/edit-class (:id class-data) class-data]))
                        remove-class (fn [class-data] (re-frame/dispatch [::classes-events/delete-class (:id class-data)]))
                        handle-add-click (fn [] (do (reset! current-class {})
                                                    (swap! modal-state assoc :title :add)
                                                    (swap! modal-state assoc :handle-save add-class)
                                                    (open-modal)))
                        handle-edit-click (fn [class-data] (do (reset! current-class class-data)
                                                               (swap! modal-state assoc :title :edit)
                                                               (swap! modal-state assoc :handle-save edit-class)
                                                               (open-modal)))]
                    [ui/card
                     [ui/card-header {:title "Classes"}]
                     [ui/card-media {}
                      [:div.classes-list
                       (for [class-data classes]
                         ^{:key (:id class-data)}
                         [classes-dashboard-item class-data {:on-edit   handle-edit-click
                                                             :on-remove remove-class}])
                       [add-new-class-dashboard-item {:on-click handle-add-click}]]
                      [class-modal current-class {:title        (:title @modal-state)
                                                  :modal-open   (:open @modal-state)
                                                  :handle-save  (:handle-save @modal-state)
                                                  :handle-close close-modal}]]])))))
