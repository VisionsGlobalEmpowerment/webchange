(ns webchange.dashboard.classes.views-list-menu
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.classes.views-common :refer [class-modal]]))

(defn- classes-list-menu-item
  [{:keys [id name]} {:keys [on-click]}]
  [ui/list-item
   {:button   true
    :on-click #(on-click id)
    :style    {:text-transform "capitalize"}}
   [ui/list-item-text
    {:inset   true
     :primary name}]])

(defn classes-list-menu
  []
  (r/with-let [_ (re-frame/dispatch [::classes-events/load-classes])]
              (let [current-class (r/atom {})
                    modal-state (r/atom {:open false})]
                (fn []
                  (let [classes @(re-frame/subscribe [::classes-subs/classes-list])
                        open-modal #(swap! modal-state assoc :open true)
                        close-modal #(swap! modal-state assoc :open false)
                        add-class (fn [class-data] (re-frame/dispatch [::classes-events/add-class class-data]))
                        show-class (fn [class-id] (re-frame/dispatch [::classes-events/show-class-profile class-id]))]
                    [ui/list {:component "nav"}
                     [ui/list-item
                      {:button   true
                       :on-click #(re-frame/dispatch [::classes-events/show-manage-classes])}
                      "Classes"]
                     (for [class classes]
                       ^{:key (:id class)}
                       [classes-list-menu-item class {:on-click show-class}])
                     [ui/list-item
                      {:button   true
                       :on-click (fn [] (do (reset! current-class {})
                                            (swap! modal-state assoc :title :add)
                                            (open-modal)))}
                      [ui/list-item-icon [ic/add]]
                      [ui/list-item-text
                       {:inset   true
                        :primary "Add"}]
                      ]
                     [class-modal current-class {:title        :add
                                                 :modal-open   (:open @modal-state)
                                                 :handle-save  add-class
                                                 :handle-close close-modal}]])))))
