(ns webchange.book-creator.pages.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.pages.state :as state]
    [webchange.book-creator.pages.views-page-image :refer [page-image]]
    [webchange.book-creator.views-content-block :refer [content-block]]
    [webchange.editor-v2.layout.components.activity-action.state :as scene-action.events]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.components.index :refer [icon switcher]]))

(defn- get-stage-name
  [{:keys [idx]}]
  (let [index (inc idx)
        number (if (< index 10) (str "0" index) (str index))]
    (str number ". Spread")))

(defn- stage-image
  [{:keys [src stage]}]
  (r/with-let [max-size {:width  240
                         :height 115}
               size (r/atom {:width  0
                             :height 0})
               scale-image (fn [origin-size]
                             (let [scale (Math/min (/ (:width max-size) (:width origin-size))
                                                   (/ (:height max-size) (:height origin-size)))
                                   apply-scale #(-> % (* scale) (Math/round))]
                               (reset! size {:width  (apply-scale (:width origin-size))
                                             :height (apply-scale (:height origin-size))})))
               _ (let [img (js/Image.)]
                   (set! (.-onload img) (fn []
                                          (scale-image {:width  (.-width img)
                                                        :height (.-height img)})))
                   (set! (.-src img) src))]
    [:div {:class-name "stage-image-container"
           :style      @size}
     [page-image {:src src :stage stage :side "left"}]
     [page-image {:src src :stage stage :side "right"}]]))

(defn- stage-item
  [{:keys [idx img active? disabled?] :as stage}]
  (let [handle-click (fn [] (re-frame/dispatch [::state-flipbook/select-stage idx]))]
    [:div (cond-> {:class-name (get-class-name {"stage-item" true
                                                "active"     active?
                                                "disabled"   disabled?})}
                  (not disabled?) (assoc :on-click handle-click))
     [:div.img-container
      (if (some? img)
        [stage-image {:src   img
                      :stage idx}]
        [icon {:icon       "image"
               :class-name "stage-image-placeholder"}])]
     [:span (get-stage-name stage)]]))

(defn- add-stage-button
  []
  (let [add-stage-action-name :add-page
        handle-click (fn [] (re-frame/dispatch [::scene-action.events/show-actions-form add-stage-action-name {:on-success [::state-flipbook/reset-stage]}]))]
    [:button.add-stage-button {:on-click handle-click}
     [icon {:icon "add"}]]))

(defn- stages-list
  []
  (let [show-generated-pages? @(re-frame/subscribe [::state-flipbook/show-generated-pages?])
        stages @(re-frame/subscribe [::state-flipbook/stage-options {:filter-generated? (not show-generated-pages?)}])
        disabled? @(re-frame/subscribe [::state/pages-list-disabled?])]
    [:div.stages-list
     (for [{:keys [idx] :as stage} stages]
       ^{:key idx}
       [stage-item (merge stage
                          {:disabled? disabled?})])
     [add-stage-button]]))

(defn- tech-pages-switcher
  []
  (let [checked? @(re-frame/subscribe [::state-flipbook/show-generated-pages?])
        handle-change (fn [value] (re-frame/dispatch [::state-flipbook/set-show-generated-pages? value]))]
    [switcher {:label     "Display technical pages"
               :checked?  checked?
               :on-change handle-change}]))

(defn pages-block
  []
  [content-block {:title         "Pages"
                  :left-controls [tech-pages-switcher]}
   [stages-list]])
