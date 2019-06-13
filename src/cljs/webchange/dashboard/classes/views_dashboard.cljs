(ns webchange.dashboard.classes.views-dashboard
  (:require
    [cljsjs.material-ui]
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
  [{:keys [id name] :as class} {:keys [on-click on-edit on-remove]}]
  (let [menu-anchor (r/atom nil)
        menu-open? (r/atom false)]
    (fn []
      [:div.classes-list-item
       [:div.classes-list-item_header {:on-click #(on-click id)} name]
       [:div.classes-list-item_body]
       [:div.classes-list-item_actions
        [ui/icon-button
         {:on-click #(do (reset! menu-open? true)
                         (reset! menu-anchor (.-currentTarget %)))}
         [ic/more-horiz]]
        [ui/menu
         {:open      @menu-open?
          :on-close  #(reset! menu-open? false)
          :anchor-El @menu-anchor}
         [ui/menu-item
          {:on-click #(do (on-edit class)
                          (reset! menu-open? false))}
          "Edit"]
         [ui/menu-item
          {:on-click #(do (on-remove class)
                          (reset! menu-open? false))}
          "Remove"]]]])))

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
                        show-class (fn [class-id] (re-frame/dispatch [::classes-events/show-class class-id]))
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
                     [ui/card-content
                      [:div.classes-list
                       (for [class-data classes]
                         ^{:key (:id class-data)}
                         [classes-dashboard-item class-data {:on-click  show-class
                                                             :on-edit   handle-edit-click
                                                             :on-remove remove-class}])
                       [add-new-class-dashboard-item {:on-click handle-add-click}]]
                      [class-modal current-class {:title        (:title @modal-state)
                                                  :modal-open   (:open @modal-state)
                                                  :handle-save  (:handle-save @modal-state)
                                                  :handle-close close-modal}]]])))))
