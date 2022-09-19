(ns webchange.lesson-builder.widgets.activity-actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as menu]
    [webchange.lesson-builder.blocks.state :as layout-state]
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
                                :icon "plus"}])
            :always (concat [
                             #_{:id   :image-text
                                :text "Add Text"
                                :icon "plus"}
                             ])
            (not flipbook?) (concat [{:id   :image-add
                                      :text "Add Image"
                                      :icon "plus"}
                                     {:id   :character-add
                                      :text "Add Character"
                                      :icon "plus"}
                                     {:id   :background-image
                                      :text "Background"
                                      :icon "plus"}])
            :always (concat [{:id   :background-music
                              :text "Background Music"
                              :icon "plus"}]))))

(re-frame/reg-event-fx
  ::handle-item-click
  (fn [{:keys [_]} [_ component-name]]
    {:dispatch (cond
                 (= component-name :add-layout) [::layout-state/open-tool :add-page]
                 (= component-name :background-image) [::layout-state/open-tool :background-image]
                 :default [::menu/open-component component-name])}))
