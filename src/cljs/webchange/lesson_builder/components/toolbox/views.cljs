(ns webchange.lesson-builder.components.toolbox.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn- toolbox-header
  [{:keys [actions text title icon]}]
  [:div.toolbox--header
   (when (some? icon)
     [ui/icon {:icon       icon
               :class-name "toolbox--header--icon"}])
   [:div.toolbox--header--name title]
   (when (some? text)
     [:div.toolbox--header--text text])
   [:div.toolbox--header--filler]
   (when (some? actions)
     actions)])

(defn toolbox
  [{:keys [class-name] :as props}]
  [:div {:class-name (ui/get-class-name {"component--toolbox" true
                                         class-name           (some? class-name)})}
   [toolbox-header props]
   (->> (r/current-component)
        (r/children)
        (into [:div.toolbox--content]))])
