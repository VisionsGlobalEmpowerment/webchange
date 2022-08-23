(ns webchange.admin.widgets.page.block.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn- header-actions-component
  [{:keys [actions]}]
  (when-not (empty? actions)
    [:div {:class-name (get-class-name {"block--actions" true})}
     (for [[idx action] (map-indexed vector actions)]
       ^{:key idx}
       [ui/button (merge {:color "blue-1"}
                         action)])]))

(defn- header-tabs-component
  [{:keys [active-tab tabs on-tab-click]}]
  (when-not (empty? tabs)
    [:div {:class-name (get-class-name {"block--tabs" true})}
     (for [[idx {:keys [title]}] (map-indexed vector tabs)]
       ^{:key idx}
       [ui/button {:color      "transparent"
                   :shape      "rounded"
                   :on-click   #(on-tab-click idx)
                   :class-name (get-class-name {"block--tabs--button"         true
                                                "block--tabs--button--active" (= idx active-tab)})}
        title])]))

(defn- header-component
  [{:keys [actions header icon icon-color on-icon-click on-title-click subtitle tabs title] :as props}]
  (let [show-header? (or (some? icon)
                         (some? title)
                         (some? subtitle)
                         (some? header)
                         (some? tabs)
                         (some? actions))]
    (when show-header?
      [:div {:class-name (get-class-name {"block--header" true})}
       (when (some? icon)
         (if on-icon-click
           [ui/button (cond-> {:icon     icon
                               :on-click on-icon-click}
                              (some? icon-color) (assoc :color icon-color))]
           [ui/icon {:icon icon}]))
       [:div (cond-> {:class-name (get-class-name {"title-text"            true
                                                   "title-text--clickable" (fn? on-title-click)})}
                     (fn? on-title-click) (assoc :on-click on-title-click))
        title
        (when (some? subtitle)
          [:div.subtitle-text subtitle])]
       [:div.block--header--content
        (when (some? header)
          header)]
       [header-tabs-component props]
       [header-actions-component props]])))

(defn- footer-component
  [{:keys [footer]}]
  (when-not (empty? footer)
    [:div {:class-name (get-class-name {"block--footer"                                true
                                        (str "block--footer--columns-" (count footer)) true})}
     (for [[idx action] (map-indexed vector footer)]
       ^{:key idx}
       [ui/button action])]))

(defn block
  [{:keys [class-name class-name-content focused? tabs] :as props}]
  (r/with-let [active-tab (r/atom 0)
               handle-tab-click #(reset! active-tab %)]
    [:<>
     (when focused?
       [ui/focus-overlay])
     [:div {:class-name (get-class-name {"widget--side-bar-page--block"          true
                                         "widget--side-bar-page--block--focused" focused?
                                         class-name                              (some? class-name)})}
      [header-component (assoc props :active-tab @active-tab
                                     :on-tab-click handle-tab-click)]
      (into [:div {:class-name (get-class-name {"block--content"   true
                                                class-name-content (some? class-name-content)})}]
            (if (empty? tabs)
              (->> (r/current-component)
                   (r/children))
              [(-> tabs (nth @active-tab) (get :component))]))
      [footer-component props]]]))
