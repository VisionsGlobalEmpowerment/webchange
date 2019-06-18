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

(def padding 24)
(def size 200)
(def classes-list-item-style
  {:background-color "#ffffff"
   :border           "solid 1px #d3d3d3"
   :border-radius    "10px"
   :box-shadow       "rgba(0, 0, 0, 0.12) 0px 1px 6px, rgba(0, 0, 0, 0.12) 0px 1px 4px"
   :display          "flex"
   :flex-direction   "column"
   :flex-grow        0
   :height           size
   :margin           "10px"
   :width            size})
(def classes-add-new-list-item-style
  {:background-color "#00bcd4"
   :color            "#ffffff"
   :cursor           "pointer"
   :font-size        "36px"
   :font-weight      "bold"
   :line-height      (str size "px")
   :text-align       "center"
   :user-select      "none"})
(def classes-list-item-header-style
  {:font-size   "32px"
   :font-weight 500
   :flex-grow   0
   :color       "#292929"
   :cursor      "pointer"
   :line-height "43px"
   :padding     (str padding "px")})
(def classes-list-item-body-style
  {:flex-grow 1
   :padding   (str 0 " " padding "px")})
(def classes-list-item-actions-style
  {:flex-grow  0
   :text-align "right"
   :padding    (str 0 " " (/ padding 2) "px")})

(defn- add-new-class-dashboard-item
  [{:keys [on-click]}]
  [:div
   {:on-click on-click
    :style    (merge classes-list-item-style classes-add-new-list-item-style)}
   [:div
    {:style classes-list-item-body-style}
    "+ New"]])

(defn- classes-dashboard-item
  [{:keys [id name] :as class} {:keys [on-click on-edit on-remove]}]
  (let [menu-anchor (r/atom nil)
        menu-open? (r/atom false)]
    (fn []
      [:div {:style classes-list-item-style}
       [:div
        {:on-click #(on-click id)
         :style    classes-list-item-header-style}
        name]
       [:div {:style classes-list-item-body-style}]
       [:div {:style classes-list-item-actions-style}
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
                      [:div {:style {:display   "flex"
                                     :flex-wrap "wrap"
                                     :padding   20}}
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
