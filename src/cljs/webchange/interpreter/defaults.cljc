(ns webchange.interpreter.defaults)

(def default-game-assets [
                          ;; Common elements
                          {:url "/raw/img/bg.jpg", :size 1, :type "image"}
                          {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}

                          ;; Navigation menu
                          {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}

                          ;; Settings
                          {:url "/raw/img/ui/settings/settings.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings/music.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings/music_icon.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings/sound_fx.png", :size 1, :type "image"}
                          {:url "/raw/img/ui/settings/sound_fx_icon.png", :size 1, :type "image"}

                          ;; Activity finished
                          {:url "/raw/img/ui/vera_315x371.png", :size 1, :type "image"}
                          ])

(def default-assets (concat default-game-assets
                            [{:url "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                             {:url "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                             {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                             {:url "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                             {:url "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
                             {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                             {:url "/raw/audio/effects/wrong.wav", :size 1, :type "audio"}
                             {:url "/raw/audio/effects/correct.mp3", :size 1, :type "audio"}

                             {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}

                             {:url "/raw/img/ui/hand.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/logo.png", :size 1, :type "image"}
                             {:url "/raw/img/ui/next_button_01.png", :size 1, :type "image"}

                             {:url "/raw/anim/senoravaca/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/senoravaca/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/senoravaca/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/senoravaca/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/senoravaca/skeleton3.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/senoravaca/skeleton4.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/senoravaca/skeleton5.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/senoravaca/skeleton6.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/vera/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/vera/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/vera/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera/skeleton3.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera/skeleton4.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera/skeleton5.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/vera-45/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/vera-45/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/vera-45/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton3.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton4.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton5.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton6.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton7.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-45/skeleton8.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/vera-90/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/vera-90/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/vera-90/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-90/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-90/skeleton3.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/vera-90/skeleton4.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/rock/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/rock/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/rock/skeleton.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/mari/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/mari/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/mari/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/mari/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/mari/skeleton3.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/boxes/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/boxes/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/boxes/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/boxes/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/boxes/skeleton3.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/book/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/book/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/book/skeleton.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/book/skeleton2.png", :size 1, :type "anim-texture"}
                             {:url "/raw/anim/book/skeleton3.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/hat/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/hat/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/hat/skeleton.png", :size 1, :type "anim-texture"}

                             {:url "/raw/anim/pinata/skeleton.atlas", :size 1, :type "anim-text"}
                             {:url "/raw/anim/pinata/skeleton.json", :size 1, :type "animation"}
                             {:url "/raw/anim/pinata/skeleton.png", :size 1, :type "anim-texture"}

                             {:url "/raw/img/elements/8.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/airplane.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ant.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/apple.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/arrow.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/baby.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/baby_cow.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ball.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/bat.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/bear.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/bed.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/bee.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/bicycle.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/black.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/boat.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/boot.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/boots.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/boy.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/broccoli.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/building.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/call.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/candle.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/car.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/carrot.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/cat.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/chair.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/chalk.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/cheese.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/chicken.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/chocolate.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/cloud.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/comb.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/cow.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/crocodile.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/crown.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/cry.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/deer.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/diamond.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/dinosaur.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/dragon.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/duck.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ear.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/earth.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/egg.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/egg_yolk.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/elephant.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/embrace.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/eyebrow.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/eyes.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/factory.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fifteen.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/finger.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fingernail.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fire.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fish.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/five.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fifth.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/flamingo.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/flower.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/flute.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fox.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/frog.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/fruits.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/garden.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/gemstone.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/giant.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/giraffe.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/girl.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/glove.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/gnocchi.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/grapes.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/grass.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/green.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/guitar.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/hand.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/hair_brush.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/hippopotamus.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/home.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ice cream.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/iguana.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/insect.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/island.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/jacket.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/jamon.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/jaw.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/juice.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/kangaroo.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/kayak.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ketchup.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/key.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/kimono.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/kite.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/kiwi.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/knot.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/koala.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ladybug.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/lama.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/leaf.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/lemon.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/lion.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/loupe.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/lupa.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/magnet.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/mama.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/mexico.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/milk.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/monkey.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/moon.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/mountain.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/mouse.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/mouth.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/nail.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/nest.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/nino.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/nora.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/nurse.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/one.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/onion.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/orange.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/ostrich.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/pencil.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/phone.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/pig.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/potatoes.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/pumpkin.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/queen.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/quesadilla.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/rain.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/rock.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/rose.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/salad.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/sausage.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/saxophone.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/shark.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/sheep.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/skunk.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/smile.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/snake.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/soup.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/spider.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/squirrel.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/star.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/strawberry.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/summer.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/sun.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/sunflower.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/taxi.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/teeth.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/text.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/tomato.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/toothbrush.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/tornado.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/toys.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/train.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/tree.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/turtle.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/twins.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/unicorn.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/uniform.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/violin.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/waffles.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/watermelon.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/web site.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/website.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/wendy.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/whale.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/willi.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/window.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/wrench.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/xylophone.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yacht.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yak.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yam.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yo_yo.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yogurt.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/yoyo.png", :size 1, :type "image"}
                             {:url "/raw/img/elements/zebra.png", :size 1, :type "image"}]))
