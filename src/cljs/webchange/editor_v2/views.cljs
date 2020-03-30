(ns webchange.editor-v2.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.views-data :refer [data get-scenes-options]]
    [webchange.editor-v2.views-diagram :refer [diagram]]
    [webchange.editor-v2.views-scene :refer [scene]]
    [webchange.ui.theme :refer [with-mui-theme]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.events :as editor-events]
    [webchange.routes :refer [redirect-to]]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form delete-dataset-item-modal]]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.lessons.views :refer [edit-lesson-form add-lesson-form]]
    [webchange.editor-v2.translator.translator-form.views-form-audio-upload :as upload]))

(defn- get-styles
  []
  {:main-wrapper {:height "100%"}})

(defn modal-windows
  []
  [:div
   [translator-modal]
   [delete-dataset-item-modal]])

(defn- concepts
  []
  (let [course @(re-frame/subscribe [::subs/current-course])
        concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))]
    [ui/paper
     [ui/typography {:variant "h6"} "Concepts"]
     [ui/list
      (for [concept concepts]
        ^{:key (:id concept)}
        [ui/list-item
         [ui/list-item-text {:primary (:name concept)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:on-click #(redirect-to :course-editor-v2-concept :course-id course :concept-id (:id concept)) :aria-label "Edit"}
           [ic/edit]]
          [ui/icon-button {:on-click #(re-frame/dispatch [::concepts-events/open-delete-dataset-item-modal concept]) :aria-label "Delete"}
           [ic/delete]]]])
      [ui/list-item {:button true :on-click #(redirect-to :course-editor-v2-add-concept :course-id course)}
       [ui/list-item-avatar [ui/avatar [ic/add]]]
       [ui/list-item-text "Add new"]]]]))

(defn- scenes
  []
  (let [course @(re-frame/subscribe [::subs/current-course])
        scenes @(re-frame/subscribe [::subs/course-scenes])
        scenes-options (get-scenes-options scenes)]
    [ui/paper
     [ui/typography {:variant "h6"} "Scenes"]
     [ui/list
      (for [scene scenes-options]
        ^{:key (:value scene)}
        [ui/list-item
         [ui/list-item-text {:primary (:text scene)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:on-click #(redirect-to :course-editor-v2-scene :id course :scene-id (:value scene)) :aria-label "Edit"}
           [ic/edit]]]])]]))

