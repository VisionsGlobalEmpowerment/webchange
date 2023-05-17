(ns webchange.i18n.translate
  (:require
    [re-frame.core :as re-frame]
    [taoensso.tempura :refer [tr]]
    [webchange.state.core :as state]))

(def translations
  {"english" {:book-library        "Book library"
              :categories          {:animals            "Animals"
                                    :family-and-friends "Family & Friends" ;; <- non-breaking space before "Friends"
                                    :science-stem       "Science"
                                    :sports             "Sports"
                                    :vehicles           "Vehicles"}
              :continue            "Continue"
              :error-general       "Something went wrong.. :("
              :exit                "Exit"
              :favorite            "Favorite"
              :favorite-books      "My Favorite books"
              :goodbye             "Goodbye!"
              :great-work          "Great work!"
              :home-page           "Home"
              :loading             "Loading"
              :next                "Next"
              :no-books            "There are no books yet..."
              :play                "Play"
              :read                {:read      "Read"
                                    :to-me     "Read to me"
                                    :by-myself "Read by myself"}
              :search              "Search"
              :search-book-library "Search Book Library"
              :skip                "Skip"
              :wait                "Please, wait..."
              :watch               "Watch"}
   "spanish" {:book-library        "Biblioteca"
              :categories          {:animals            "Animales"
                                    :family-and-friends "Familia y Amigos" ;; <- non-breaking space before "Amigos"
                                    :science-stem       "Ciencia"
                                    :sports             "Deportes"
                                    :vehicles           "Vehículos"}
              :continue            "Seguir"
              :error-general       "Algo salió mal.. :("
              :exit                "Salida"
              :favorite            "Favoritos"
              :favorite-books      "Mis libros favoritos"
              :goodbye             "Adiós!"
              :great-work          "¡Bien hecho!"
              :home-page           "Inicio"
              :loading             "Cargando"
              :next                "Sigue"
              :no-books            "Aún no hay libros..."
              :play                "Juega"
              :read                {:read      "Leer"
                                    :to-me     "¡Leeme!"
                                    :by-myself "Yo leo"}
              :search              "Buscar"
              :search-book-library "Buscar"
              :skip                "Omitir"
              :wait                "Espere por favor..."
              :watch               "Mira"}
   "sepedi"  {:book-library        "Bokgobapuku"
              :categories          {:animals            "Diphoofolo"
                                    :family-and-friends "Meloko le Metswalle" ;; <- non-breaking spaces
                                    :science-stem       "saentshe"
                                    :sports             "Dipapadi"
                                    :vehicles           "Difatanaga"}
              :continue            "tšwela pele"
              :error-general       "Something went wrong... :("
              :exit                "tšwa"
              :favorite            "Tšeo di ratwago"
              :favorite-books      "Dipuku tšeo ke di Ratago kudu"
              :goodbye             "šala gabotse"
              :great-work          "mošomo o mogolo"
              :home-page           "Gae"
              :loading             "Loading"
              :next                "Ye e latelago"
              :no-books            "Ga go na dipuku go fihla ga bjale"
              :play                "Raloka"
              :read                {:read      "Bala"
                                    :to-me     "Mpalele"
                                    :by-myself "Bala ke nnoši"}
              :search              "Nyaka"
              :search-book-library "Nyaka ka gare ga Bokgobapuku"
              :skip                "Tshela"
              :wait                "Ka kgopelo, ema..."
              :watch               "Bogela"}
   "tamil"   {:book-library        "நூலகம்"
              :categories          {:animals            "விலங்குகள்"
                                    :family-and-friends "குடும்பம் மற்றும் நண்பர்கள்" ;; <- non-breaking spaces
                                    :science-stem       "அறிவியல்"
                                    :sports             "விளையாட்டு"
                                    :vehicles           "வாகனங்கள்"}
              :continue            "தொடரவும்"
              :error-general       "ஏதோ சிக்கல் ஏற்பட்டுள்ளது"
              :exit                "வெளியேறு"
              :favorite            "பிடித்த"
              :favorite-books      "எனக்குப் பிடித்த புத்தகங்கள்"
              :goodbye             "பார்க்கலாம். சென்று  வருகிறேன்"
              :great-work          "மிகச் சிறப்பு!"
              :home-page           "முகப்புப் பக்கம்"
              :loading             "பதிவிறக்கம் செய்யப்படுகிறது"
              :next                "அடுத்தது"
              :no-books            "இங்கு புத்தகங்கள் இன்னும் வைக்கப்படவில்லை"
              :play                "விளையாடு"
              :read                {:read      "வாசி"
                                    :to-me     "எனக்கு வாசித்துக் காட்டு"
                                    :by-myself "நானே வாசிக்கிறேன்"}
              :search              "தேடு"
              :search-book-library "புத்தக நூலகத்தைத் தேடுங்கள்"
              :skip                "அடுத்த பக்கத்திற்குச் செல்"
              :wait                "தயவுசெய்து, காத்திருங்கள்…"
              :watch               "பார்க்கவும்"}})

(defn- translate
  [language key]
  (tr {:dict translations} [language "english"] key))

(re-frame/reg-sub
  ::t
  (fn []
    (re-frame/subscribe [::state/current-course-lang]))
  (fn [language [_ key]]
    (if (map? key)
      (->> key
           (map (fn [[field key]]
                  [field (translate language key)]))
           (into {}))
      (translate language key))))

