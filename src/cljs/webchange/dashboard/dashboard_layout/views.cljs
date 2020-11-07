(ns webchange.dashboard.dashboard-layout.views
  (:require
    [webchange.dashboard.dashboard-layout.views-app-bar :as views-app-bar]
    [webchange.dashboard.dashboard-layout.views-drawer :as views-drawer]
    [webchange.dashboard.dashboard-layout.views-progress-bar :as views-progress-bar]
    [webchange.dashboard.dashboard-layout.views-side-menu :as views-side-menu]))

(def app-bar views-app-bar/app-bar)
(def drawer views-drawer/drawer)
(def progress-bar views-progress-bar/progress-bar)
(def side-menu views-side-menu/side-menu)
