(ns webchange.admin.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.editor-v2.components.card.views :refer [list-card] :as card]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]))

(re-frame/reg-sub
  ::schools-list
  (fn [db]
    ;; (get-in db [:dashboard :schools])
    ;;#############################
    [{:id 1 :name "School 01" :presentation 37 :users 90}
     {:id 2 :name "School 02" :presentation 37 :users 90}
     {:id 3 :name "School 03" :presentation 37 :users 90}
     {:id 4 :name "School 04" :presentation 37 :users 90}
     ]
    ;;############################
    ))


(defn school-item [data]
  (let [list-styles (card/get-styles)]
    (println data)
    [ui/list-item
     [ui/list-item-text {:primary (:name data)}]
     [ui/list-item-secondary-action
      [ui/button {:aria-label "Presentation"
                  :style {:background-color "white"
                          :padding "0"}
                  :on-click   #()}
       (:users data)
       [:span {:style {:width "5px"}}]
       [ic/group {:style (:action-icon list-styles)}]]
      [ui/button {:aria-label "Presentation"
                  :style {:background-color "white"
                          :padding "0"}
                  :on-click   #()}
       (:presentation data)
       [:span {:style {:width "5px"}}]
       [ic/tv {:style (:action-icon list-styles)}]]

      [ui/icon-button {:aria-label "Delete"
                       :on-click   #()}
       [ic/delete {:style (:action-icon list-styles)}]]

      [ui/icon-button {:aria-label "Edit"
                       :on-click   #()}
       [ic/edit {:style (:action-icon list-styles)}]]]]))

(defn schools-list []
  (let [schools @(re-frame/subscribe [::schools-list])
        list-styles (card/get-styles)]
    [list-card {:title        "Schools"
                :style {:padding "0px 50px"}
                :full-height  true}

     [ui/button {:aria-label "Presentation"
                 :on-click   #()}
      "New School"
      [ic/add {:style (:action-icon list-styles)}]]

     [ui/list {:style (:list-full-height list-styles)}
      (for [{:keys [id] :as school-data} schools]
        ^{:key id}
        [school-item school-data])]]))
