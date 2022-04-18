(ns webchange.editor-v2.activity-form.generic.components.info-action.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.generic.components.info-action.state :as state]
    [webchange.ui-framework.components.index :refer [button dialog circular-progress]]))

(defn- get-styles
  []
  (let [image-size 100
        image-style {:height image-size
                     :width  image-size}]
    {:file-input      {:display "none"}
     :image           image-style
     :image-clickable (merge image-style
                             {:cursor "pointer"})}))

;; Param controls

(defn- image
  [{:keys [tooltip src loading? on-click]}]
  (let [styles (get-styles)]
    (if loading?
      [ui/avatar {:style (:image styles)}
       [ui/circular-progress]]
      [ui/tooltip {:title     tooltip
                   :placement "top"}
       [ui/avatar (merge {:on-click on-click
                          :style    (:image-clickable styles)}
                         (if (some? src) {:src src} {}))
        (when (nil? src) [ic/image])]])))

(defn- update-book-preview-button
  []
  (let [uploading? @(re-frame/subscribe [::state/uploading?])]
    (if uploading?
      [ui/circular-progress]
      [button {:color    "default"
               :on-click #(re-frame/dispatch [::state/update-book-preview])}
       "Update"])))

(defn- course-image
  []
  (r/with-let [file-input (atom nil)
               styles (get-styles)]
    (let [value @(re-frame/subscribe [::state/image])
          handle-image-click #(.click @file-input)
          handle-input-change #(re-frame/dispatch [::state/upload-preview %])
          uploading? @(re-frame/subscribe [::state/uploading?])
          book? @(re-frame/subscribe [::state/book?])]
      [ui/grid {:container   true
                :justify     "flex-start"
                :align-items "center"
                :style       {:margin-top 16}
                :class-name  "course-image"}
       [ui/grid {:item       true :xs 6
                 :class-name "title"}
        [ui/typography {:variant "body1"}
         (str (if book? "Book" "Course") " Preview:")]
        (when book?
          [update-book-preview-button])]
       [ui/grid {:item       true
                 :xs         6
                 :class-name "preview-container"}
        [image {:src      value
                :loading? uploading?
                :on-click handle-image-click
                :tooltip  (str "Click to change " (if book? "book" "course") " image")}]
        [:input {:type      "file"
                 :ref       #(reset! file-input %)
                 :on-change #(-> % (.. -target -files) (.item 0) (handle-input-change))
                 :style     (:file-input styles)}]]])))

(defn- age-control
  []
  (let [value @(re-frame/subscribe [::state/age])
        handle-change #(re-frame/dispatch [::state/set-age %])
        available-ages @(re-frame/subscribe [::state/available-ages])]
    [ui/form-control {:full-width true}
     [ui/input-label "Ages"]
     [ui/select {:value     value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-ages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- category-control
  []
  (let [value @(re-frame/subscribe [::state/category])
        handle-change #(re-frame/dispatch [::state/set-category %])
        available-categories @(re-frame/subscribe [::state/book-categories])]
    [ui/form-control {:full-width true}
     [ui/input-label "Categories"]
     [ui/select {:value     value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-categories]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- genre-control
  []
  (let [value @(re-frame/subscribe [::state/genre])
        handle-change #(re-frame/dispatch [::state/set-genre %])
        available-languages @(re-frame/subscribe [::state/available-genres])]
    [ui/form-control {:full-width true}
     [ui/input-label "Genres"]
     [ui/select {:value     value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- reading-level-control
  []
  (let [value @(re-frame/subscribe [::state/reading-level])
        handle-change #(re-frame/dispatch [::state/set-reading-level %])
        available-languages @(re-frame/subscribe [::state/available-reading-levels])]
    [ui/form-control {:full-width true}
     [ui/input-label "Reading Level"]
     [ui/select {:value     value
                 :variant   "outlined"
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- tags-control
  []
  (let [value @(re-frame/subscribe [::state/tags])
        handle-change #(re-frame/dispatch [::state/set-tags %])
        available-languages @(re-frame/subscribe [::state/available-tags])]
    [ui/form-control {:full-width true}
     [ui/input-label "Tags"]
     [ui/select {:value     value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- language-control
  []
  (let [current-value @(re-frame/subscribe [::state/language])
        handle-change #(re-frame/dispatch [::state/set-language %])
        available-languages @(re-frame/subscribe [::state/available-languages])]
    [ui/form-control {:full-width true}
     [ui/input-label "Language"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- name-control
  []
  (let [value @(re-frame/subscribe [::state/name])
        handle-change #(re-frame/dispatch [::state/set-name %])]
    [ui/form-control {:full-width true}
     [ui/text-field {:label         "Name"
                     :full-width    true
                     :default-value value
                     :variant       "outlined"
                     :on-change     #(handle-change (-> % .-target .-value))
                     :style         {:margin-top 16}}]]))

(defn- book-info
  []
  [ui/grid {:container   true
            :justify     "space-between"
            :spacing     24
            :align-items "center"}
   [ui/grid {:item true :xs 6} [age-control]]
   [ui/grid {:item true :xs 6} [genre-control]]
   [ui/grid {:item true :xs 6} [reading-level-control]]
   [ui/grid {:item true :xs 6} [category-control]]
   [ui/grid {:item true :xs 12} [tags-control]]])

(defn- course-info-form
  []
  (r/with-let []
    (let [book? @(re-frame/subscribe [::state/book?])]
      [:div.course-info-form
       [ui/grid {:container   true
                 :justify     "space-between"
                 :spacing     24
                 :align-items "center"}
        [ui/grid {:item true :xs 4}
         [name-control]]
        [ui/grid {:item true :xs 4}
         [language-control]]
        [ui/grid {:item true :xs 4}
         [course-image]]
        (when book?
          [ui/grid {:item true :xs 12}
           [book-info]])]])
    (finally
      (re-frame/dispatch [::state/reset]))))

(defn- save-button
  []
  (let [save-in-progress? @(re-frame/subscribe [::state/save-in-progress?])
        handle-click #(re-frame/dispatch [::state/save])]
    [:div.save-button-container
     [button {:on-click  handle-click
              :size      "big"
              :disabled? save-in-progress?}
      "Save"]
     (when save-in-progress? [circular-progress])]))

(defn info-window
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        book? @(re-frame/subscribe [::state/book?])
        close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog {:open?      open?
               :on-close   close
               :title      (if book?
                             "Book Info"
                             "Course Info")
               :class-name "course-info-window"
               :actions    [[save-button]]}
       [course-info-form]])))
