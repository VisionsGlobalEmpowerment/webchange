(ns webchange.editor-v2.lessons.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.routes :refer [redirect-to]]
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
        level-activities @(re-frame/subscribe [::lessons-subs/level-activities level-id])
        lesson @(re-frame/subscribe [::lessons-subs/lesson-with-items level-id lesson-id])
        dataset-items @(re-frame/subscribe [::concepts-subs/dataset-items])
        data (r/atom lesson)
        styles (get-styles)]
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
                          :scenes-list   level-activities}]]
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
              level-activities @(re-frame/subscribe [::lessons-subs/level-activities level-id])
              dataset-items @(re-frame/subscribe [::concepts-subs/dataset-items])
              loading @(re-frame/subscribe [:loading])
              styles (get-styles)]
          [ui/grid {:container true
                    :spacing   16}
           [ui/grid {:item true :xs 12}
            [lesson-form {:data          data
                          :level-scheme  level-scheme
                          :lesson-scheme lesson-scheme
                          :dataset-items dataset-items
                          :scenes-list   level-activities}]]
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
