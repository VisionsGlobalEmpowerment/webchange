(ns webchange.editor-v2.lessons.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.editor-v2.layout.card.views :refer [list-card] :as list]
    [webchange.editor-v2.lessons.subs :as lessons-subs]
    [webchange.editor-v2.lessons.events :as lessons-events]
    [webchange.editor-v2.lessons.views-lesson-form :refer [lesson-form]]
    [webchange.editor-v2.concepts.subs :as concepts-subs]))

(defn- get-styles
  []
  {:actions-container {:text-align "right"}
   :cancel-button     {:margin-right "16px"}})

(defn edit-lesson-form
  [_ level-id lesson-id]
  (let [level-scheme @(re-frame/subscribe [::lessons-subs/level-scheme level-id])
        lesson @(re-frame/subscribe [::lessons-subs/lesson-with-items level-id lesson-id])
        dataset-items @(re-frame/subscribe [::concepts-subs/dataset-items])
        scenes-list @(re-frame/subscribe [::lessons-subs/scene-list])
        data (r/atom lesson)
        styles (get-styles)]
    (println "level-id" level-id)
    (println "level-scheme" level-scheme)
    (if lesson
      (fn [course-id level-id lesson-id]
        (let [lesson-scheme (get level-scheme (-> @data :type keyword))
              loading @(re-frame/subscribe [:loading])]
          [ui/grid {:container true
                    :spacing   16}
           [ui/grid {:item true :xs 12}
            [lesson-form {:data          data
                          :level-scheme  level-scheme
                          :lesson-scheme lesson-scheme
                          :dataset-items dataset-items
                          :scenes-list   scenes-list}]]
           [ui/grid {:item  true :xs 12
                     :style (:actions-container styles)}
            (when (:edit-lesson loading)
              [ui/circular-progress])
            [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)
                        :style    (:cancel-button styles)}
             "Cancel"]
            [ui/button {:color    "secondary"
                        :variant  "contained"
                        :on-click #(re-frame/dispatch [::lessons-events/edit-lesson course-id level-id lesson-id @data])}
             "Edit"]]]))
      [ui/circular-progress])))

(defn add-lesson-form
  [_ level-id]
  (if-let [level-scheme @(re-frame/subscribe [::lessons-subs/level-scheme level-id])]
    (let [data (r/atom {:type (-> level-scheme keys first name) :activities []})]
      (fn [course-id level-id]
        (let [lesson-scheme (get level-scheme (-> @data :type keyword))
              dataset-items @(re-frame/subscribe [::concepts-subs/dataset-items])
              scenes-list @(re-frame/subscribe [::lessons-subs/scene-list])
              loading @(re-frame/subscribe [:loading])
              styles (get-styles)]
          [ui/grid {:container true
                    :spacing   16}
           [ui/grid {:item true :xs 12}
            [lesson-form {:data          data
                          :level-scheme  level-scheme
                          :lesson-scheme lesson-scheme
                          :dataset-items dataset-items
                          :scenes-list   scenes-list}]]
           [ui/grid {:item  true :xs 12
                     :style (:actions-container styles)}
            (when (:add-lesson loading)
              [ui/circular-progress])
            [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)
                        :style    (:cancel-button styles)}
             "Cancel"]
            [ui/button {:color    "secondary"
                        :variant  "contained"
                        :on-click #(re-frame/dispatch [::lessons-events/add-lesson course-id level-id @data])}
             "Add"]]])))
    [ui/circular-progress]))

(defn- level-item
  [level]
  (r/with-let [in (r/atom true)]
              (let [course @(re-frame/subscribe [::subs/current-course])
                    list-styles (list/get-styles)]
                [list-card {:title        (:name level)
                            :title-action (r/as-element [ui/icon-button {:size     "small"
                                                                         :style    {:padding "5px"}
                                                                         :on-click #(swap! in not)}
                                                         (if @in
                                                           [ic/expand-less]
                                                           [ic/expand-more])])
                            :on-add-click #(redirect-to :course-editor-v2-add-lesson :course-id course :level-id (:level level))
                            :style        {:margin-bottom "24px"}}
                 (when @in
                   [ui/list
                    (for [lesson (:lessons level)]
                      ^{:key (:lesson lesson)}
                      [ui/list-item
                       [ui/list-item-text {:primary (:name lesson)}]
                       [ui/list-item-secondary-action
                        [ui/icon-button {:on-click   #(redirect-to :course-editor-v2-lesson :course-id course :level-id (:level level) :lesson-id (:lesson lesson))
                                         :aria-label "Edit"}
                         [ic/edit {:style (:action-icon list-styles)}]]]])])])))

(defn lessons
  []
  (let [levels @(re-frame/subscribe [::subs/course-levels])]
    [:div {:style {:height   "100%"
                   :position "relative"}}
     [:div {:style {:position "absolute"
                    :width    "100%"
                    :height   "100%"
                    :overflow "auto"}}
      (for [level levels]
        ^{:key (:level level)}
        [level-item level])]]))
