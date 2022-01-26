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

(defn- button-connector
  []
  [:div.play-button-connector.timeline-item
   [:svg {:width               "100%"
          :height              "100%"
          :fill                "none"
          :preserveAspectRatio "none"
          :viewBox             "0 0 198 93"}
    [:path {:d              "M 5 4 q 44 0 96 42 t 96 42"
            :stroke         "#fff"
            :stroke-width   3
            :stroke-linecap "round"}]]])

(defn- item-connector
  []
  [:div.connector.timeline-item
   [:svg {:width               "100%"
          :height              "100%"
          :fill                "none"
          :preserveAspectRatio "none"
          :viewBox             "0 0 103 167"}
    [:path {:d                "M 4 5 Q 43 12 48 84 T 97 161"
            :stroke           "#fff"
            :stroke-width     3
            :stroke-linecap   "round"
            :stroke-dasharray "3,10"}]]])

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
                               (let [loading? @(re-frame/subscribe [::state/loading?])
                                     finished-activities @(re-frame/subscribe [::state/finished-activities])
                                     handle-next-click (fn [] (re-frame/dispatch [::state/open-next-activity]))
                                     handle-activity-click (fn [activity] (re-frame/dispatch [::state/open-activity activity]))]
                                 [:div.timeline-wrapper {:ref #(when (some? %)
                                                                 (reset! container %))}
                                  (when-not loading?
                                    (into [:div.timeline
                                           (-> (reduce (fn [result {:keys [id] :as item}]
                                                         (concat result [^{:key (str id "-connector")}
                                                                         [item-connector]
                                                                         ^{:key id}
                                                                         [timeline-item (merge item
                                                                                               {:on-click handle-activity-click})]]))
                                                       [^{:key "filler"} [:div.filler]]
                                                       finished-activities)
                                               (concat [(when-not (empty? finished-activities)
                                                          ^{:key "play-button-connector"}
                                                          [button-connector])
                                                        ^{:key "play-button"}
                                                        [play-button {:on-click handle-next-click}]]))]))]))})))
