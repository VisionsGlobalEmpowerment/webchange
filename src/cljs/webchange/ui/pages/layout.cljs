(ns webchange.ui.pages.layout
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn layout
  [{:keys [title show-back?] :or {show-back? true}}]
  (let [handle-back-click #(re-frame/dispatch [:ui-redirect :dashboard])]
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

(defn panel
  [{:keys [class-name color]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name {"bbs--ui-pages--panel"                     true
                                                 (str "bbs--ui-pages--panel--color-" color) (some? color)
                                                 class-name                                 (some? class-name)})}])))
