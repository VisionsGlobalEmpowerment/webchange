(ns webchange.admin.pages.courses.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.courses.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.auth.state :as auth]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- type-selector
  []
  (let [selected-type @(re-frame/subscribe [::state/selected-type])
        courses-counter @(re-frame/subscribe [::state/courses-counter])]
    [:div {:class-name "type-selector"}
     [:div {:class-name (ui/get-class-name {"type-selector-item"        true
                                            "type-selector-item-active" (= selected-type :published)})
            :on-click   #(re-frame/dispatch [::state/select-type :published])}
      "Global library"
      [ui/chip {:counter (:published courses-counter)}]]
     [:div.type-spacer]
     [:div {:class-name (ui/get-class-name {"type-selector-item"        true
                                            "type-selector-item-active" (= selected-type :my)})
            :on-click   #(re-frame/dispatch [::state/select-type :my])}
      "My Courses"
      [ui/chip {:counter (:my courses-counter)}]]]))

(defn- duplicate-window
  []
  (let [{:keys [open? in-progress?]} @(re-frame/subscribe [::state/duplicate-window-state])
        current-language @(re-frame/subscribe [::state/duplicate-window-selected-language])
        handle-select-language #(re-frame/dispatch [::state/duplicate-window-select-language %])
        duplicate #(re-frame/dispatch [::state/duplicate-selected-course])
        close-window #(re-frame/dispatch [::state/close-duplicate-window])]
    [ui/confirm {:title "Duplicate course?"
                 :open?        open?
                 :loading?     in-progress?
                 :confirm-text "Duplicate course"
                 :on-confirm   duplicate
                 :on-cancel    close-window}
     [ui/select {:label      "Language"
                 :value      current-language
                 :options    language-options
                 :on-change  handle-select-language
                 :class-name "language-selector"}]]))

(defn- courses-list-item
  [{:keys [id name slug lang metadata]}]
  (let [handle-click #(re-frame/dispatch [::state/open-course slug])
        handle-edit-click #(re-frame/dispatch [::state/edit-course slug])
        handle-duplicate-click #(re-frame/dispatch [::state/duplicate-course id lang])
        course-locked? (get metadata :locked false)
        super-admin? @(re-frame/subscribe [::auth/super-admin?])]
    [ui/list-item {:name     name
                   :info     [{:key   "Language"
                               :value lang}]
                   :on-click handle-click
                   :actions  [{:icon     "duplicate"
                               :title    "Duplicate"
                               :on-click handle-duplicate-click}
                              (if (and course-locked?
                                       (not super-admin?))
                                {:icon     "lock"
                                 :title    "Locked"}
                                {:icon     "edit"
                                 :title    "Edit"
                                 :on-click handle-edit-click})]}]))

(defn- courses-list
  []
  (let [loading? @(re-frame/subscribe [::state/courses-loading?])
        courses @(re-frame/subscribe [::state/courses])]
    (if loading?
      [ui/loading-overlay]
      [ui/list {:class-name "courses-list"}
       (for [{:keys [id] :as course} courses]
         ^{:key id}
         [courses-list-item course])])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [handle-add-click #(re-frame/dispatch [::state/add-course])
          show-my-global? @(re-frame/subscribe [::state/show-my-global?])]
      [page/single-page {:class-name "page--courses"
                         :header     {:title      "Courses"
                                      :icon       "courses"
                                      :icon-color "blue-2"
                                      :controls   [[type-selector]
                                                   [ui/switch {:class-name "show-global-selector"
                                                               :label      "Show My Global Courses"
                                                               :checked?   show-my-global?
                                                               :on-change  #(re-frame/dispatch [::state/set-show-global (not show-my-global?)])}]]
                                      :actions    [{:text     "Add Course"
                                                    :icon     "plus"
                                                    :on-click handle-add-click}]}}
       [courses-list]
       [duplicate-window]])))
