(ns webchange.utils.book-library
  (:require
    [webchange.utils.languages :as l]))

(def ages [{:name "0-2" :value "0-2"}
           {:name "3" :value "3"}
           {:name "4" :value "4"}
           {:name "5" :value "5"}
           {:name "6" :value "6"}
           {:name "7" :value "7"}
           {:name "8" :value "8"}
           {:name "9" :value "9"}
           {:name "10" :value "10"}
           {:name "11" :value "11"}
           {:name "12+" :value "12+"}])

(def categories (->> [{:name "Animals" :value "animals"}
                      {:name "Family and Friends" :value "family-and-friends"}
                      {:name "Science/STEM" :value "science-stem"}
                      {:name "Sports" :value "sports"}
                      {:name "Vehicles" :value "vehicles"}]
                     (map #(assoc % :metadata {:book-library? true}))
                     (concat
                      [{:name "All About Me" :value "all-about-me"}
                       {:name "Arts and Music" :value "arts-and-music"}
                       {:name "Autobiography and Biography" :value "autobiography-and-biography"}
                       {:name "Countries and Cultures" :value "countries-and-cultures"}
                       {:name "Early Learning Concepts" :value "early-learning-concepts"}
                       {:name "Everyday Life" :value "everyday-life"}
                       {:name "History" :value "history"}
                       {:name "Life Skills" :value "life-skills"}
                       {:name "Math" :value "math"}
                       {:name "Mystery and Adventure" :value "mystery-and-adventure"}
                       {:name "Series" :value "series"}
                       {:name "Social Emotional Learning (SEL)" :value "social-emotional-learning"}
                       {:name "Traditional Tales" :value "traditional-tales"}])))

(def genres [{:name "Fiction" :value "fiction"}
             {:name "Non-Fiction" :value "non-fiction"}
             {:name "Poetry" :value "poetry"}
             {:name "Reference" :value "reference"}
             {:name "Early Reader" :value "early-reader"}])

(def languages l/languages)

(def reading-levels [{:name "Early Reader" :value "early-reader"}
                     {:name "A" :value "a"}
                     {:name "B" :value "b"}
                     {:name "C" :value "c"}
                     {:name "D" :value "d"}
                     {:name "E" :value "e"}
                     {:name "F" :value "f"}
                     {:name "G" :value "g"}
                     {:name "H" :value "h"}
                     {:name "I" :value "i"}
                     {:name "J" :value "j"}])

(def tags [{:name "alphabet" :value "alphabet"}
           {:name "numbers" :value "numbers"}
           {:name "shapes" :value "shapes"}
           {:name "colors" :value "colors"}
           {:name "calendar" :value "calendar"}
           {:name "space" :value "space"}
           {:name "seasons" :value "seasons"}
           {:name "human body" :value "human-body"}
           {:name "health" :value "health"}
           {:name "plants" :value "plants"}
           {:name "weather" :value "weather"}
           {:name "earth and sky" :value "earth-and-sky"}
           {:name "environment" :value "environment"}
           {:name "water" :value "water"}
           {:name "technology" :value "technology"}
           {:name "food" :value "food"}
           {:name "engineering" :value "engineering"}
           {:name "high frequency words" :value "high-frequency-words"}
           {:name "persuasive/opinion" :value "persuasive-opinion"}
           {:name "personal narrative" :value "personal-narrative"}
           {:name "informational/how to" :value "informational"}
           {:name "dialogue" :value "dialogue"}
           {:name "rhyming" :value "rhyming"}
           {:name "repetitive sentence structure" :value "repetitive-sentence-structure"}])

(def age-options (->> ages
                      (map (fn [{:keys [name value]}]
                             {:text  name
                              :value value}))))

(def genre-options (->> genres
                        (map (fn [{:keys [name value]}]
                               {:text  name
                                :value value}))))

(def reading-level-options (->> reading-levels
                                (map (fn [{:keys [name value]}]
                                       {:text  name
                                        :value value}))))

(def category-options (->> categories
                           (map (fn [{:keys [name value]}]
                                  {:text  name
                                   :value value}))))

(def tag-options (->> tags
                      (map (fn [{:keys [name value]}]
                             {:text  name
                              :value value}))))
