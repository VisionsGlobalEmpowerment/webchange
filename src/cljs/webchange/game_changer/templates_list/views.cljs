(ns webchange.game-changer.templates-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.templates-list.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- template-list-item
  [{:keys [template on-click selected?]}]
  (let [{:keys [description name preview]} template]
    [:div {:class-name (get-class-name {"templates-list-item" true
                                        "selected"            selected?})
           :on-click   #(on-click template)}
     [:div.preview {:style {:background-image (str "url(" preview ")")}}]
     [:span.title name]
     [:div.description description]]))

(defn templates-list
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])
               handle-click (fn [template]
                              (->> (select-keys template [:id :name])
                                   (swap! data assoc :template)))]
    (let [templates @(re-frame/subscribe [::state/templates-list])
          current-template-id (get-in @data [:template :id])]
      [:div.templates-list
       (for [{:keys [id] :as template} templates]
         ^{:key id}
         [template-list-item {:template  template
                              :selected? (= id current-template-id)
                              :on-click  handle-click}])])))


