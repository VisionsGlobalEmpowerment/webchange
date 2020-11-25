(ns webchange.fixtures.course-for-table)

(def data {:levels
           [{:name  "Level 1",
             :level 1,
             :scheme
                    {:lesson     {:name "Lesson", :lesson-sets ["concepts"]},
                     :assessment {:name "Assessment", :lesson-sets ["assessment-1"]}},
             :lessons
                    [{:name        "Lesson 1",
                      :type        "lesson",
                      :lesson      1,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls1"}}
                     {:name        "Lesson 2",
                      :type        "lesson",
                      :lesson      2,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls2"}}
                     {:name        "Lesson 3",
                      :type        "lesson",
                      :lesson      3,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls3"}}
                     {:name        "Assessment 1",
                      :type        "assessment",
                      :lesson      4,
                      :activities  [{:scored true, :activity "hide-n-seek", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment-1 "assessment1"}}
                     {:name        "Lesson 4",
                      :type        "lesson",
                      :lesson      5,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls4"}}
                     {:name        "Lesson 5",
                      :type        "lesson",
                      :lesson      6,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls5"}}
                     {:name        "Lesson 6",
                      :type        "lesson",
                      :lesson      7,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls6"}}
                     {:name        "Lesson 7",
                      :type        "lesson",
                      :lesson      8,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls7"}}
                     {:name        "Assessment 2",
                      :type        "assessment",
                      :lesson      9,
                      :activities  [{:scored true, :activity "hide-n-seek", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment-1 "assessment2"}}
                     {:name        "Lesson 8",
                      :type        "lesson",
                      :lesson      10,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls8"}}
                     {:name        "Lesson 9",
                      :type        "lesson",
                      :lesson      11,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls9"}}
                     {:name        "Lesson 10",
                      :type        "lesson",
                      :lesson      12,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls10"}}
                     {:name        "Lesson 11",
                      :type        "lesson",
                      :lesson      13,
                      :activities
                                   [{:activity "home", :time-expected 300}
                                    {:activity "see-saw", :time-expected 300, :only [:beginner]}
                                    {:activity "swings", :time-expected 300}
                                    {:activity "sandbox", :time-expected 300}
                                    {:scored        true, :activity "volleyball", :time-expected 300,
                                     :tags-by-score {:intermediate [0 75], :advanced [75 101]}}
                                    {:activity "book", :time-expected 300}
                                    {:scored        true, :activity "cycling", :time-expected 300, :only [:intermediate]
                                     :tags-by-score {:beginner [0 75], :intermediate [75 101]}}
                                    {:activity "painting-tablet", :time-expected 300}],
                      :lesson-sets {:concepts "ls11"}}
                     {:name        "Assessment 3",
                      :type        "assessment",
                      :lesson      14,
                      :activities  [{:scored true, :activity "hide-n-seek", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment-1 "assessment3"}}]}
            {:name  "Level 2",
             :level 2,
             :scheme
                    {:lesson     {:name "Lesson", :lesson-sets ["concepts-single" "concepts-group"]},
                     :assessment {:name "Assessment", :lesson-sets ["assessment"]}},
             :lessons
                    [{:name        "Lesson 12",
                      :type        "lesson",
                      :lesson      1,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls1-3", :concepts-single "l2-ls1-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 13",
                      :type        "lesson",
                      :lesson      2,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls2-3", :concepts-single "l2-ls2-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 14",
                      :type        "lesson",
                      :lesson      3,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls3-3", :concepts-single "l2-ls3-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 15",
                      :type        "lesson",
                      :lesson      4,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls4-3", :concepts-single "l2-ls4-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 16",
                      :type        "lesson",
                      :lesson      5,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls5-3", :concepts-single "l2-ls5-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 17",
                      :type        "lesson",
                      :lesson      6,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls6-3", :concepts-single "l2-ls6-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 18",
                      :type        "lesson",
                      :lesson      7,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls7-3", :concepts-single "l2-ls7-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 19",
                      :type        "lesson",
                      :lesson      8,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls8-3", :concepts-single "l2-ls8-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 20",
                      :type        "lesson",
                      :lesson      9,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls9-3", :concepts-single "l2-ls9-1", :concepts-all "concepts-all"}}
                     {:name        "Assessment 1",
                      :type        "assessment",
                      :lesson      10,
                      :activities  [{:scored true, :activity "pinata", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment "assessment4"}}
                     {:name        "Lesson 21",
                      :type        "lesson",
                      :lesson      11,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls10-3", :concepts-single "l2-ls10-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 22",
                      :type        "lesson",
                      :lesson      12,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls11-3", :concepts-single "l2-ls11-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 23",
                      :type        "lesson",
                      :lesson      13,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls12-3", :concepts-single "l2-ls12-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 24",
                      :type        "lesson",
                      :lesson      14,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls13-3", :concepts-single "l2-ls13-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 25",
                      :type        "lesson",
                      :lesson      15,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls14-3", :concepts-single "l2-ls14-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 26",
                      :type        "lesson",
                      :lesson      16,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls15-3", :concepts-single "l2-ls15-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 27",
                      :type        "lesson",
                      :lesson      17,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls16-3", :concepts-single "l2-ls16-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 28",
                      :type        "lesson",
                      :lesson      18,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls17-3", :concepts-single "l2-ls17-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 29",
                      :type        "lesson",
                      :lesson      19,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls18-3", :concepts-single "l2-ls18-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 30",
                      :type        "lesson",
                      :lesson      20,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls19-3", :concepts-single "l2-ls19-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 31",
                      :type        "lesson",
                      :lesson      21,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls20-3", :concepts-single "l2-ls20-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 32",
                      :type        "lesson",
                      :lesson      22,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls21-3", :concepts-single "l2-ls21-1", :concepts-all "concepts-all"}}
                     {:name        "Assessment 2",
                      :type        "assessment",
                      :lesson      23,
                      :activities  [{:scored true, :activity "pinata", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment "assessment5"}}
                     {:name        "Lesson 33",
                      :type        "lesson",
                      :lesson      24,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls22-3", :concepts-single "l2-ls22-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 34",
                      :type        "lesson",
                      :lesson      25,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls23-3", :concepts-single "l2-ls23-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 35",
                      :type        "lesson",
                      :lesson      26,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls24-3", :concepts-single "l2-ls24-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 36",
                      :type        "lesson",
                      :lesson      27,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls25-3", :concepts-single "l2-ls25-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 37",
                      :type        "lesson",
                      :lesson      28,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls26-3", :concepts-single "l2-ls26-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 38",
                      :type        "lesson",
                      :lesson      29,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls27-3", :concepts-single "l2-ls27-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 39",
                      :type        "lesson",
                      :lesson      30,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls28-3", :concepts-single "l2-ls28-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 40",
                      :type        "lesson",
                      :lesson      31,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls29-3", :concepts-single "l2-ls29-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 41",
                      :type        "lesson",
                      :lesson      32,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls30-3", :concepts-single "l2-ls30-1", :concepts-all "concepts-all"}}
                     {:name        "Lesson 42",
                      :type        "lesson",
                      :lesson      33,
                      :activities
                                   [{:activity "cinema", :time-expected 300}
                                    {:activity "cinema-video", :time-expected 300}
                                    {:activity "letter-intro", :time-expected 300}
                                    {:activity "park-poem", :time-expected 300}
                                    {:scored true, :activity "running", :time-expected 300}
                                    {:scored true, :activity "slide", :time-expected 300}
                                    {:activity "writing-lesson", :time-expected 300}
                                    {:activity "writing-practice", :time-expected 300}
                                    {:scored true, :activity "magic-hat", :time-expected 300}
                                    {:scored true, :activity "cycling-letters", :time-expected 300}
                                    {:scored true, :activity "volleyball-letters", :time-expected 300}],
                      :lesson-sets {:concepts-group "l2-ls31-3", :concepts-single "l2-ls31-1", :concepts-all "concepts-all"}}
                     {:name        "Assessment 3",
                      :type        "assessment",
                      :lesson      34,
                      :activities  [{:scored true, :activity "pinata", :time-expected 300, :expected-score-percentage 90}],
                      :lesson-sets {:assessment "assessment6"}}]}]})
