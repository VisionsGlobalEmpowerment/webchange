(ns webchange.lesson-builder.blocks.toolbox.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.blocks.toolbox.state :as state]
    [webchange.lesson-builder.tools.background-image.views :refer [background-image background-type-switcher]]
    [webchange.lesson-builder.widgets.welcome.views :refer [welcome]]
    [webchange.ui.index :as ui]))

(defn- toolbox-header
  [{:keys [actions title icon]}]
  [:div.toolbox--header
   [ui/icon {:icon       icon
             :class-name "toolbox--header--icon"}]
   [:div.toolbox--header--name title]
   (when (some? actions)
     [actions])])

(defn toolbox
  [{:keys [class-name] :as props}]
  [:div {:id         "block--toolbox"
         :class-name class-name}
   [toolbox-header props]
   (->> (r/current-component)
        (r/children)
        (into [:div.toolbox--content]))])

(def toolboxes {:welcome          {:title     "Welcome To The Lesson Builder!"
                                   :icon      "create"
                                   :component welcome}
                :background-image {:title     "Change Background"
                                   :icon      "create"
                                   :component background-image
                                   :actions   background-type-switcher}})

(defn block-toolbox
  [{:keys [class-name]}]
  (let [current-widget @(re-frame/subscribe [::state/current-widget])
        {:keys [actions title icon component]} (get toolboxes current-widget)]
    [toolbox {:title      title
              :icon       icon
              :actions    actions
              :class-name class-name}
     [component]]))
