(ns webchange.lesson-builder.tools.template-options.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.state :as state]
    [webchange.lesson-builder.tools.template-options.first-words-book-spreads.views :as first-words-book-spreads]
    [webchange.lesson-builder.tools.template-options.slide-riddle-rounds.views :as slide-riddle-rounds]
    [webchange.lesson-builder.tools.template-options.sandbox-digging-rounds.views :as sandbox-digging-rounds]
    [webchange.lesson-builder.tools.template-options.onset-rime-rounds.views :as onset-rime-rounds]
    [webchange.lesson-builder.tools.template-options.select-book.views :as select-book]
    [webchange.lesson-builder.tools.template-options.select-video.views :as select-video]
    [webchange.lesson-builder.tools.template-options.rhyming-sides.views :as rhyming-sides]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- note-field
  [{:keys [text]}]
  [ui/note {:text text}])

(declare option-field)

(defn- group-field
  [{:keys [label children]}]
  [:div.group
   [:h3.group-label label]
   (for [child children]
     [option-field child])])

(defn- string-field
  [{:keys [label key placeholder]}]
  (let [default-value @(re-frame/subscribe [::state/saved-field-value key])]
    [ui/input {:label         label
               :placeholder   placeholder
               :default-value default-value
               :required?     true
               :on-change     (state/handle-change-field key)}]))

(defn- image-field
  [{:keys [label key]}]
  (let [value @(re-frame/subscribe [::state/field-value key])]
    [select-image {:label     label
                   :value     (:src value)
                   :on-change #(re-frame/dispatch [::state/set-field key {:src (:url %)}])}]))

(defn- letter-lookup-field
  [{:keys [key label]}]
  (let [value @(re-frame/subscribe [::state/field-value key])
        letter-options @(re-frame/subscribe [::state/letter-options])]
    [ui/select {:label     label
                :value     value
                :required? true
                :options   letter-options
                :on-change (state/handle-change-field key)}]))

(defn- lookup-field
  [{:keys [key label options placeholder]}]
  (let [value @(re-frame/subscribe [::state/field-value key])]
    [:div.lookup-field
     [ui/input-label
      label]
     [ui/select {:value       value
                 :required?   true
                 :options     options
                 :placeholder placeholder
                 :on-change   (state/handle-change-field key)}]]))

(defn- update-template-field
  [{:keys [label]}]
  [ui/button {:class-name "update-template"
              :on-click   #(re-frame/dispatch [::state/update-template])}
   label])

(defn- option-field
  [{:keys [type] :as props}]
  (case (keyword type)
    :note [note-field props]
    :group [group-field props]
    :string [string-field props]
    :update-template [update-template-field props]
    :letter-lookup [letter-lookup-field props]
    :image [image-field props]
    :lookup [lookup-field props]
    :select-book [select-book/field props]
    :select-video [select-video/field props]
    :first-words-book-spreads [first-words-book-spreads/field props]
    :slide-riddle-rounds [slide-riddle-rounds/field props]
    :sandbox-digging-rounds [sandbox-digging-rounds/field props]
    :rhyming-sides [rhyming-sides/field props]
    :onset-rime-rounds [onset-rime-rounds/field props]))

(defn- options-panel
  []
  (let [options @(re-frame/subscribe [::state/options])]
    [:div.template-options--options-panel
     (for [{:keys [uid] :as option} options]
       ^{:key uid}
       [option-field option])]))

(defn- template-options-widget
  []
  (let [loading? @(re-frame/subscribe [::state/loading?])]
    [:div {:class-name (ui/get-class-name {"widget--template-options" true})}
     [:h1 "Template Options"]
     [options-panel]
     [ui/button {:class-name "template-options-apply"
                 :loading?   loading?
                 :on-click   #(re-frame/dispatch [::state/apply])}
      "Apply"]]))

(defn template-options
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [template-options-widget]))
