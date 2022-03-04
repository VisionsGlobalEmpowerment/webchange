(ns webchange.editor-v2.course-dashboard.views-concepts
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.concepts.subs :as concepts-subs]
    [webchange.editor-v2.components.card.views :refer [list-card get-styles]]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]))

(defn concepts-list
  [{:keys [title]}]
  (let [course @(re-frame/subscribe [::subs/current-course])
        concepts (->> @(re-frame/subscribe [::concepts-subs/dataset-items]) (sort-by :name))
        styles (get-styles)]
    [list-card {:title        title
                :full-height  true
                :on-add-click #(redirect-to :course-editor-v2-add-concept :id course)}
     [ui/list {:style (:list-full-height styles)}
      (for [concept concepts]
        ^{:key (:id concept)}
        [ui/list-item
         [ui/list-item-text {:primary (:name concept)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:aria-label "Edit"
                           :size       "small"
                           :on-click   #(redirect-to :course-editor-v2-concept :id course :concept-id (:id concept))}
           [ic/edit {:style (:action-icon styles)}]]
          [ui/icon-button {:on-click   #(re-frame/dispatch [::concepts-events/open-delete-dataset-item-modal concept])
                           :aria-label "Delete"}
           [ic/delete {:style (:action-icon styles)}]]]])]]))
