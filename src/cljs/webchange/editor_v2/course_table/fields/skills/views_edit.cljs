(ns webchange.editor-v2.course-table.fields.skills.views-edit
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.skills.state :as skills-state]))

(defn selected-skills-list
  [{:keys [skills show-field on-delete]}]
  [ui/list {:class-name "selected-skills-list"}
   (for [{:keys [id] :as skill} skills]
     ^{:key id}
     [ui/list-item {:dense true
                    :style (if (= show-field :name) {:margin-bottom "8px"} {})}
      [ui/list-item-text {:primary                (get skill show-field)
                          :primaryTypographyProps {:style {:padding-right "28px"}}}]
      [ui/list-item-secondary-action
       (when (some? on-delete)
         [ui/icon-button {:on-click #(on-delete id)}
          [ic/close]])]])])

(defn- add-skill-control
  [{:keys [skills show-field on-select]}]
  (let [handle-change (fn [event] (-> event (.. -target -value) (on-select)))]
    [ui/select
     {:value         ""
      :display-empty true
      :on-change     handle-change
      :on-wheel      #(.stopPropagation %)
      :style         {:margin       "0"
                      :font-size    "12px"
                      :padding-left "8px"
                      :width        "100%"}}
     [ui/menu-item {:value ""} "Add Skill"]
     (for [{:keys [id] :as skill} skills]
       ^{:key id}
       [ui/menu-item {:value id}
        (get skill show-field)])]))

(defn edit-form
  [{:keys [data field]}]
  (r/with-let [component-id (keyword (:idx data))
               _ (re-frame/dispatch [::skills-state/init data component-id])]
    (let [skills @(re-frame/subscribe [::skills-state/skills-list component-id])
          current-skills @(re-frame/subscribe [::skills-state/current-skills component-id])
          handle-delete (fn [skill-id] (re-frame/dispatch [::skills-state/remove-selected-skill skill-id component-id]))
          handle-add (fn [skill-id] (re-frame/dispatch [::skills-state/add-selected-skill skill-id component-id]))]
      [:div
       [selected-skills-list {:skills     current-skills
                              :show-field field
                              :on-delete  handle-delete}]
       [add-skill-control {:skills     skills
                           :show-field field
                           :on-select  handle-add}]])
    (finally
      (re-frame/dispatch [::skills-state/save component-id]))))
