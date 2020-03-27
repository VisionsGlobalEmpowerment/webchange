(ns webchange.editor-v2.lessons.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.editor-v2.layout.card.views :refer [list-card]]
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
         [ui/menu-item {:value (:name item)} (:name item)])]]]))

(defn- add-scene
  [data scene-list]
  (r/with-let [scene-data (r/atom {})]
              [ui/table-row
               [ui/table-cell
                [ui/select {:value (:activity @scene-data) :on-change #(swap! scene-data assoc :activity (-> % .-target .-value))}
                 (for [[item-key item] scene-list]
                   [ui/menu-item {:value (name item-key)} (:name item)])]]
               [ui/table-cell
                [ui/text-field {:on-change #(swap! scene-data assoc :time-expected (-> % .-target .-value js/parseInt))}]]
               [ui/table-cell
                [ui/checkbox {:on-change #(swap! scene-data assoc :scored (-> % .-target .-checked))}]]
               [ui/table-cell
                [ui/text-field {:on-change #(swap! scene-data assoc :expected-score-percentage (-> % .-target .-value js/parseInt))}]]
               [ui/table-cell
                [ui/icon-button {:on-click   #(swap! data update :activities (fn [list] (concat list [@scene-data])))
                                 :aria-label "Add"}
                 [ic/add]]]
               [ui/table-cell]]))

(defn- scene-info
  [data scene-list scene-idx scene]
  (r/with-let [edit? (r/atom false)
               scene-data (r/atom scene)]
              (if @edit?
                [ui/table-row
                 [ui/table-cell
                  [ui/select {:value (:activity @scene-data) :on-change #(swap! scene-data assoc :activity (-> % .-target .-value))}
                   (for [[item-key item] scene-list]
                     [ui/menu-item {:value (name item-key)} (:name item)])]]
                 [ui/table-cell
                  [ui/text-field {:default-value (:time-expected @scene-data) :on-change #(swap! scene-data assoc :time-expected (-> % .-target .-value js/parseInt))}]]
                 [ui/table-cell
                  [ui/checkbox {:checked (:scored @scene-data) :on-change #(swap! scene-data assoc :scored (-> % .-target .-checked))}]]
                 [ui/table-cell
                  [ui/text-field {:default-value (:expected-score-percentage @scene-data) :on-change #(swap! scene-data assoc :expected-score-percentage (-> % .-target .-value js/parseInt))}]]
                 [ui/table-cell
                  [ui/icon-button {:on-click   #(do
                                                  (swap! data update :activities (fn [list] (map-indexed (fn [idx item] (if (= scene-idx idx) @scene-data item)) list)))
                                                  (reset! edit? false))
                                   :aria-label "Save"}
                   [ic/check]]]
                 [ui/table-cell
                  [ui/icon-button {:on-click #(reset! edit? false) :aria-label "Cancel"}
                   [ic/cancel]]]]
                [ui/table-row
                 [ui/table-cell (:activity scene)]
                 [ui/table-cell (:time-expected scene)]
                 [ui/table-cell (-> scene :scored boolean str)]
                 [ui/table-cell (:expected-score-percentage scene)]
                 [ui/table-cell
                  [ui/icon-button {:on-click #(reset! edit? true) :aria-label "Edit"}
                   [ic/edit]]]
                 [ui/table-cell
                  [ui/icon-button {:on-click #(swap! data update :activities (fn [list] (keep-indexed (fn [idx item] (if (not= scene-idx idx) item)) list))) :aria-label "Delete"}
                   [ic/delete]]]])))

(defn- edit-scenes-list
  [data]
  (let [scene-list @(re-frame/subscribe [::lessons-subs/scene-list])]
    [ui/table
     [ui/table-head
      [ui/table-row
       [ui/table-cell "Activity"]
       [ui/table-cell "Time"]
       [ui/table-cell "Scored"]
       [ui/table-cell "Score"]
       [ui/table-cell ""]
       [ui/table-cell ""]]]
     [ui/table-body
      (for [[item-idx item] (keep-indexed vector (:activities @data))]
        ^{:key item-idx}
        [scene-info data scene-list item-idx item])
      [add-scene data scene-list]]]))

(defn edit-lesson-form
  [course-id level-id lesson-id]
  (let [scheme @(re-frame/subscribe [::lessons-subs/level-scheme level-id])
        lesson @(re-frame/subscribe [::lessons-subs/lesson-with-items level-id lesson-id])
        data (r/atom lesson)
        lesson-scheme (get scheme (-> @data :type keyword))
        loading @(re-frame/subscribe [:loading])]
    (if lesson
      [ui/card
       [ui/card-content
        [ui/select {:value     (:type @data)
                    :on-change #(swap! data assoc :type (-> % .-target .-value))
                    :style     {:min-width "150px"}}
         (for [[lesson-type lesson-type-data] scheme]
           [ui/menu-item {:value lesson-type}
            (:name lesson-type-data)])]
        [ui/text-field {:label         "Name"
                        :full-width    true
                        :default-value (:name @data)
                        :on-change     #(swap! data assoc :name (-> % .-target .-value))}]
        [ui/divider]
        [ui/grid {:container true
                  :justify   "space-between"
                  :spacing   40}
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
          [ui/circular-progress])]]
      [ui/circular-progress])))

(defn add-lesson-form
  [course-id level-id]
  (if-let [scheme @(re-frame/subscribe [::lessons-subs/level-scheme level-id])]
    (let [data (r/atom {:type (-> scheme keys first name) :activities []})
          lesson-scheme (get scheme (-> @data :type keyword))
          loading @(re-frame/subscribe [:loading])]
      [ui/card
       [ui/card-content
        [ui/select {:value     (:type @data)
                    :on-change #(swap! data assoc :type (-> % .-target .-value))
                    :style     {:min-width "150px"}}
         (for [[lesson-type lesson-type-data] scheme]
           [ui/menu-item {:value lesson-type}
            (:name lesson-type-data)])]
        [ui/text-field {:label      "Name"
                        :full-width true
                        :on-change  #(swap! data assoc :name (-> % .-target .-value))}]
        [ui/divider]
        [ui/grid {:container true
                  :justify   "space-between"
                  :spacing   40}
         [ui/grid {:item true :xs 6}
          (for [lesson-set (:lesson-sets lesson-scheme)]
            ^{:key (str lesson-set)}
            [edit-lesson-set data (keyword lesson-set)])]
         [ui/grid {:item true :xs 6}
          [ui/paper
           [edit-scenes-list data]]]]]
       [ui/card-actions
        [ui/button {:on-click #(re-frame/dispatch [::lessons-events/add-lesson course-id level-id @data])} "Add"]
        [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)} "Cancel"]
        (when (:add-lesson loading)
          [ui/circular-progress])]])
    [ui/circular-progress]))

(defn- level-item
  [level]
  (r/with-let [in (r/atom true)]
              (let [course @(re-frame/subscribe [::subs/current-course])]
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
                      [ui/list-item
                       [ui/list-item-text {:primary (:name lesson)}]
                       [ui/list-item-secondary-action
                        [ui/icon-button {:on-click   #(redirect-to :course-editor-v2-lesson :course-id course :level-id (:level level) :lesson-id (:lesson lesson))
                                         :aria-label "Edit"}
                         [ic/edit]]]])])])))

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