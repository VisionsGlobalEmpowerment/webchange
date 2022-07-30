(ns webchange.lesson-builder.tools.character-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.character-add.state :as state]
    [webchange.lesson-builder.widgets.choose-character.views :refer [choose-character]]
    [webchange.ui.index :as ui]))

(defn character-add
  []
  [:div.widget--character-add
   [:h1 "Choose a Character"]
   [choose-character {:on-change #(re-frame/dispatch [::state/select-character %])}]
   [ui/button {:class-name "apply-button"
               :on-click #(re-frame/dispatch [::state/apply])}
    "Apply"]])
