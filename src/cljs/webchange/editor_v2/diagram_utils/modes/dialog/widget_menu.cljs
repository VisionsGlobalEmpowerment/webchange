(ns webchange.editor-v2.diagram-utils.modes.dialog.widget-menu
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as dialog.au]
    ))



(defn complete-path
  [path]
  (apply concat (map (fn [item] (if (number? item) [:data item] [item])) path)))



(defn- delete-action
  [node]
  (re-frame/dispatch [::dialog-form.actions/delete-phrase-action node]))

(defn- add-parallel-action
  [action number node path]
  (re-frame/dispatch [::dialog-form.actions/add-new-phrase-parallel-action action number node path]))

(defn- add-action
  [action relative-position number node path]
  (re-frame/dispatch [::dialog-form.actions/add-new-phrase-action action relative-position number node path]))


(defn- add-concept-action
  [relative-position number node path]
  (re-frame/dispatch [::dialog-form.actions/add-new-phrase-concept-action relative-position number node path]))

(defn- add-inconcept-action
  [action relative-position node]
    (re-frame/dispatch [::dialog-form.concepts/add-new-phrase-inconcept-action action node relative-position]))

(defn- add-new-phrase-inconcept-parallel-action
  [action node]
  (re-frame/dispatch [::dialog-form.concepts/add-inconcept-parallel-action action node]))

(defn- delete-inconcept-action
  [node]
  (re-frame/dispatch [::dialog-form.concepts/delete-phrase-inconcept-action node]))

(def actions
  {:insert-before {:text "Add activity before"
                   :handler (partial add-action dialog-form.actions/default-action :before)}
   :insert-after  {:text "Add activity after"
                   :handler (partial add-action dialog-form.actions/default-action :after)}
   :add-parallel-activity  {:text "Add parallel activity"
                   :handler (partial add-parallel-action dialog-form.actions/default-action)}
   :insert-concept-before {:text "Add concept before"
                           :handler (partial add-concept-action :before)}
   :insert-concept-after  {:text "Add concept  after"
                           :handler (partial add-concept-action :after)}
   :delete        {:text "Delete"
                   :handler (partial delete-action)}

   :insert-inconcept-action-after  {:text "Add inconcept action after"
                                    :handler (partial add-inconcept-action dialog-form.actions/default-action :after)}
   :insert-inconcept-action-before  {:text "Add inconcept action before"
                                    :handler (partial add-inconcept-action dialog-form.actions/default-action :before)}
   :add-inconcept-parallel-action  {:text "Add inconcept parallel action"
                                    :handler (partial add-new-phrase-inconcept-parallel-action dialog-form.actions/default-action)}
   :delete-inconcept-action  {:text "Delete inconcept action"
                              :handler (partial delete-inconcept-action)}})
(defn availavle-menu-items
  [node]
  (let [
        {:keys [concept-action? base-action parent-action item-position]} (dialog.au/get-concept-node-data node)
        concept-list-position (get-in node [:path 1])
        items (count (:data base-action))
        concept-items (if concept-action?
                        [:insert-inconcept-action-before
                         :insert-inconcept-action-after
                         :add-inconcept-parallel-action]
                        [])
        non-concept-items (if (not concept-action?)
                            [:add-parallel-activity  :delete
                             :insert-concept-before :insert-concept-after
                             :insert-before :insert-after]
                            [])
        margin-before (if (and concept-action? (or (= 1 items) (= 0 concept-list-position)))
                        [:insert-concept-before :insert-before]
                        [])
        margin-after (if (and concept-action? (or (= 1 items) (= concept-list-position (- items 1) )))
                        [:insert-concept-after :insert-after]
                        [])
        concept-delete (if concept-action?
                  (if (and
                        (or
                          (and (dialog.au/node-parallel? parent-action) (= 0 item-position))
                          (not (dialog.au/node-parallel? parent-action)))
                        (= 1 items))
                     [:delete]
                     [:delete-inconcept-action])
                  [])]
    (concat margin-before non-concept-items concept-items margin-after concept-delete)))

(defn available-actions-menu
  [node]
  (let [{:keys [concept-action? base-action]} (dialog.au/get-dialog-node-data node)
        available-activities (:available-activities base-action)]
    (->> (concat (flatten (map (fn [activity]
                                 (map (fn [pos] [
                                                 {:key (keyword (str "insert-" activity "-" pos))
                                                  :text (str  "Add effect " activity " " pos)
                                                  :handler (if concept-action?
                                                             (partial add-inconcept-action (dialog-form.actions/get-action activity) (keyword pos))
                                                             (partial add-action (dialog-form.actions/get-action activity)  (keyword pos))
                                                             )
                                                  }
                                                 ]) ["before", "after"])
                                 ) available-activities))
                 (map (fn [activity] {:key (keyword (str "insert-" activity "-parallel"))
                                      :text (str  "Add effect " activity " parallel")
                                      :handler (if concept-action?
                                                 (partial add-new-phrase-inconcept-parallel-action (dialog-form.actions/get-action activity))
                                                 (partial add-parallel-action (dialog-form.actions/get-action activity))
                                                 )
                                      }) available-activities)
                 )

         (map (fn [item] [(:key item) item]))
         (into {})
         )))

(defn menu
  [node]
  (r/with-let [menu-anchor (r/atom nil)]
              (let [close-menu #(reset! menu-anchor nil)
                    available-actions (->> (availavle-menu-items node)
                                           (select-keys actions)
                                           (merge (available-actions-menu node))
                                           )
                    available-actions (sort (fn [a b] (compare (:text (last a)) (:text (last b)))) available-actions)
                    ]
                (when-not (empty? available-actions)
                  [:div
                   [ui/icon-button
                    {:on-click #(reset! menu-anchor (.-currentTarget %))}
                    [ic/more-vert {:style {:color "#323232"}}]]
                   [ui/menu
                    {:anchor-el @menu-anchor
                     :open      (boolean @menu-anchor)
                     :on-close  close-menu}
                    (for [[key {:keys [text handler]}] available-actions]
                      ^{:key key}
                      [ui/menu-item
                       {:on-click #(do (handler node)
                                       (close-menu))}
                       text])]]))))
