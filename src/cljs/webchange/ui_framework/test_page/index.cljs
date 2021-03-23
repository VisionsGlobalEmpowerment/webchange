(ns webchange.ui-framework.test-page.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.index :refer [button icon icon-button]]))

(defn- row
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-row
     [:span.test-page-row-title
      title ":"]
     (into [:div.test-page-row-wrapper]
           (r/children this))]))

(defn- block
  [{:keys [title]}]
  (let [this (r/current-component)]
    [:div.test-page-element
     (when (some? title)
       [:span.test-page-element-title title])
     (into [:div.test-page-element-wrapper]
           (r/children this))]))

(defn- table
  [{:keys [columns rows data]}]
  [:table
   (when (some? columns)
     [:thead
      [:tr
       (when (some? rows) [:th])
       (for [[column-index column-title] (map-indexed vector columns)]
         ^{:key column-index}
         [:th column-title])]])
   [:tbody
    (for [[row-index row] (map-indexed vector data)]
      ^{:key row-index}
      [:tr
       (when (some? rows)
         [:td (str (nth rows row-index))])
       (for [[cell-index cell] (map-indexed vector row)]
         ^{:key cell-index}
         [:td cell])])]])

(defn- generate-controls-table
  [{:keys [control children common-props props]
    :or   {children     []
           common-props {}}}]
  (let [[first-prop second-prop] props]
    (map (fn [first-prop-value]
           (map (fn [second-prop-value]
                  (print "> " (-> {}
                                  (assoc (:name first-prop) first-prop-value)
                                  (assoc (:name second-prop) second-prop-value)))
                  [block
                   (into [control (merge common-props
                                         (-> {}
                                             (assoc (:name first-prop) first-prop-value)
                                             (assoc (:name second-prop) second-prop-value)))]
                         children)])
                (:data second-prop)))
         (:data first-prop))))

(defn test-ui
  []
  [:div
   (let [colors ["default" "primary"]
         variants ["outlined" "contained" "rectangle"]
         sizes ["medium" "big"]
         disabled? [true false]]

     [row {:title "Button"}
      [table {:columns variants
              :rows    colors
              :data    (generate-controls-table {:control  button
                                                 :children ["Button"]
                                                 :props    [{:name :color
                                                             :data colors}
                                                            {:name :variant
                                                             :data variants}]})}]
      [table {:columns variants
              :rows    sizes
              :data    (generate-controls-table {:control  button
                                                 :children ["Button"]
                                                 :props    [{:name :size
                                                             :data sizes}
                                                            {:name :variant
                                                             :data variants}]})}]
      [table {:columns colors
              :rows    disabled?
              :data    (generate-controls-table {:control  button
                                                 :children ["Button"]
                                                 :props    [{:name :disabled?
                                                             :data disabled?}
                                                            {:name :color
                                                             :data colors}]})}]])
   [row {:title "Icon"}
    [block {:title "With rotation"}
     [icon {:icon    "sync"
            :rotate? true}]]]
   [row {:title "Icon Button"}
    [block {:title "Default"}
     [icon-button {:icon "arrow-left"}]]
    [block {:title "Primary Color"}
     [icon-button {:icon  "mic"
                   :color "primary"}]]
    [block {:title "Disabled"}
     [icon-button {:icon      "arrow-right"
                   :disabled? true}]]
    [block {:title "Outlined"}
     [icon-button {:icon    "link"
                   :variant "outlined"}]]
    [block {:title "Play button"}
     [icon-button {:icon  "play"
                   :color "primary"}]]
    [block {:title "With text"}
     [icon-button {:icon    "link"
                   :variant "outlined"}
      "Copy Link"]]]])
