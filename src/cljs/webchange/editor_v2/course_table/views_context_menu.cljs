(ns webchange.editor-v2.course-table.views-context-menu
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.course-data-utils.events :as course-data.events]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [cell-data->cell-attributes click-event->cell-data]]
    [webchange.editor-v2.course-table.utils-rows-number :refer [get-element-height]]))

(defn- init-mouse-event
  [{:keys [on-right-click]}]
  (js/document.addEventListener "contextmenu" (fn [event]
                                                (.preventDefault event)
                                                (on-right-click))))

(defn- get-current-cell
  [{:keys [container selection]}]
  (when (some? selection)
    (let [query (->> selection
                     (cell-data->cell-attributes)
                     (map (fn [[key value]] (str "[" (clojure.core/name key) "=\"" value "\"]")))
                     (clojure.string/join ""))]
      (.querySelector container query))))

(defn- handle-menu-item-click
  [handler args]
  (let [selection @(re-frame/subscribe [::selection-state/selection])]
    (re-frame/dispatch [::selection-state/reset-selection])
    (apply handler (concat [{:selection selection}] args))))

(defn- menu
  [{:keys [anchor open? items on-close]}]
  [ui/menu {:open      open?
            :on-close  on-close
            :anchor-el anchor
            :style     {:margin-top (- 30 (/ (get-element-height anchor) 2))}}
   (for [{:keys [id title handler]} items]
     (let [[handler & args] (if (sequential? handler) handler [handler])]
       ^{:key id}
       [ui/menu-item {:on-click #(do (on-close)
                                     (handle-menu-item-click handler args))}
        title]))])

(defn- handle-add-level
  [{:keys [selection]} relative-position]
  (re-frame/dispatch [::course-data.events/add-level {:selection         selection
                                                      :relative-position relative-position}]))

(defn- handle-remove-level
  [{:keys [selection]}]
  (re-frame/dispatch [::course-data.events/remove-level {:selection selection}]))

(defn- handle-copy-lesson
  [{:keys [selection]}]
  (re-frame/dispatch [::selection-state/save-selection selection]))

(defn- handle-paste-lesson
  [{:keys [selection]} relative-position]
  (let [saved-selection @(re-frame/subscribe [::selection-state/saved-selection])]
    (re-frame/dispatch [::selection-state/reset-saved-selection])
    (re-frame/dispatch [::course-data.events/copy-lesson {:selection-from    saved-selection
                                                          :selection-to      selection
                                                          :relative-position relative-position}])))

(defn- handle-add-lesson
  [{:keys [selection]} relative-position]
  (re-frame/dispatch [::course-data.events/add-lesson {:selection         selection
                                                       :relative-position relative-position}]))

(defn- handle-remove-lesson
  [{:keys [selection]}]
  (re-frame/dispatch [::course-data.events/remove-lesson {:selection selection}]))

(defn- handle-add-activity
  [{:keys [selection]} relative-position]
  (re-frame/dispatch [::course-data.events/add-activity {:selection         selection
                                                         :relative-position relative-position}]))

(defn- handle-remove-activity
  [{:keys [selection]}]
  (re-frame/dispatch [::course-data.events/remove-activity {:selection selection}]))

(defn- get-level-menu-items
  [_]
  [{:id      :add-level-before
    :title   "Add level before"
    :handler [handle-add-level :before]}
   {:id      :add-level-after
    :title   "Add level after"
    :handler [handle-add-level :after]}
   {:id      :remove-level
    :title   "Remove level"
    :handler handle-remove-level}])

(defn- get-lesson-menu-items
  [{:keys [saved-selection]}]
  (cond-> [{:id      :add-lesson-before
            :title   "Add lesson before"
            :handler [handle-add-lesson :before]}
           {:id      :add-lesson-after
            :title   "Add lesson after"
            :handler [handle-add-lesson :after]}
           {:id      :remove-lesson
            :title   "Remove lesson"
            :handler handle-remove-lesson}
           {:id      :copy-lesson
            :title   "Copy lesson"
            :handler handle-copy-lesson}]
          (some? saved-selection) (concat [{:id      :paste-lesson-before
                                            :title   "Paste lesson before"
                                            :handler [handle-paste-lesson :before]}
                                           {:id      :paste-lesson-after
                                            :title   "Paste lesson after"
                                            :handler [handle-paste-lesson :after]}])))

(defn- get-row-menu-items
  [{:keys [_]}]
  [{:id      :add-activity-before
    :title   "Add activity before"
    :handler [handle-add-activity :before]}
   {:id      :add-activity-after
    :title   "Add activity after"
    :handler [handle-add-activity :after]}
   {:id      :remove-activity
    :title   "Remove activity"
    :handler handle-remove-activity}])

(defn- get-menu-items
  [{:keys [current-selection] :as params}]
  (cond-> []
          (= (:field current-selection) :level-idx) (concat (get-level-menu-items params))
          (= (:field current-selection) :lesson-idx) (concat (get-lesson-menu-items params))
          (not (some #{(:field current-selection)} [:level-idx :lesson-idx :concepts])) (concat (get-row-menu-items params))))

(defn context-menu
  [{:keys [container]}]
  (r/with-let [handle-open-menu #(re-frame/dispatch [::selection-state/open-context-menu])
               handle-close-menu #(re-frame/dispatch [::selection-state/close-context-menu])
               _ (init-mouse-event {:on-right-click handle-open-menu})]
    (let [selection @(re-frame/subscribe [::selection-state/selection])
          saved-selection @(re-frame/subscribe [::selection-state/saved-selection])
          menu-open? @(re-frame/subscribe [::selection-state/menu-open?])]
      (when (and menu-open? (some? @container))
        (let [cell (get-current-cell {:container @container
                                      :selection selection})
              menu-items (get-menu-items {:current-selection selection
                                          :saved-selection   saved-selection})]
          (when (and (some? cell)
                     (not (empty? menu-items)))
            [menu {:open?    menu-open?
                   :on-close handle-close-menu
                   :anchor   cell
                   :items    menu-items}]))))))
