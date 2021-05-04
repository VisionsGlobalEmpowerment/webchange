(ns webchange.game-changer.templates-list.views-list-item
  (:require
    [reagent.core :as r]
    [webchange.game-changer.templates-list.views-preview :refer [activity-preview]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn template-list-item
  [{:keys [template on-click selected?]}]
  (r/with-let [slideshow-visible? (r/atom false)
               handle-mouse-enter (fn [] (reset! slideshow-visible? true))
               handle-mouse-leave (fn [] (reset! slideshow-visible? false))]
    (let [{:keys [description name preview preview-anim]} template]
      [:div.templates-list-item-place-holder
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
        [:div.title name]
        [:div.description description]

        ]])))
