(ns webchange.ui.components.icon.navigation.index
  (:require
    [webchange.ui.components.icon.navigation.icon-accounts :as accounts]
    [webchange.ui.components.icon.navigation.icon-achievements :as achievements]
    [webchange.ui.components.icon.navigation.icon-book :as book]
    [webchange.ui.components.icon.navigation.icon-classes :as classes]
    [webchange.ui.components.icon.navigation.icon-courses :as courses]
    [webchange.ui.components.icon.navigation.icon-create :as create]
    [webchange.ui.components.icon.navigation.icon-dashboard :as dashboard]
    [webchange.ui.components.icon.navigation.icon-games :as games]
    [webchange.ui.components.icon.navigation.icon-lesson :as lesson]
    [webchange.ui.components.icon.navigation.icon-levels :as levels]
    [webchange.ui.components.icon.navigation.icon-library :as library]
    [webchange.ui.components.icon.navigation.icon-play :as play]
    [webchange.ui.components.icon.navigation.icon-scene :as scene]
    [webchange.ui.components.icon.navigation.icon-school :as school]
    [webchange.ui.components.icon.navigation.icon-students :as students]
    [webchange.ui.components.icon.navigation.icon-teachers :as teachers]
    [webchange.ui.components.icon.utils :refer [with-prefix]]))

(def data (with-prefix "navigation"
                       {"accounts"     accounts/data
                        "achievements" achievements/data
                        "book"         book/data
                        "classes"      classes/data
                        "courses"      courses/data
                        "create"       create/data
                        "dashboard"    dashboard/data
                        "games"        games/data
                        "lesson"       lesson/data
                        "levels"       levels/data
                        "library"      library/data
                        "play"         play/data
                        "scene"        scene/data
                        "school"       school/data
                        "students"     students/data
                        "teachers"     teachers/data}))
