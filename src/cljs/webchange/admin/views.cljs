(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.schools.views :refer [schools-list]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.ui.theme :refer [get-in-theme]]))

(def bar-height 64)

(re-frame/reg-sub
  ::current-main-content
  (fn [db]
    ;; (get-in db [:dashboard :current-main-content])
    :schools-list ;;#############################################
    ))

(defn main-content [page]
  (case page
    :schools-list [schools-list]))

(defn top-bar []
  [ui/toolbar {:style {:box-shadow (get-in-theme [:shadows 4])}}

   [ui/button {:on-click   #()}
    [ic/account-circle {:style (:action-icon list-styles)}]
    [:span {:style {:width "10px"}}]
    "TabSchool"]

   [ui/button {:on-click   #()}
    [ic/desktop-windows {:style (:action-icon list-styles)}]
    [:span {:style {:width "10px"}}]
    "Dashboard"]
   ])

(defn dashboard []
  (fn [route-params]
    (let [current-main-content @(re-frame/subscribe [::current-main-content])]
      [:div
       [top-bar]
       [:div {:style {:height  (str "calc(100vh - " bar-height "px)")}}
        [main-content current-main-content]
        ]])))
