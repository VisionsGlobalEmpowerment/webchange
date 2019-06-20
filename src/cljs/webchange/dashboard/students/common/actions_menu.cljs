(ns webchange.dashboard.students.common.actions-menu
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(defn translate
  [path]
  (get-in {:edit   "Edit"
           :remove "Remove"}
          path))

(defn actions-menu []
  (let [menu-anchor (r/atom nil)
        menu-open? (r/atom false)]
    (fn [{:keys [on-edit-click on-remove-click]}]
      [:div
       [ui/icon-button
        {:on-click #(do (reset! menu-open? true)
                        (reset! menu-anchor (.-currentTarget %)))}
        [ic/more-horiz]]
       [ui/menu
        {:open      @menu-open?
         :on-close  #(reset! menu-open? false)
         :anchor-El @menu-anchor}
        (when on-edit-click
          [ui/menu-item
           {:on-click #(do (on-edit-click)
                           (reset! menu-open? false))}
           (translate [:edit])])
        (when on-remove-click
          [ui/menu-item
           {:on-click #(do (on-remove-click)
                           (reset! menu-open? false))}
           (translate [:remove])])]])))
