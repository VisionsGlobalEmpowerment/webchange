(ns webchange.dashboard.courses.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page]]
    [webchange.dashboard.courses.subs :as courses-subs]
    [webchange.dashboard.courses.events :as courses-events]
    [cljs-react-material-ui.reagent :as ui]))

(def styles
  {:add-button {:margin   16
                :width    150
                :height   40
                :position "fixed"
                :bottom   20
                :right    20}})

(defn- translate
  [path]
  (get-in {:title     "Courses"
           :actions   {:approve "Approve"
                       :decline  "Decline"
                       :request-changes  "Request changes"}}
          path))

(defn- course-list-item
  [{:keys [id name]}]
  [ui/table-row {:hover true}
   [ui/table-cell {} name]
   [ui/table-cell {:align "right"
                   :style {:white-space "nowrap"}}
    [ui/tooltip
     {:title (translate [:actions :edit])}
     [ui/button {:on-click #(re-frame/dispatch [::courses-events/approve id])} "Approve"]]
    [ui/tooltip
     {:title (translate [:actions :sync])}
     [ui/button {:on-click #(re-frame/dispatch [::courses-events/request-changes id])} "Requst changes"]]
    [ui/tooltip
     {:title (translate [:actions :remove])}
     [ui/button {:on-click #(re-frame/dispatch [::courses-events/decline id])} "Decline"]]]])

(defn courses-list-page
  []
  (let [courses @(re-frame/subscribe [::courses-subs/courses-list])
        is-loading? @(re-frame/subscribe [::courses-subs/courses-loading])]
    (if is-loading?
      [ui/linear-progress]
      [content-page {:title (translate [:title])}
       [:div
        [ui/table
         [ui/table-body
          (for [course courses]
            ^{:key (:id course)}
            [course-list-item course])]]]])))
