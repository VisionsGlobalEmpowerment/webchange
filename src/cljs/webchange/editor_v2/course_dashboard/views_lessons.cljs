(ns webchange.editor-v2.course-dashboard.views-lessons
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.editor-v2.components.card.views :refer [list-card] :as list]))

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

(defn lessons-list
  [{:keys [title]}]
  (let [levels @(re-frame/subscribe [::subs/course-levels])]
    [list-card {:title title
                :full-height  true}
     (for [level levels]
        ^{:key (:level level)}
        [level-item level])]))
