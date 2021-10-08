(ns webchange.dashboard.courses.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page]]
    [webchange.dashboard.courses.subs :as courses-subs]
    [webchange.dashboard.courses.events :as courses-events]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui-framework.components.index :refer [with-confirmation]]))

(def styles
  {:add-button {:margin   16
                :width    150
                :height   40
                :position "fixed"
                :bottom   20
                :right    20}})

(defn- translate
  [path]
  (get-in {:title     "In review"
           :actions   {:approve "Approve"
                       :decline  "Decline"
                       :request-changes  "Request changes"
                       :preview "Preview"
                       :unpublish "Unpublish"}}
          path))

(defn- review-list-item
  [{:keys [id name slug image-src]}]
  (let [handle-approve #(re-frame/dispatch [::courses-events/approve id])
        handle-decline #(re-frame/dispatch [::courses-events/decline id])
        handle-confirm (fn [{:keys [message action]}]
                         (with-confirmation {:message    message
                                             :on-confirm action}))]
    [ui/table-row {:hover true}
     [ui/table-cell {} [:img {:src image-src :width 300}]]
     [ui/table-cell {} name]
     [ui/table-cell {:align "right"
                     :style {:white-space "nowrap"}}
      [ui/tooltip
       {:title (translate [:actions :approve])}
       [ui/button {:on-click  #(handle-confirm {:message "Are you sure to approve?"
                                                :action  handle-approve})}
        "Approve"]]
      [ui/tooltip
       {:title (translate [:actions :decline])}
       [ui/button {:on-click  #(handle-confirm {:message "Are you sure to decline?"
                                                :action  handle-decline})}
        "Decline"]]
      [ui/tooltip
       {:title (translate [:actions :preview])}
       [ui/button {:href     (str "/courses/" slug "/play")} "Preview"]]]]))

(defn- published-list-item
  [{:keys [id name slug image-src]}]
  [ui/table-row {:hover true}
   [ui/table-cell {} [:img {:src image-src :width 300}]]
   [ui/table-cell {} name]
   [ui/table-cell {:align "right"
                   :style {:white-space "nowrap"}}
    [ui/tooltip
     {:title (translate [:actions :unpublish])}
     [ui/button {:on-click #(re-frame/dispatch [::courses-events/unpublish id])} "Unpublish"]]
    [ui/tooltip
     {:title (translate [:actions :preview])}
     [ui/button {:href     (str "/courses/" slug "/play")} "Preview"]]]])

(defn courses-list-page
  []
  (let [courses @(re-frame/subscribe [::courses-subs/courses-list])
        books @(re-frame/subscribe [::courses-subs/books-list])
        published-courses @(re-frame/subscribe [::courses-subs/published-courses-list])
        published-books @(re-frame/subscribe [::courses-subs/published-books-list])
        is-loading? @(re-frame/subscribe [::courses-subs/courses-loading])]
    (if is-loading?
      [ui/linear-progress]
      [content-page {:title (translate [:title])}
       [:div
        [:h2 "Games"]
        [ui/table
         [ui/table-body
          (for [course courses]
            ^{:key (:id course)}
            [review-list-item course])]]]
       [:div
        [:h2 "Books"]
        [ui/table
         [ui/table-body
          (for [book books]
            ^{:key (:id book)}
            [review-list-item book])]]]
       [:div
        [:h2 "Published Games"]
        [ui/table
         [ui/table-body
          (for [course published-courses]
            ^{:key (:id course)}
            [published-list-item course])]]]
       [:div
        [:h2 "Published Books"]
        [ui/table
         [ui/table-body
          (for [book published-books]
            ^{:key (:id book)}
            [published-list-item book])]]]])))
