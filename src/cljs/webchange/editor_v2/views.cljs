(ns webchange.editor-v2.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.components.file-input.views :refer [select-file-form]]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.concepts.views :refer [concepts-list
                                                add-dataset-item-form
                                                edit-dataset-item-form
                                                delete-dataset-item-modal]]
    [webchange.editor-v2.lessons.views :refer [lessons edit-lesson-form add-lesson-form]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.layout.card.views :refer [list-card]]
    [webchange.editor-v2.scene.views :refer [scenes-list
                                             scene-translate]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.routes :refer [redirect-to]]))

(defn- get-styles
  []
  {:main-content     {:height "100%"
                      :margin "0"
                      :width  "100%"}
   :list-full-height {:overflow "auto"
                      :position "absolute"
                      :width    "100%"
                      :height   "100%"}})

(defn add-lesson-view
  [course-id level]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Add lesson"}]}
   [add-lesson-form course-id level]])

(defn lesson-view
  [course-id level lesson]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Edit lesson"}]}
   [edit-lesson-form course-id level lesson]])

(defn add-concept-view
  [course-id]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Add dataset item"}]}
   [add-dataset-item-form course-id]])

(defn concept-view
  [course-id concept-id]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Edit dataset item"}]}
   [edit-dataset-item-form course-id concept-id]])

(defn scene-view
  [course-id _]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Scene"}]}
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
                         [ui/circular-progress])]]]]
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
        [concepts-list]]
       [ui/grid {:item true
                 :xs   4}
        [scenes-list]]
       [ui/grid {:item true
                 :xs   4}
        [lessons]]]]]))
