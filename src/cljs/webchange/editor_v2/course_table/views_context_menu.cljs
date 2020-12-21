(ns webchange.editor-v2.course-table.views-context-menu
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.course-data-utils.events :as course-data.events]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [cell-data->cell-attributes click-event->cell-data]]))

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

(defn- menu
  [{:keys [anchor open? items on-close]}]
  [ui/menu {:open          open?
            :on-close      on-close
            :anchor-el     anchor
            :anchor-origin {:horizontal "right"
                            :vertical   "top"}}
   (for [{:keys [id title handler]} items]
     (let [[func & args] (if (sequential? handler) handler [handler])]
       ^{:key id}
       [ui/menu-item {:on-click #(do (on-close)
                                     (apply func args))}
        title]))])

(defn- handle-copy-lesson
  []
  (re-frame/dispatch [::selection-state/save-selection]))

(defn- handle-paste-lesson
  [relative-position]
  (let [current-selection @(re-frame/subscribe [::selection-state/selection])
        saved-selection @(re-frame/subscribe [::selection-state/saved-selection])]
    (re-frame/dispatch [::selection-state/reset-selection])
    (re-frame/dispatch [::selection-state/reset-saved-selection])
    (re-frame/dispatch [::course-data.events/copy-lesson {:selection-from    saved-selection
                                                          :selection-to      current-selection
                                                          :relative-position relative-position}])))

(defn- get-lesson-menu-items
  [{:keys [saved-selection]}]
  (cond-> [{:id      :copy-lesson
            :title   "Copy lesson"
            :handler handle-copy-lesson}]
          (some? saved-selection) (concat [{:id      :paste-lesson-before
                                            :title   "Paste lesson before"
                                            :handler [handle-paste-lesson :before]}
                                           {:id      :paste-lesson-after
                                            :title   "Paste lesson after"
                                            :handler [handle-paste-lesson :after]}])))

(defn- get-menu-items
  [{:keys [current-selection] :as params}]
  (cond-> []
          (= (:field current-selection) :lesson-idx) (concat (get-lesson-menu-items params))))

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
