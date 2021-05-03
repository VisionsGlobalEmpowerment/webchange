(ns webchange.game-changer.templates-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.templates-list.state :as state]
    [webchange.game-changer.templates-list.views-preview :refer [activity-preview]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- template-list-item
  [{:keys [template on-click selected?]}]
  (r/with-let [slideshow-visible? (r/atom false)
               handle-mouse-enter (fn [] (reset! slideshow-visible? true))
               handle-mouse-leave (fn [] (reset! slideshow-visible? false))]
    (let [{:keys [description name preview preview-anim]} template]
      [:div {:class-name     (get-class-name {"templates-list-item" true
                                              "selected"            selected?})
             :on-click       #(on-click template)
             :on-mouse-enter handle-mouse-enter
             :on-mouse-leave handle-mouse-leave}
       [:div {:class-name (get-class-name {"preview"     true
                                           "placeholder" (not (some? preview))})
              :style      (cond-> {}
                                  (some? preview) (assoc :background-image (str "url(" preview ")")))}
        (when (and (some? preview-anim)
                   @slideshow-visible?)
          [activity-preview {:slides preview-anim}])]
       [:span.title name]
       [:div.description description]])))

(defn templates-list
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])
               handle-click (fn [template]
                              (->> (select-keys template [:id :name :options :options-groups])
                                   (swap! data assoc :template)))]
    (let [templates @(re-frame/subscribe [::state/templates-list])
          current-template-id (get-in @data [:template :id])]
      [:div.templates-list
       (for [{:keys [id] :as template} templates]
         ^{:key id}
         [template-list-item {:template  template
                              :selected? (= id current-template-id)
                              :on-click  handle-click}])])))


