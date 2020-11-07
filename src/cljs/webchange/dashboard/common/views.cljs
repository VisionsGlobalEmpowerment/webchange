(ns webchange.dashboard.common.views
  (:require
    [webchange.dashboard.common.views-content-wrappers :as content-wrappers-views]
    [webchange.dashboard.common.score-table.views :as score-table-views]))

(def content-page content-wrappers-views/content-page)
(def content-page-section content-wrappers-views/content-page-section)
(def score-table score-table-views/score-table)
