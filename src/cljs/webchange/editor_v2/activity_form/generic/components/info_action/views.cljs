(ns webchange.editor-v2.activity-form.generic.components.info-action.views
  (:require
    [clojure.string :as str]
    [webchange.ui-framework.components.index :refer [dialog]]
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]
    [webchange.editor-v2.activity-form.generic.components.info-action.state :as info-state]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.state.state-course :as state-course]
    [webchange.subs :as subs]))

(defn- get-styles
  []
  (let [image-size 60
        image-style {:height       image-size
                     :margin-right "15px"
                     :width        image-size}]
    {:file-input      {:display "none"}
     :image           image-style
     :image-clickable (merge image-style
                             {:cursor "pointer"})
     :image-label     {:margin-right "16px"}}))

(defn- image
  [{:keys [src loading? on-click]}]
  (let [styles (get-styles)]
    (if loading?
      [ui/avatar {:style (:image styles)}
       [ui/circular-progress]]
      [ui/tooltip {:title     "Click to change course image"
                   :placement "top"}
       [ui/avatar (merge {:on-click on-click
                          :style    (:image-clickable styles)}
                         (if (some? src) {:src src} {}))
        (when (nil? src) [ic/image])]])))

(defn- course-image
  [{:keys [data]}]
  (r/with-let [uploading (r/atom false)
               file-input (atom nil)

               handle-image-click #(.click @file-input)
               handle-finish-upload (fn [result]
                                      (reset! uploading false)
                                      (swap! data assoc :image-src (:url result)))
               handle-start-upload (fn [js-file]
                                     (reset! uploading true)
                                     (re-frame/dispatch [::assets-events/upload-asset js-file {:type      :image
                                                                                               :on-finish handle-finish-upload}]))
               handle-input-change #(-> % (.. -target -files) (.item 0) handle-start-upload)
               styles (get-styles)]
    [ui/grid {:container   true
              :justify     "flex-start"
              :align-items "center"}
     [ui/typography {:variant "body1"
                     :style   (:image-label styles)}
      "Course Image"]
     [image {:src      (:image-src @data)
             :loading? @uploading
             :on-click handle-image-click}]
     [:input {:type      "file"
              :ref       #(reset! file-input %)
              :on-change handle-input-change
              :style     (:file-input styles)}]]))

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
    [ui/form-control {:full-width true
                      :margin     "normal"
                      :style      {:margin-top 0}}
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
  [data]
  (when (book? @data)
    (let [uploading (atom false)
          handle-finish-upload (fn [result]
                                 (reset! uploading false)
                                 (swap! data assoc :image-src (:url result)))
          handle-start-upload (fn [blob]
                                (reset! uploading true)
                                (re-frame/dispatch [::assets-events/upload-asset blob {:type      :image
                                                                                       :options   {:max-width 384 :max-height 432}
                                                                                       :on-finish handle-finish-upload}]))]
      (init-book-keywords! data)
      (fn [data]
        [:div
         [age-control {:data data}]
         [category-control {:data data}]
         [genre-control {:data data}]
         [reading-level-control {:data data}]
         [tags-control {:data data}]
         (if @uploading
           [ui/circular-progress]
           [ui/button {:color    "secondary"
                       :style    {:margin-left "auto"}
                       :on-click #(re-frame/dispatch [::info-state/update-book-preview handle-start-upload])}
            "Update book preview"])]))))

(defn- language-control
  [{:keys [data]}]
  (let [current-value (:lang @data)
        handle-change #(swap! data assoc :lang %)
        available-languages @(re-frame/subscribe [::info-state/available-languages])]
    [ui/form-control {:full-width true}
     [ui/input-label "Language"]
     [ui/select {:value     current-value
                 :variant   "outlined"
                 :on-change #(handle-change (-> % .-target .-value))}
      (for [{:keys [name value]} available-languages]
        ^{:key value}
        [ui/menu-item {:value value} name])]]))

(defn course-info-modal
  [{:keys []}]
  (let [info @(re-frame/subscribe [::state-course/course-info])]
    (if (empty? info)
      [ui/circular-progress]
      (r/with-let [data (r/atom info)]
        [:div
         [ui/grid {:container   true
                   :justify     "space-between"
                   :spacing     24
                   :align-items "center"}
          [ui/grid {:item true :xs 4}
           [ui/text-field {:label         "Name"
                           :full-width    true
                           :default-value (:name @data)
                           :variant       "outlined"
                           :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]
          [ui/grid {:item true :xs 4}
           [language-control {:data data}]]
          [ui/grid {:item true :xs 4}
           [course-image {:data data}]]
          [book-info data]]
         [ui/card-actions
          [ui/button {:color    "secondary"
                      :style    {:margin-left "auto"}
                      :on-click #(do (re-frame/dispatch [::editor-events/edit-course-info @data])
                                     (re-frame/dispatch [::info-state/close]))}
           "Save"]]]))))

(defn info-window
  []
  (let [open? @(re-frame/subscribe [::info-state/modal-state])
        close #(re-frame/dispatch [::info-state/close])]
    (when open?
      [dialog {:open?    open?
               :on-close close
               :title    "Course Info"}
       [course-info-modal]])))
