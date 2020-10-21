(ns webchange.editor-v2.wizard.steps.choose-template
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]))

(defn- get-styles
  []
  {:form              {:padding-bottom "16px"}
   :text-input        {:margin-top "16px"}
   :control-container {:margin-top "8px"}
   :tag               {:margin "4px 8px"}
   :tags-container    {:margin-left "-16px"}
   :template-item     {:text-transform  "capitalize"
                       :display         "flex"
                       :justify-content "space-between"}})

(def validation-map {:template-id [(fn [value] (when (nil? value) "Template is required"))]})

(defn form
  [{:keys [parent-data validator]}]
  (r/with-let [_ (re-frame/dispatch [::state-activity/load-templates])
               data (connect-data parent-data :template)
               current-tag (r/atom nil)
               handle-change-template (fn [template-id]
                                        (swap! data assoc :template-id template-id))
               handle-tag-click (fn [tag]
                                  (reset! current-tag tag))
               {:keys [error-message]} (validator/init data validation-map validator)
               styles (get-styles)]
    (let [templates @(re-frame/subscribe [::state-activity/templates])
          filtered-templates (filter (fn [{:keys [tags]}]
                                       (if (some? @current-tag)
                                         (some #{@current-tag} tags)
                                         true))
                                     templates)
          tags (->> templates
                    (map :tags)
                    (apply concat)
                    (distinct))]
      [ui/grid {:container   true
                :justify     "center"
                :spacing     24
                :align-items "center"
                :style       (:form styles)}
       [ui/grid {:item  true :xs 12
                 :style (:tags-container styles)}
        [ui/button {:on-click #(handle-tag-click nil)
                    :color    (if (= nil @current-tag)
                                "secondary"
                                "default")
                    :style    (:tag styles)}
         "All"]
        (doall (for [tag tags]
                 ^{:key tag}
                 [ui/button {:on-click #(handle-tag-click tag)
                             :color    (if (= tag @current-tag)
                                         "secondary"
                                         "default")
                             :style    (:tag styles)}
                  tag]))]
       [ui/grid {:item true :xs 12}
        [ui/form-control {:full-width true
                          :style      (:control-container styles)}
         [ui/input-label "Template"]
         [ui/select {:value        (or (:template-id @data) "")
                     :on-change    #(handle-change-template (-> % .-target .-value))
                     :render-value (fn [value]
                                     (->> (fn []
                                            (let [{:keys [name]} (some (fn [{:keys [id] :as template}]
                                                                         (and (= value id)
                                                                              template))
                                                                       templates)]
                                              [:div {:style (:template-item styles)} name]))
                                          (r/reactify-component)
                                          (r/create-element)))
                     :style        (:control styles)}
          (doall (for [template filtered-templates]
                   ^{:key (:id template)}
                   [ui/menu-item {:value (:id template)
                                  :style (:template-item styles)}
                    (:name template)
                    [:div
                     (for [tag (:tags template)]
                       ^{:key tag}
                       [ui/chip {:label   tag
                                 :variant "outlined"}])]]))]
         [error-message {:field-name :template-id}]]]])))

(defn get-step
  [{:keys [data validator]}]
  {:label   "Template"
   :header  "Choose your template"
   :content [form {:parent-data data
                   :validator   validator}]})