(defn- level-item
  [level]
  (r/with-let [in (r/atom true)]
    (let [course @(re-frame/subscribe [::subs/current-course])]
      [:div
       [ui/list-item {:button true :on-click #(swap! in not)}
        [ui/list-item-text {:primary (:name level)}]
        [ui/list-item-secondary-action
         [ui/icon-button
          (if @in
            [ic/expand-less]
            [ic/expand-more])]]]
       [ui/collapse {:in @in}
        [ui/divider]
        [ui/list
         (for [lesson (:lessons level)]
           [ui/list-item
            [ui/list-item-text {:primary (:name lesson)}]
            [ui/list-item-secondary-action
             [ui/icon-button {:on-click #(redirect-to :course-editor-v2-lesson :course-id course :level-id (:level level) :lesson-id (:lesson lesson)) :aria-label "Edit"}
              [ic/edit]]]])
         [ui/list-item {:button true :on-click #(redirect-to :course-editor-v2-add-lesson :course-id course :level-id (:level level))}
          [ui/list-item-avatar [ui/avatar [ic/add]]]
          [ui/list-item-text {:primary "Add new"}]]]
        [ui/divider]]
       ])))

(defn- lessons
  []
  (let [levels @(re-frame/subscribe [::subs/course-levels])]
    [ui/paper
     [ui/typography {:variant "h6"} "Scenes"]
     [ui/list
      (for [level levels]
        ^{:key (:level level)}
        [level-item level]
        )]
     ]))

(defn- upload-image-form
  [uploading-atom on-change]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        start-upload (fn [js-file]
                    (reset! uploading-atom true)
                    (re-frame/dispatch [::concepts-events/upload-asset js-file {:type :image :on-finish on-finish}]))]
    [upload/select-file-form {:on-change start-upload}]))

(defn- course-info
  []
  (let [loading @(re-frame/subscribe [:loading])]
    (if (:course-info loading)
      [ui/circular-progress]
      (r/with-let [info @(re-frame/subscribe [::editor-subs/course-info])
                   data (r/atom info)
                   uploading (r/atom false)]
        [ui/card {:style {:width "50%"}}
         [ui/card-header {:title "Edit course info"}]

         [ui/card-content
          [ui/text-field {:label "Name" :full-width true :default-value (:name @data) :on-change #(swap! data assoc :name (-> % .-target .-value))}]
          [ui/text-field {:label "Language" :full-width true :default-value (:lang @data) :on-change #(swap! data assoc :lang (-> % .-target .-value))}]

          [ui/grid {:container true :justify "flex-start" :align-items "flex-end"}
           (if (:image-src @data)
             [ui/avatar {:style {:width 60 :height 60} :src (:image-src @data)}]
             [ui/avatar {:style {:width 60 :height 60}} [ic/image]])
           [ui/text-field {:style {:width "50%"} :value (str (:image-src @data)) :on-change #(swap! data assoc :image-src (-> % .-target .-value))}]
           [upload-image-form uploading #(swap! data assoc :image-src %)]
           (when @uploading
             [ui/circular-progress])]]

         [ui/card-actions
          [ui/button {:style {:margin-left "auto"} :on-click #(re-frame/dispatch [::editor-events/edit-course-info @data])} "Save"]
          (when (:edit-course-info loading)
            [ui/circular-progress])]
         ]))))

(defn add-lesson-view
  [course-id level]
  [with-mui-theme "dark"
   [:div.editor-v2 {}
    [:div.page
     [ui/app-bar {:position "static"}
      [ui/toolbar
       [ui/typography {:variant "h4"} "Lesson"]]]
     [ui/paper
      [add-lesson-form course-id level]]
     ]]])

(defn lesson-view
  [course-id level lesson]
  [with-mui-theme "dark"
   [:div.editor-v2 {}
    [:div.page
     [ui/app-bar {:position "static"}
      [ui/toolbar
       [ui/typography {:variant "h4"} "Lesson"]]]
     [ui/paper
      [edit-lesson-form course-id level lesson]]
     ]]])

(defn add-concept-view
  [course-id]
  (let [styles (get-styles)]
    [:div.editor-v2 {:style (:main-wrapper styles)}
     [:div.page
      [ui/app-bar {:position "static"}
       [ui/toolbar
        [ui/typography {:variant "h4"} "Concept"]]]
      [ui/paper
       [add-dataset-item-form course-id]]]]))

(defn concept-view
  [course-id concept-id]
  (let [styles (get-styles)]
    [:div.editor-v2 {:style (:main-wrapper styles)}
     [:div.page
      [ui/app-bar {:position "static"}
       [ui/toolbar
        [ui/typography {:variant "h4"} "Concept"]]]
      [ui/paper
       [edit-dataset-item-form course-id concept-id]]]]))

(defn scene-view
  []
  (let [styles (get-styles)]
    [:div.editor-v2 {:style (:main-wrapper styles)}
     [:div.top-side
      [data]
      [scene]]
     [diagram]
     [modal-windows]]))

(defn course-view
  []
  (let [styles (get-styles)]
    [:div.editor-v2 {:style (:main-wrapper styles)}
     [:div.page
      [ui/app-bar {:position "static"}
       [ui/toolbar
        [ui/typography {:variant "h4"} "Course"]]]
      [ui/grid {:container true
                :justify   "space-between"
                :spacing 40}
       [ui/grid {:item true :xs 12}
        [course-info]]
       [ui/grid {:item true :xs 4}
        [concepts]]
       [ui/grid {:item true :xs 4}
        [scenes]]
       [ui/grid {:item true :xs 4}
        [lessons]]]]]))
