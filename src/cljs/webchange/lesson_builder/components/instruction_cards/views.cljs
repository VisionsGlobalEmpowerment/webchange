(ns webchange.lesson-builder.components.instruction-cards.views
  (:require
    [webchange.ui.index :as ui]))

(defn- card
  [{:keys [action content idx icon show-indexes? total-cards]}]
  [:div {:class-name (ui/get-class-name {"instruction-cards--card"                     true
                                         (str "card--background-" total-cards "-" idx) true})}
   (when show-indexes?
     [:div.card--idx
      idx])
   [:div.card--icon-container
    (when (some? icon)
      [ui/icon {:icon       icon
                :class-name "card--icon"}])]
   [:div.card--text
    content]
   (when (some? action)
     [ui/button (merge {:shape      "rounded"
                        :class-name "card--button"}
                       action)])])

(defn instruction-cards
  [{:keys [background-variant data show-indexes?]
    :or   {background-variant 1
           show-indexes?      false}}]
  [:div {:class-name (ui/get-class-name {"component--instruction-cards"                                               true
                                         (str "component--instruction-cards--columns-" (count data))                  true
                                         (str "component--instruction-cards--background-variant-" background-variant) true})}
   (for [[idx card-data] (map-indexed vector data)]
     ^{:key idx}
     [card (merge {:idx           (inc idx)
                   :total-cards   (count data)
                   :show-indexes? show-indexes?}
                  card-data)])])
