(ns webchange.migrations.core
  (:require [webchange.migrations.add-unique-ids]
            [webchange.migrations.change-first-word-book-template]
            [webchange.migrations.activity-stats-fill-unique-id]
            [webchange.migrations.concepts]
            [webchange.migrations.scene-id-in-course-data]
            [webchange.migrations.scene-data]
            [webchange.migrations.scene-template-in-metadata]))
