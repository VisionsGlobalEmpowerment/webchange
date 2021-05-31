(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.ui-framework.components.index :refer [menu select]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- target-control
  [{:keys [path type value]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-target path value])]
    (let [current-target @(re-frame/subscribe [::state/current-target path])
          available-targets @(re-frame/subscribe [::state/available-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-phrase-action-target path type target]))]
      [menu {:class-name "targets-menu"
             :el         (r/as-element [:span.target-value (if-not (empty? current-target) current-target "___")])
             :items      (->> available-targets
                              (map (fn [target]
                                     {:text     target
                                      :on-click #(handle-target-change target)})))}])))

(defn text-control
  []
  (let []
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :reagent-render
                     (fn [{:keys [path type value]}]
                       (let [handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (re-frame/dispatch [::state/set-phrase-action-text path type new-value])))]
                         [:span {:class-name                        "text"
                                 :on-input                          handle-change
                                 :content-editable                  true
                                 :suppress-content-editable-warning true}
                          value]))})))

(defn action-unit
  [{:keys [text character path type concept-name]}]
  (let [concept? (= type :concept)]
    [:div (cond-> {:class-name (get-class-name {"action-unit"  true
                                                "concept-unit" concept?})}
                  concept? (assoc :title (str "Concept «" concept-name "»")))
     [target-control {:value character
                      :path  path
                      :type  type}]
     [text-control {:value text
                    :path  path
                    :type  type}]]))
