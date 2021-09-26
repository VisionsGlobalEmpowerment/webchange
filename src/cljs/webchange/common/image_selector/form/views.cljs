(ns webchange.common.image-selector.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.state :as state]
    [webchange.ui-framework.components.index :refer [checkbox label select]]))

;; (defn- tag-selector
;;   [{:keys [id]}]
;;   (let [options @(re-frame/subscribe [::state/tags-options id])
;;         handle-change (fn [{:keys [value checked?]}]
;;                         (js/console.log "----------------->>>> " id)
;;                         (js/console.log "----------------->>>> " value)
;;                         (js/console.log "----------------->>>> " checked?)
;;                         (re-frame/dispatch [::state/update-current-tags id value checked?]))]
;;     (into [:div.tag-selector]
;;           (->> options
;;                (reduce (fn [result {:keys [text value selected?]}]
;;                          (concat result [[checkbox {:value     value
;;                                                     :on-change handle-change
;;                                                     :checked?  selected?}]
;;                                          [label {:class-name "tag-label"} text]]))
;;                        [])))))

(defn- tag-selector
  [{:keys [id]}]
  (let [options (into [] @(re-frame/subscribe [::state/tags-options id]))
        handle-change (fn [{:keys [value checked?]}]
                        (re-frame/dispatch [::state/update-current-tags id value checked?]))]
    [:div.tag-selector
     (for [{:keys [text value selected?]} options]
       ^{:key value}
       [:div {:class-name (if selected? "chip-active" "chip")
              :on-click   #(handle-change {:value value
                                           :checked? (not selected?)})}
        text
        ;; (if selected?
        ;;   [:span.closebtn "×"])
        ])]))

(defn- assets-list-item
  [{:keys [on-click path thumbnail-path]}]
  (let [handle-click #(on-click path)]
    [:div {:class-name "assets-list-item"
           :on-click   handle-click}
     [:img {:src thumbnail-path}]]))

(defn- assets-list
  [{:keys [id on-click]}]
  (let [assets @(re-frame/subscribe [::state/assets id])]
    [:div.assets-list
     (if-not (empty? assets)
       (for [{:keys [id] :as asset} assets]
         ^{:key id}
         [assets-list-item (merge asset
                                  {:on-click on-click})])
       [:div.empty-list-message
        "No available images"])]))

(defn select-image-form
  [{:keys [id tags on-change]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id tags])]
    [:div.select-image-form
     [tag-selector {:id id}]
     [assets-list {:id       id
                   :on-click on-change}]]))