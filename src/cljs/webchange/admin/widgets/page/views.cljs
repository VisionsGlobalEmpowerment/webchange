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

(defn header
  [{:keys [actions] :as props}]
  (let [children (->> (r/current-component)
                      (r/children))]
    [:div {:class-name (:header class-names)}
     [header-info props]
     (when (some? children)
       (into [:div.content]
             children))
     (when (some? actions)
       [:div.actions
        actions])]))

(defn- block-title
  [{:keys [actions title]}]
  (when (or (some? title)
            (some? actions))
    [:h1.block-title
     (when (some? title)
       [:div.title-text title])
     (when (some? actions)
       [:div.title-actions actions])]))

(defn main-content
  [{:keys [class-name footer title]}]
  [:div {:class-name (:main class-names)}
   [block-title {:title title}]
   (->> (r/current-component)
        (r/children)
        (into [:div {:class-name (get-class-name {"widget-profile--main-content--content" true
                                                  class-name                              (some? class-name)})}]))
   (when (some? footer)
     [:div.widget-profile--main-content--footer
      footer])])

(defn side-bar
  [{:keys [actions title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (:side-bar class-names)}
              [block-title {:title   title
                            :actions actions}]])))

(defn block
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--content-block
              [:h2 title]])))
