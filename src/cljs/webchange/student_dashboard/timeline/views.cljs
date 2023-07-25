(ns webchange.student-dashboard.timeline.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.student-dashboard.timeline.state :as state]
    [webchange.student-dashboard.timeline.finish-button.views :refer [finish-button]]
    [webchange.student-dashboard.timeline.play-button.views :refer [play-button]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- dashed-connector-svg
  []
  [:svg {:width               "100%"
         :height              "100%"
         :fill                "none"
         :preserveAspectRatio "none"
         :viewBox             "0 0 103 167"}
   [:path {:d                "M 4 5 Q 43 12 48 84 T 97 161"
           :stroke           "#fff"
           :stroke-width     3
           :stroke-linecap   "round"
           :stroke-dasharray "3,10"}]])

(defn- solid-connector-svg
  []
  [:svg {:width               "100%"
         :height              "100%"
         :fill                "none"
         :preserveAspectRatio "none"
         :viewBox             "0 0 198 93"}
   [:path {:d              "M 5 4 q 44 0 96 42 t 96 42"
           :stroke         "#fff"
           :stroke-width   3
           :stroke-linecap "round"}]])

(defn- timeline-item
  [{:keys [activity class-names title letter on-click preview new?]}]
  [:div {:title      title
         :on-click   #(on-click activity)
         :class-name (get-class-name (merge class-names
                                            {"activity"      true
                                             "timeline-item" true}))}
   [:img {:src        preview
          :class-name "preview"}]
   (when new?
     [:img {:src        "/images/student_dashboard/star.svg"
            :class-name "star"}])
   (when (some? letter)
     [:div.letter-wrapper
      [:div.letter
       [:span letter]]])])

(defn- button-connector
  [{:keys [class-names]}]
  [:div {:class-name (get-class-name (merge class-names
                                            {"connector"             true
                                             "play-button-connector" true}))}
   [solid-connector-svg]])

(defn- item-connector
  [{:keys [class-names]}]
  [:div {:class-name (get-class-name (merge class-names
                                            {"connector" true}))}
   [dashed-connector-svg]])

(defn new-activity? [activity]
  (let [new-unique-ids (map :unique-id @(re-frame/subscribe [::state/new-activities]))]
    (boolean (some #{(:unique-id activity)} new-unique-ids))))

(defn timeline
  []
  (let [container (atom nil)
        scroll-to-end (fn []
                        (when (some? @container)
                          (js/setTimeout #(set! (.-scrollLeft @container) (.-scrollWidth @container)) 300)))]
    (r/create-class
      {:display-name         "timeline"
       :component-did-mount  (fn []
                               (re-frame/dispatch [::state/init])
                               (scroll-to-end))
       :component-did-update (fn []
                               (scroll-to-end))
       :reagent-render
       (fn []
         (let [loading? @(re-frame/subscribe [::state/loading?])
               course-finished? @(re-frame/subscribe [::state/course-finished?])
               course-lang @(re-frame/subscribe [::state/course-language])
               finished-activities @(re-frame/subscribe [::state/finished-activities])
               handle-next-click (fn [] (re-frame/dispatch [::state/open-next-activity]))
               handle-activity-click (fn [activity] (re-frame/dispatch [::state/open-activity
                                                                        (assoc activity :new? (new-activity? activity))]))]
           [:div.timeline-wrapper {:ref #(when (some? %)
                                           (reset! container %))}
            (when-not loading?
              (into [:div.timeline]
                    (-> (->> finished-activities
                             (map-indexed vector)
                             (reduce (fn [result [idx {:keys [id activity] :as item}]]
                                       (concat result [^{:key (str id "-connector")}
                                                       (when-not (= idx 0)
                                                         [item-connector {:class-names {"top-to-bottom" (get-in item [:class-names "position-bottom"])
                                                                                        "bottom-to-top" (get-in item [:class-names "position-top"])}}])
                                                       ^{:key id}
                                                       [timeline-item (merge item
                                                                             {:on-click handle-activity-click
                                                                              :new?     (new-activity? activity)})]]))
                                     []))
                        (concat [(when-not (empty? finished-activities)
                                   ^{:key "play-button-connector"}
                                   [button-connector {:class-names {"bottom-to-top" (get-in (last finished-activities) [:class-names "position-bottom"])
                                                                    "top-to-bottom" (get-in (last finished-activities) [:class-names "position-top"])}}])
                                 ^{:key "play-button"}
                                 (if course-finished?
                                   [finish-button {:lang course-lang}]
                                   [play-button {:on-click handle-next-click}])]))))]))})))
