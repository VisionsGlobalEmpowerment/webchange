(ns webchange.editor-v2.activity-form.generic.components.info-action.views
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]
    [webchange.editor-v2.activity-form.generic.components.info-action.state :as info-state]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.state.state-course :as state-course]
    [webchange.subs :as subs]
    [webchange.ui-framework.components.index :refer [button dialog]]
    [webchange.utils.flipbook :refer [flipbook-activity?]]))

(defn- get-styles
  []
  (let [image-size 100
        image-style {:height image-size
                     :width  image-size}]
    {:file-input      {:display "none"}
     :image           image-style
     :image-clickable (merge image-style
                             {:cursor "pointer"})}))

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
  (let [uploading? @(re-frame/subscribe [::info-state/uploading?])]
    (if uploading?
      [ui/circular-progress]
      [button {:color    "default"
               :on-click #(re-frame/dispatch [::info-state/update-book-preview])}
       "Update"])))

(defn- course-image
  [{:keys [book?] :or {book? false}}]
  (r/with-let [file-input (atom nil)
               styles (get-styles)]
    (let [value @(re-frame/subscribe [::info-state/image])
          handle-image-click #(.click @file-input)
          handle-input-change #(re-frame/dispatch [::info-state/upload-preview %])
          uploading? @(re-frame/subscribe [::info-state/uploading?])]
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

(defn- book?
  [course-info]
  (= "book" (:type course-info)))

(defn- init-book-keywords!
  [data]
  (let [text-objects (-> @(re-frame/subscribe [::state/scene-data])
                         :objects
                         (select-keys [:page-cover-title-text :page-cover-illustrators :page-cover-authors]))
        keywords (->> text-objects
                      (map second)
                      (map :text)
                      (map #(str/split % #" "))
                      (flatten))]
    (swap! data assoc-in [:metadata :keywords] keywords)))

(defn- age-control
  [{:keys [data]}]
  (let [current-value (-> @data :metadata :ages (or []))
        handle-change #(swap! data assoc-in [:metadata :ages] %)
        available-languages @(re-frame/subscribe [::info-state/available-ages])]
    [ui/form-control {:full-width true}
     [ui/input-label "Ages"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- category-control
  [{:keys [data]}]
  (let [current-value (-> @data :metadata :categories (or []))
        available-categories @(re-frame/subscribe [::info-state/book-categories])
        handle-change #(swap! data assoc-in [:metadata :categories] %)]
    [ui/form-control {:full-width true}
     [ui/input-label "Categories"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-categories]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- genre-control
  [{:keys [data]}]
  (let [current-value (-> @data :metadata :genres (or []))
        handle-change #(swap! data assoc-in [:metadata :genres] %)
        available-languages @(re-frame/subscribe [::info-state/available-genres])]
    [ui/form-control {:full-width true}
     [ui/input-label "Genres"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- reading-level-control
  [{:keys [data]}]
  (let [current-value (-> @data :metadata :reading-level (or ""))
        handle-change #(swap! data assoc-in [:metadata :reading-level] %)
        available-languages @(re-frame/subscribe [::info-state/available-reading-levels])]
    [ui/form-control {:full-width true}
     [ui/input-label "Reading Level"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- tags-control
  [{:keys [data]}]
  (let [current-value (-> @data :metadata :tags (or []))
        handle-change #(swap! data assoc-in [:metadata :tags] %)
        available-languages @(re-frame/subscribe [::info-state/available-tags])]
    [ui/form-control {:full-width true}
     [ui/input-label "Tags"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :multiple  true
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn- book-info
  [{:keys [data]}]
  (init-book-keywords! data)
  (fn [{:keys [data]}]
    [ui/grid {:container   true
              :justify     "space-between"
              :spacing     24
              :align-items "center"}
     [ui/grid {:item true :xs 6}
      [age-control {:data data}]]
     [ui/grid {:item true :xs 6}
      [genre-control {:data data}]]
     [ui/grid {:item true :xs 6}
      [reading-level-control {:data data}]]
     [ui/grid {:item true :xs 6}
      [category-control {:data data}]]
     [ui/grid {:item true :xs 12}
      [tags-control {:data data}]]
     [ui/grid {:item true :xs 12}]]))

(defn- language-control
  []
  (let [current-value @(re-frame/subscribe [::info-state/language])
        handle-change #(re-frame/dispatch [::info-state/set-language %])
        available-languages @(re-frame/subscribe [::info-state/available-languages])]
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
  (let [value @(re-frame/subscribe [::info-state/name])
        handle-change #(re-frame/dispatch [::info-state/set-name %])]
    [ui/form-control {:full-width true}
     [ui/text-field {:label         "Name"
                     :full-width    true
                     :default-value value
                     :variant       "outlined"
                     :on-change     #(handle-change (-> % .-target .-value))
                     :style         {:margin-top 16}}]]))

(defn course-info-modal
  [{:keys [info]}]
  (r/with-let [data (r/atom info)]
    (let [course-info @(re-frame/subscribe [::info-state/course-info])]
      (print "course-info" course-info)
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
         [course-image {:data  data
                        :book? (book? @data)}]]
        (when (book? @data)
          [ui/grid {:item true :xs 12}
           [book-info {:data data}]])]])))

(defn- save-button
  [{:keys [data]}]
  (let [handle-click #(do (re-frame/dispatch [::editor-events/edit-course-info @data])
                          (re-frame/dispatch [::info-state/close]))]
    [button {:on-click handle-click
             :size     "big"}
     "Save"]))

(defn info-window
  []
  (let [open? @(re-frame/subscribe [::info-state/modal-state])
        close #(re-frame/dispatch [::info-state/close])
        course-info @(re-frame/subscribe [::state-course/course-info])
        scene-data @(re-frame/subscribe [::subs/current-scene-data])
        book? (flipbook-activity? scene-data)]
    (when open?
      [dialog {:open?    open?
               :on-close close
               :title    (if book?
                           "Book Info"
                           "Course Info")
               :actions  [[save-button]]}
       (if (empty? course-info)
         [ui/circular-progress]
         [course-info-modal {:info course-info}])])))
