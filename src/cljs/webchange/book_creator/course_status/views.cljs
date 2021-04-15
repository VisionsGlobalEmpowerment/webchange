(ns webchange.book-creator.course-status.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.course-status.state :as state]
    [webchange.ui-framework.components.index :refer [button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- publish-button
  []
  (let [handle-click #(re-frame/dispatch [::state/publish])]
    [button {:on-click handle-click}
     "Publish"]))

(defn- status-message
  [{:keys [message class-name]}]
  [:div {:class-name (get-class-name (cond-> {"status-message" true}
                                             (some? class-name) (assoc class-name true)))}
   message])

(defn- syncing
  []
  [status-message {:message    "sync.."
                   :class-name "sync"}])

(defn- in-review
  []
  [status-message {:message    "In Review"
                   :class-name "in-review"}])

(defn- declined
  []
  [status-message {:message    "Declined"
                   :class-name "declined"}])

(defn- published
  []
  [status-message {:message    "Published"
                   :class-name "published"}])

(defn review-status
  []
  (let [status @(re-frame/subscribe [::state/status])]
    (cond
      (= status "sync") [syncing]
      (= status "in-review") [in-review]
      (= status "declined") [declined]
      (= status "published") [published]
      (and (not= status "published")
           (not= status "declined")) [publish-button]
      :else nil)))
