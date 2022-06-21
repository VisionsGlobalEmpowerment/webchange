(ns webchange.ui.components.card.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.card.background :as background]
    [webchange.ui.components.icon.views :refer [navigation-icon]]
    [webchange.ui.components.panel.views :refer [panel]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- card-wrapper
  [{:keys [actions background class-name]
    :or   {background "blue-1"}}]
  (->> (r/current-component)
       (r/children)
       (into [panel {:class-name         class-name
                     :class-name-content (get-class-name {"bbs--card--content"                               true
                                                          (str "bbs--card--content--background-" background) true
                                                          "bbs--card--content--no-actions"                   (empty? actions)})
                     :color              background}
              (when-not (= background "transparent")
                background/data)])))

(defn- card-counter
  [{:keys [counter type]}]
  [:div {:class-name (get-class-name {"bbs--card--counter"                   true
                                      (str "bbs--card--counter--type-" type) true})}
   (if (number? counter) counter "-")])

(defn- card-icon
  [{:keys [counter icon icon-background type] :as props}]
  [:div {:class-name (get-class-name {"bbs--card--icon"                                    true
                                      (str "bbs--card--icon--background-" icon-background) (some? icon-background)})}
   [navigation-icon {:icon       icon
                     :class-name "bbs--card--icon-data"}]
   (when (and (some? counter)
              (= type "vertical"))
     [card-counter props])])

(defn- card-data
  [{:keys [background text type] :as props
    :or   {background "blue-1"}}]
  [:div {:class-name (get-class-name {"bbs--card--data"                               true
                                      (str "bbs--card--data--background-" background) true})}
   (when (= type "horizontal")
     [card-counter props])
   [:div.bbs--card--text text]])

(defn- card-action
  [{:keys [on-click text] :as props}]
  (let [handle-click #(when (fn? on-click) (on-click))
        button-props (-> props
                         (dissoc :text)
                         (assoc :on-click handle-click
                                :shape "rounded"))]
    [button button-props
     text]))

(defn- card-actions
  [{:keys [actions]}]
  (when-not (empty? actions)
    [:div {:class-name "bbs--card--actions"}
     (for [[idx action-data] (map-indexed vector actions)]
       ^{:key (str "action-" idx)}
       [card-action action-data])]))

(defn- card-horizontal
  [props]
  [:<>
   [card-icon props]
   [card-data props]
   [card-actions props]])

(defn- card-vertical
  [props]
  [:<>
   [card-icon props]
   [card-data props]
   [card-actions props]])

(defn card
  [{:keys [actions background counter icon icon-background text type]
    :or   {type "horizontal"}
    :as   props}]
  {:pre [(some #{icon} available-values/icon-navigation)
         (or (nil? icon-background) (some #{icon-background} available-values/color))
         (or (nil? background) (some #{background} available-values/color))
         (or (nil? counter) (number? counter))
         (string? text)
         (some #{type} ["horizontal" "vertical"])
         (or (nil? actions) (every? #(and (string? (:text %))
                                          (fn? (:on-click %)))
                                    actions))]}
  [card-wrapper (merge props
                       {:class-name (get-class-name {"bbs--card"                               true
                                                     (str "bbs--card--" type)                  true
                                                     (str "bbs--card--background-" background) (some? background)})})
   (case type
     "horizontal" [card-horizontal props]
     "vertical" [card-vertical props])])
