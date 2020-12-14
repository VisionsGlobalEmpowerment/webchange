(ns webchange.editor-v2.course-table.fields.skills.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.fields.skills.state :as skills-state]))

(defn edit-form
  []
  (let [skills @(re-frame/subscribe [::skills-state/skills-list])
        handle-item-click (fn [{:keys [id selected?]}]
                            (if selected?
                              (re-frame/dispatch [::skills-state/remove-selected-skill id])
                              (re-frame/dispatch [::skills-state/add-selected-skill id])))
        handle-save (fn []
                      (re-frame/dispatch [::skills-state/save-skills]))]
    [ui/list
     (for [{:keys [id name abbr selected?] :as skill} skills]
       ^{:key id}
       [ui/list-item {:dense    true
                      :button   true
                      :on-click #(handle-item-click skill)}
        [ui/checkbox {:checked        selected?
                      :disable-ripple true
                      :style          {:padding 0}}]
        [ui/list-item-text {:primary   name
                            :secondary abbr}]])]))
