(ns webchange.ui.pages.layout
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.routes :as routes]))

(defn layout
  [{:keys [title show-back?] :or {show-back? true}}]
  (let [handle-back-click #(re-frame/dispatch [::routes/redirect :dashboard])]
    [:div#bbs--ui-pages--layout
     [:div.header
      (when show-back?
        [:button {:class-name "back-button"
                  :on-click   handle-back-click}
         "<< Back"])
      [:h1 title]]
     (->> (r/current-component)
          (r/children)
          (into [:div.content]))]))
