(ns webchange.editor-v2.lessons.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.routes :refer [redirect-to]]
    [webchange.editor-v2.lessons.subs :as lessons-subs]
    [webchange.editor-v2.lessons.events :as lessons-events]
    [webchange.editor-v2.concepts.subs :as concepts-subs]))

(defn- add-concept-to-lesson
  [items concept]
  (conj (vec items) concept))

(defn- remove-concept-from-lesson
  [items concept-name]
  (->> items
       (filter #(not= (:name %) concept-name))))

(defn- get-item
  [items name]
  (let [item (->> items
                  (filter #(= (:name %) name))
                  first)]
    (select-keys item [:id :name])))

(defn- edit-lesson-set
  [data lesson-key]
  (let [items @(re-frame/subscribe [::concepts-subs/dataset-items])
        lesson-items (-> @data :lesson-sets lesson-key :items)]
    [ui/list
     [ui/list-subheader (name lesson-key)]
     (for [item lesson-items]
       ^{:key (:name item)}
       [ui/list-item
        [ui/list-item-text {:primary (:name item)}]
        [ui/list-item-secondary-action
         [ui/icon-button {:on-click #(swap! data update-in [:lesson-sets lesson-key :items] (fn [list] (remove-concept-from-lesson list (:name item)))) :aria-label "Delete"}
          [ic/delete]]]])
     [ui/list-item
      [ui/select {:on-change #(swap! data update-in [:lesson-sets lesson-key :items] (fn [list] (add-concept-to-lesson list (get-item items (-> % .-target .-value)))))}
       (for [item items]
         [ui/menu-item {:value (:name item)} (:name item)])]]
     ]
    ))

(defn- edit-scenes-list
  [data]
  )

(defn edit-lesson-form
  [course-id level-id lesson-id]
  (let [scheme @(re-frame/subscribe [::lessons-subs/level-scheme level-id])
        lesson @(re-frame/subscribe [::lessons-subs/lesson-with-items level-id lesson-id])
        data (r/atom lesson)]
    (if lesson
      (fn [course-id level-id lesson-id]
        (let [lesson-scheme (get scheme (-> @data :type keyword))
              loading @(re-frame/subscribe [:loading])]
          [ui/card
           [ui/card-header {:title "Edit lesson"}]

           [ui/card-content
            [ui/select {:value (:type @data) :on-change #(swap! data assoc :type (-> % .-target .-value))}
             (for [[lesson-type lesson-type-data] scheme]
               [ui/menu-item {:value lesson-type} (:name lesson-type-data)])]
            [ui/text-field {:label "Name" :full-width true :default-value (:name @data) :on-change #(swap! data assoc :name (-> % .-target .-value))}]

            [ui/divider]

            [ui/grid {:container true
                      :justify   "space-between"
                      :spacing 40}
             [ui/grid {:item true :xs 6}
              (for [lesson-set (:lesson-sets lesson-scheme)]
                ^{:key (str lesson-set)}
                [edit-lesson-set data (keyword lesson-set)])]
             [ui/grid {:item true :xs 6}
              [edit-scenes-list data]]]]

           [ui/card-actions
            [ui/button {:on-click #(re-frame/dispatch [::lessons-events/edit-lesson course-id level-id lesson-id @data])} "Edit"]
            [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)} "Cancel"]
            (when (:edit-lesson loading)
              [ui/circular-progress])]
           ]))
      [ui/circular-progress])))

(defn add-lesson-form
  [course-id level]
  )