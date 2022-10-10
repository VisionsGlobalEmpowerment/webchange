(ns webchange.lesson-builder.widgets.activity-actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.layout.menu.state :as menu]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.state-flipbook :as flipbook-state]))

(re-frame/reg-sub
  ::menu-items
  :<- [::state/flipbook?]
  (fn [flipbook?]
    (cond-> []
            flipbook? (concat [#_{:id   :edit-cover
                                  :text "Edit Cover"
                                  :icon "edit"}
                               {:id       :add-empty-page
                                :text     "Add Empty Page"
                                :icon     "plus"
                                :on-click #(re-frame/dispatch [::flipbook-state/add-page])}
                               {:id   :add-layout
                                :text "Add Layout"
                                :icon "plus"
                                :tool :add-page}
                               {:id       :add-text
                                :text     "Add Text"
                                :icon     "plus"
                                :on-click #(re-frame/dispatch [::flipbook-state/add-text])}
                               {:id   :image-add
                                :text "Add Image"
                                :icon "plus"
                                :on-click #(re-frame/dispatch [::flipbook-state/open-add-image])}])
            (not flipbook?) (concat [{:id   :image-add
                                      :text "Add Image"
                                      :icon "plus"}
                                     {:id   :character-add
                                      :text "Add Character"
                                      :icon "plus"
                                      :tool :character-add}
                                     {:id   :background-image
                                      :text "Background"
                                      :icon "plus"
                                      :tool :background-image}])
            :always (concat [{:id   :background-music
                              :text "Background Music"
                              :icon "plus"}]))))

(re-frame/reg-event-fx
  ::handle-item-click
  (fn [{:keys [_]} [_ {:keys [id tool]}]]
    {:dispatch (cond
                 (some? tool) [:layout/open-tool tool]
                 :default [::menu/open-component id])}))
