(ns webchange.game-changer.steps.select_template.views-preview
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def timeout 1500)

(defn- init-timer
  [current-slide slides-count]
  (js/setInterval
    (fn []
      (if (= @current-slide (dec slides-count))
        (reset! current-slide 0)
        (swap! current-slide inc)))
    timeout))

(defn activity-preview
  [{:keys [slides]}]
  (r/with-let [current-slide (r/atom 0)
               timer-id (init-timer current-slide (count slides))]
    [:div {:class-name "activity-slideshow"}
     (-> (for [[idx slide] (map-indexed vector slides)]
           ^{:key slide}
           [:img {:class-name (get-class-name {"active" (= idx @current-slide)})
                  :src        slide}])
         (doall))]
    (finally
      (js/clearInterval timer-id))))

