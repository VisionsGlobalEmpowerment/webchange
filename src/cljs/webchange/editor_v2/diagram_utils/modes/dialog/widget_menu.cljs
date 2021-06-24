(ns webchange.editor-v2.diagram-utils.modes.dialog.widget-menu
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.dialog.utils.dialog-action :as actions-defaults]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as dialog.au]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))


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
  (let [current-target (-> (actions-defaults/get-inner-action (:data number)) (get :target))
        action (cond-> action
                       (some? current-target) (actions-defaults/update-inner-action {:target current-target}))]
    (re-frame/dispatch [::dialog-form.actions/add-new-scene-action action relative-position number node path])))

(defn- add-concept-action
  [relative-position number]
  (let [current-target (-> (:data number) (actions-defaults/get-inner-action ) (get :target))
        action-data (cond-> {}
                       (some? current-target) (assoc :target current-target))]
    (re-frame/dispatch [::dialog-form.actions/add-new-phrase-concept-action relative-position number action-data])))

(defn- add-in-concept-action
  [action relative-position node]
  (re-frame/dispatch [::dialog-form.concepts/add-new-phrase-in-concept-action action node relative-position]))

(defn- add-new-phrase-in-concept-parallel-action
  [action node]
  (re-frame/dispatch [::dialog-form.concepts/add-in-concept-parallel-action action node]))

(defn- delete-in-concept-action
  [node]
  (re-frame/dispatch [::dialog-form.concepts/delete-phrase-in-concept-action node]))

(def actions
  {:insert-before                   {:text    "Add activity before"
                                     :handler (partial add-action actions-defaults/default-action :before)}
   :insert-after                    {:text    "Add activity after"
                                     :handler (partial add-action actions-defaults/default-action :after)}
   :add-parallel-activity           {:text    "Add parallel activity"
                                     :handler (partial add-parallel-action actions-defaults/default-action)}
   :insert-concept-before           {:text    "Add concept before"
                                     :handler (partial add-concept-action :before)}
   :insert-concept-after            {:text    "Add concept after"
                                     :handler (partial add-concept-action :after)}
   :insert-text-animation-before    {:text    "Add text animation before"
                                     :handler (partial add-action actions-defaults/text-animation-action :before)}
   :insert-text-animation-after     {:text    "Add text animation after"
                                     :handler (partial add-action actions-defaults/text-animation-action :after)}
   :delete                          {:text    "Delete"
                                     :handler (partial delete-action)}

   :insert-in-concept-action-after  {:text    "Add in-concept action after"
                                     :handler (partial add-in-concept-action actions-defaults/default-action :after)}
   :insert-in-concept-action-before {:text    "Add in-concept action before"
                                     :handler (partial add-in-concept-action actions-defaults/default-action :before)}
   :add-in-concept-parallel-action  {:text    "Add in-concept parallel action"
                                     :handler (partial add-new-phrase-in-concept-parallel-action actions-defaults/default-action)}
   :delete-in-concept-action        {:text    "Delete in-concept action"
                                     :handler (partial delete-in-concept-action)}})
(defn- available-menu-items
  [node]
  (let [{:keys [concept-action? base-action]} (dialog.au/get-concept-node-data node)
        has-concepts? @(re-frame/subscribe [::translator-form.concepts/has-concepts?])
        scene-contains-texts? (->> @(re-frame/subscribe [::translator-form.scene/text-objects]) (empty?) (not))
        concept-list-position (get-in node [:path 1])
        items (count (:data base-action))
        concept-items (if concept-action?
                        [:insert-in-concept-action-before
                         :insert-in-concept-action-after
                         :add-in-concept-parallel-action]
                        [])
        non-concept-items (if (not concept-action?)
                            (cond-> [:add-parallel-activity
                                     :delete
                                     :insert-before
                                     :insert-after]
                                    has-concepts? (concat [:insert-concept-before
                                                           :insert-concept-after]))
                            [])
        text-animation-items (if scene-contains-texts?
                               [:insert-text-animation-before
                                :insert-text-animation-after]
                               [])
        margin-before (if (and concept-action? (or (= 1 items) (= 0 concept-list-position)))
                        [:insert-concept-before :insert-before]
                        [])
        margin-after (if (and concept-action? (or (= 1 items) (= concept-list-position (- items 1))))
                       [:insert-concept-after :insert-after]
                       [])
        concept-delete (if concept-action?
                         (if (dialog.au/delete-in-concept-available? node)
                           [:delete-in-concept-action]
                           [:delete])
                         [])]
    (concat margin-before non-concept-items text-animation-items concept-items margin-after concept-delete)))

(defn available-actions-menu
  [node]
  (let [{:keys [concept-action? base-action]} (dialog.au/get-dialog-node-data node)
        available-activities (:available-activities base-action)]
    (->> (concat (flatten (map (fn [activity]
                                 (map (fn [pos] [
                                                 {:key     (keyword (str "insert-" activity "-" pos))
                                                  :text    (str "Add effect " activity " " pos)
                                                  :handler (if concept-action?
                                                             (partial add-in-concept-action (actions-defaults/get-effect-action-data {:action-name activity}) (keyword pos))
                                                             (partial add-action (actions-defaults/get-effect-action-data {:action-name activity}) (keyword pos)))}])
                                      ["before", "after"]))
                               available-activities))
                 (map (fn [activity]
                        {:key     (keyword (str "insert-" activity "-parallel"))
                         :text    (str "Add effect " activity " parallel")
                         :handler (if concept-action?
                                    (partial add-new-phrase-in-concept-parallel-action (actions-defaults/get-effect-action-data {:action-name activity}))
                                    (partial add-parallel-action (actions-defaults/get-effect-action-data {:action-name activity})))})
                      available-activities))
         (map (fn [item] [(:key item) item]))
         (into {}))))

(defn menu
  [node]
  (r/with-let [menu-anchor (r/atom nil)]
    (let [close-menu #(reset! menu-anchor nil)
          available-actions (->> (available-menu-items node)
                                 (select-keys actions)
                                 (merge (available-actions-menu node)))
          available-actions (sort (fn [a b] (compare (:text (last a)) (:text (last b)))) available-actions)]
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
