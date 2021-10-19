(ns webchange.editor-v2.activity-form.generic.components.anchor.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]))

(defn add-anchor
  []
  (re-frame/dispatch [::state-activity/call-activity-common-action {:action :add-anchor}]))
