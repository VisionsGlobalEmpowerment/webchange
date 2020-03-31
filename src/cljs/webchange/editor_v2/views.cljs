(ns webchange.editor-v2.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.scene.views-data :refer [get-scenes-options]]
    [webchange.ui.theme :refer [with-mui-theme]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.events :as editor-events]
    [webchange.routes :refer [redirect-to]]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form delete-dataset-item-modal]]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.lessons.views :refer [lessons edit-lesson-form add-lesson-form]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.layout.card.views :refer [list-card]]
    [webchange.editor-v2.scene.views :refer [scene-translate]]))

;; ToDo: change card title and action buttons style
;; ToDo: extract to separate files concepts, scenes and levels components

(defn- get-styles
  []
  {:main-content     {:height "100%"
                      :margin "0"
                      :width  "100%"}
   :list-full-height {:overflow "auto"
                      :position "absolute"
                      :width    "100%"
                      :height   "100%"}})

(defn- concepts
  []
  (let [course @(re-frame/subscribe [::subs/current-course])
        concepts (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
        styles (get-styles)]
    [list-card {:title        "Concepts"
                :full-height  true
                :on-add-click #(redirect-to :course-editor-v2-add-concept :course-id course)}
     [ui/list {:style (:list-full-height styles)}
      (for [concept concepts]
        ^{:key (:id concept)}
        [ui/list-item
         [ui/list-item-text {:primary (:name concept)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:on-click #(redirect-to :course-editor-v2-concept :course-id course :concept-id (:id concept)) :aria-label "Edit"}
           [ic/edit]]
          [ui/icon-button {:on-click #(re-frame/dispatch [::concepts-events/open-delete-dataset-item-modal concept]) :aria-label "Delete"}
           [ic/delete]]]])]]))

(defn- scenes
  []
  (let [course @(re-frame/subscribe [::subs/current-course])
        scenes @(re-frame/subscribe [::subs/course-scenes])
        scenes-options (get-scenes-options scenes)
        styles (get-styles)]
    [list-card {:title       "Scenes"
                :full-height true}
     [ui/list {:style (:list-full-height styles)}
      (for [scene scenes-options]
        ^{:key (:value scene)}
        [ui/list-item
         [ui/list-item-text {:primary (:text scene)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:on-click #(redirect-to :course-editor-v2-scene :id course :scene-id (:value scene)) :aria-label "Edit"}
           [ic/edit]]]])]]))

(defn add-lesson-view
  [course-id level]
  [layout {:title "Add lesson"}
   [add-lesson-form course-id level]])

(defn lesson-view
  [course-id level lesson]
  [layout {:title "Edit lesson"}
   [edit-lesson-form course-id level lesson]])

(defn add-concept-view
  [course-id]
  [layout {:title "Add dataset item"}
   [add-dataset-item-form course-id]])

(defn concept-view
  [course-id concept-id]
  [layout {:title "Edit dataset item"}
   [edit-dataset-item-form course-id concept-id]])

(defn scene-view
  []
  [layout {:title "Scene"}
   [scene-translate]])

(defn- upload-image-form
  [uploading-atom on-change]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        start-upload (fn [js-file]
                       (reset! uploading-atom true)
                       (re-frame/dispatch [::concepts-events/upload-asset js-file {:type :image :on-finish on-finish}]))]
    [select-file-form {:size      "normal"
                       :on-change start-upload}]))

(defn- course-info
  []
  (let [loading @(re-frame/subscribe [:loading])]
    (if (:course-info loading)
      [ui/circular-progress]
      (r/with-let [info @(re-frame/subscribe [::editor-subs/course-info])
                   data (r/atom info)
                   uploading (r/atom false)]
                  [ui/card {:style {:margin      "12px"
                                    :flex-shrink "0"}}
                   [ui/card-content
                    [ui/grid {:container   true
                              :justify     "space-between"
                              :spacing     24
                              :align-items "center"}
                     [ui/grid {:item true :xs 4}
                      [ui/text-field {:label         "Name"
                                      :full-width    true
                                      :variant       "outlined"
                                      :default-value (:name @data)
                                      :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]
                     [ui/grid {:item true :xs 4}
                      [ui/text-field {:label         "Language"
                                      :full-width    true
                                      :variant       "outlined"
                                      :default-value (:lang @data)
                                      :on-change     #(swap! data assoc :lang (-> % .-target .-value))}]]
                     [ui/grid {:item true :xs 4}
                      [ui/grid {:container   true
                                :justify     "flex-start"
                                :align-items "center"}
                       (if (:image-src @data)
                         [ui/avatar {:style {:width        60
                                             :height       60
                                             :margin-right "15px"}
                                     :src   (:image-src @data)}]
                         [ui/avatar {:style {:width        60
                                             :height       60
                                             :margin-right "15px"}}
                          [ic/image]])
                       [ui/text-field {:style     {:display "none"
                                                   :width   "50%"}
                                       :value     (str (:image-src @data))
                                       :on-change #(swap! data assoc :image-src (-> % .-target .-value))}]
                       [upload-image-form uploading #(swap! data assoc :image-src %)]
                       (when @uploading
                         [ui/circular-progress])]]

                     ]]
                   [ui/card-actions
                    [ui/button {:color    "secondary"
                                :style    {:margin-left "auto"}
                                :on-click #(re-frame/dispatch [::editor-events/edit-course-info @data])}
                     "Save"]
                    (when (:edit-course-info loading)
                      [ui/circular-progress])]]))))

(defn course-view
  []
  (let [styles (get-styles)]
    [layout {:title "Course"}
     [:div {:style {:height         "100%"
                    :display        "flex"
                    :flex-direction "column"}}
      [course-info]
      [ui/grid {:container true
                :justify   "space-between"
                :spacing   24
                :style     (:main-content styles)}
       [ui/grid {:item true
                 :xs   4}
        [concepts]]
       [ui/grid {:item true
                 :xs   4}
        [scenes]]
       [ui/grid {:item true
                 :xs   4}
        [lessons]]]]]))
