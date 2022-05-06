(ns webchange.admin.widgets.page.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def class-names
  {:header   "widget-profile--header"
   :main     "widget-profile--main-content"
   :side-bar "widget-profile--side-bar"})

(defn- has-child?
  [el child-class-name]
  (-> el
      (.querySelector (str "." child-class-name))
      (some?)))

(defn- with-children-classes
  [children]
  (->> children
       (map (fn [[name present]]
              [(->> name (clojure.core/name) (str "with-"))
               present]))
       (into {})))

(defn page
  []
  (let [children (r/atom nil)
        handle-ref (fn [ref]
                     (when (some? ref)
                       (reset! children (->> class-names
                                             (map (fn [[name class-name]]
                                                    [name (has-child? ref class-name)]))
                                             (into {})))))]
    (r/create-class
      {:display-name "Page Layout"
       :reagent-render
       (fn [{:keys [class-name]}]
         (->> (r/current-component)
              (r/children)
              (into [:div {:class-name (get-class-name (merge {"widget-profile" true
                                                               class-name       (some? class-name)}
                                                              (with-children-classes @children)))
                           :ref        handle-ref}])))})))

(defn- header-info
  [{:keys [icon title]}]
  [:div.info
   (when (some? icon)
     [c/icon {:icon       icon
              :class-name "icon"}])
   (when (some? title)
     [:div {:class-name "title"}
      title])])

(defn- header-action
  [{:keys [icon text] :as props}]
  (cond
    (some? icon) [c/icon-button props text]
    :default [c/button props text]))

(defn- header-actions
  [{:keys [actions]}]
  (when (some? actions)
    [:div.actions
     (for [[idx action] (map-indexed vector actions)]
       ^{:key idx}
       [header-action action])]))

(defn header
  [props]
  [:div {:class-name (:header class-names)}
   [header-info props]
   (into [:div.content]
         (->> (r/current-component)
              (r/children)))
   [header-actions props]])

(defn main-content
  [{:keys [footer title]}]
  [:div {:class-name (:main class-names)}
   (when (some? title)
     [:h1 title])
   (->> (r/current-component)
        (r/children)
        (into [:div.widget-profile--main-content--content]))
   [:div.widget-profile--main-content--footer
    footer]])

(defn side-bar
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (:side-bar class-names)}
              (when (some? title)
                [:h1 title])])))

(defn block
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--content-block
              [:h2 title]])))
