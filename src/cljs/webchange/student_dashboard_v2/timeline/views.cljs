(ns webchange.student-dashboard-v2.timeline.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.student-dashboard-v2.timeline.state :as state]
    [webchange.student-dashboard-v2.timeline.play-button.views :refer [play-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- timeline-item
  [{:keys [activity title letter on-click preview]}]
  [:div {:title      title
         :on-click   #(on-click activity)
         :class-name (get-class-name {"activity"      true
                                      "timeline-item" true})}
   [:img {:src preview}]
   (when (some? letter)
     [:div.letter [:span letter]])])

(defn timeline
  []
  (let [container (atom nil)
        scroll-to-end (fn []
                        (when (some? @container)
                          (set! (.-scrollLeft @container) (.-scrollWidth @container))))]
    (r/create-class
      {:display-name         "timeline"
       :component-did-mount  (fn [] (scroll-to-end))
       :component-did-update (fn [] (scroll-to-end))
       :reagent-render
                             (fn []
                               (let [finished-activities @(re-frame/subscribe [::state/finished-activities])
                                     handle-next-click (fn [] (re-frame/dispatch [::state/open-next-activity]))
                                     handle-activity-click (fn [activity] (re-frame/dispatch [::state/open-activity activity]))]
                                 [:div.timeline-wrapper {:ref #(when (some? %)
                                                                 (reset! container %))}
                                  (into [:div.timeline
                                         (-> (reduce (fn [result {:keys [id] :as item}]
                                                       (concat result [^{:key (str id "-connector")}
                                                                       [:div.connector.timeline-item]
                                                                       ^{:key id}
                                                                       [timeline-item (merge item
                                                                                             {:on-click handle-activity-click})]]))
                                                     []
                                                     finished-activities)
                                             (concat [^{:key "play-button-connector"}
                                                      [:div.play-button-connector.timeline-item]
                                                      ^{:key "play-button"}
                                                      [play-button {:on-click handle-next-click}]]))])]))})))
