(ns webchange.admin.pages.dashboard.organisations-chart)

(def data
  [:svg {:width "163" :height "159" :viewBox "0 0 163 159" :fill "none" :xmlns "http://www.w3.org/2000/svg"}
   [:path {:fill-rule "evenodd" :clip-rule "evenodd" :d "M87.4365 128.757C85.8128 128.918 84.166 129 82.5 129C55.1619 129 33 106.838 33 79.5C33 52.1619 55.1619 30 82.5 30C85.3524 30 88.1484 30.2413 90.8689 30.7045L96.1599 1.16907C91.7218 0.400545 87.1577 0 82.5 0C38.5934 0 3 35.5934 3 79.5C3 123.407 38.5934 159 82.5 159C85.2666 159 88.0001 158.859 90.6939 158.583L87.4365 128.757Z" :fill "#80BFE5"}]
   [:path {:fill-rule "evenodd" :clip-rule "evenodd" :d "M90.8682 30.7034C114.221 34.6799 132 55.0132 132 79.4989C132 106.837 109.838 128.999 82.4995 128.999C75.6794 128.999 69.1815 127.62 63.2694 125.125L51.7773 152.845C61.229 156.809 71.6085 158.999 82.4995 158.999C126.406 158.999 162 123.406 162 79.4989C162 40.2499 133.557 7.64402 96.1593 1.16797L90.8682 30.7034Z" :fill "#3453A1"}]
   [:path {:d "M135.622 84.9678C135.622 84.5576 135.651 84.1523 135.71 83.752C135.772 83.3483 135.878 82.9691 136.027 82.6143C136.177 82.2562 136.387 81.9404 136.657 81.667C136.927 81.3903 137.271 81.1738 137.688 81.0176C138.104 80.8581 138.609 80.7783 139.201 80.7783C139.341 80.7783 139.504 80.7848 139.689 80.7979C139.878 80.8076 140.035 80.8255 140.158 80.8516V82.0576C140.028 82.0283 139.888 82.0055 139.738 81.9893C139.592 81.9697 139.445 81.96 139.299 81.96C138.706 81.96 138.247 82.0544 137.922 82.2432C137.6 82.432 137.372 82.694 137.238 83.0293C137.108 83.3613 137.032 83.7487 137.009 84.1914H137.072C137.163 84.0319 137.277 83.8903 137.414 83.7666C137.554 83.6429 137.723 83.5452 137.922 83.4736C138.12 83.3988 138.353 83.3613 138.62 83.3613C139.037 83.3613 139.398 83.4508 139.704 83.6299C140.01 83.8057 140.246 84.0612 140.412 84.3965C140.578 84.7318 140.661 85.1403 140.661 85.6221C140.661 86.1364 140.562 86.5791 140.363 86.9502C140.165 87.3213 139.883 87.6061 139.519 87.8047C139.157 88 138.728 88.0977 138.229 88.0977C137.865 88.0977 137.525 88.0358 137.209 87.9121C136.893 87.7852 136.617 87.5931 136.379 87.3359C136.141 87.0788 135.956 86.7549 135.822 86.3643C135.689 85.9704 135.622 85.5049 135.622 84.9678ZM138.2 86.8916C138.496 86.8916 138.737 86.7907 138.923 86.5889C139.108 86.387 139.201 86.0713 139.201 85.6416C139.201 85.2933 139.12 85.0199 138.957 84.8213C138.798 84.6195 138.555 84.5186 138.229 84.5186C138.008 84.5186 137.813 84.569 137.644 84.6699C137.478 84.7676 137.349 84.8929 137.258 85.0459C137.167 85.1956 137.121 85.3503 137.121 85.5098C137.121 85.6758 137.144 85.8402 137.189 86.0029C137.235 86.1657 137.303 86.3138 137.395 86.4473C137.486 86.5807 137.598 86.6882 137.731 86.7695C137.868 86.8509 138.024 86.8916 138.2 86.8916ZM146.345 84.4307C146.345 85.0068 146.299 85.5212 146.208 85.9736C146.12 86.4261 145.977 86.8102 145.778 87.126C145.583 87.4417 145.326 87.6826 145.007 87.8486C144.688 88.0146 144.3 88.0977 143.845 88.0977C143.272 88.0977 142.801 87.9528 142.434 87.6631C142.066 87.3701 141.794 86.9502 141.618 86.4033C141.442 85.8532 141.354 85.1956 141.354 84.4307C141.354 83.6592 141.434 83 141.594 82.4531C141.757 81.903 142.02 81.4814 142.385 81.1885C142.749 80.8955 143.236 80.749 143.845 80.749C144.414 80.749 144.883 80.8955 145.251 81.1885C145.622 81.4782 145.897 81.8981 146.076 82.4482C146.255 82.9951 146.345 83.6559 146.345 84.4307ZM142.854 84.4307C142.854 84.9743 142.883 85.4284 142.941 85.793C143.003 86.1543 143.106 86.4261 143.249 86.6084C143.392 86.7907 143.591 86.8818 143.845 86.8818C144.095 86.8818 144.292 86.7923 144.436 86.6133C144.582 86.431 144.686 86.1592 144.748 85.7979C144.81 85.4333 144.841 84.9775 144.841 84.4307C144.841 83.887 144.81 83.4329 144.748 83.0684C144.686 82.7038 144.582 82.4303 144.436 82.248C144.292 82.0625 144.095 81.9697 143.845 81.9697C143.591 81.9697 143.392 82.0625 143.249 82.248C143.106 82.4303 143.003 82.7038 142.941 83.0684C142.883 83.4329 142.854 83.887 142.854 84.4307ZM148.708 80.7588C149.268 80.7588 149.693 80.9541 149.982 81.3447C150.272 81.7354 150.417 82.2839 150.417 82.9902C150.417 83.6966 150.28 84.2484 150.007 84.6455C149.733 85.0426 149.3 85.2412 148.708 85.2412C148.158 85.2412 147.74 85.0426 147.453 84.6455C147.167 84.2484 147.023 83.6966 147.023 82.9902C147.023 82.2839 147.157 81.7354 147.424 81.3447C147.694 80.9541 148.122 80.7588 148.708 80.7588ZM148.718 81.7793C148.558 81.7793 148.441 81.8802 148.366 82.082C148.291 82.2839 148.254 82.5898 148.254 83C148.254 83.4102 148.291 83.7178 148.366 83.9229C148.441 84.1279 148.558 84.2305 148.718 84.2305C148.877 84.2305 148.994 84.1296 149.069 83.9277C149.147 83.7227 149.187 83.4134 149.187 83C149.187 82.5898 149.147 82.2839 149.069 82.082C148.994 81.8802 148.877 81.7793 148.718 81.7793ZM153.776 80.8613L149.816 88H148.645L152.604 80.8613H153.776ZM153.708 83.6104C154.268 83.6104 154.693 83.8057 154.982 84.1963C155.272 84.5869 155.417 85.1354 155.417 85.8418C155.417 86.5449 155.28 87.0951 155.007 87.4922C154.733 87.8893 154.3 88.0879 153.708 88.0879C153.158 88.0879 152.74 87.8893 152.453 87.4922C152.167 87.0951 152.023 86.5449 152.023 85.8418C152.023 85.1354 152.157 84.5869 152.424 84.1963C152.694 83.8057 153.122 83.6104 153.708 83.6104ZM153.718 84.6309C153.558 84.6309 153.441 84.7318 153.366 84.9336C153.291 85.1354 153.254 85.4414 153.254 85.8516C153.254 86.2617 153.291 86.5693 153.366 86.7744C153.441 86.9795 153.558 87.082 153.718 87.082C153.877 87.082 153.994 86.9811 154.069 86.7793C154.147 86.5742 154.187 86.265 154.187 85.8516C154.187 85.4414 154.147 85.1354 154.069 84.9336C153.994 84.7318 153.877 84.6309 153.718 84.6309Z" :fill "white"}]
   [:path {:d "M12.8223 78.5205H11.9629V80H10.4883V78.5205H7.44141V77.4707L10.5713 72.8613H11.9629V77.3486H12.8223V78.5205ZM10.4883 77.3486V76.1377C10.4883 76.0238 10.4899 75.8887 10.4932 75.7324C10.4997 75.5762 10.5062 75.4199 10.5127 75.2637C10.5192 75.1074 10.5257 74.9691 10.5322 74.8486C10.542 74.7249 10.5485 74.6387 10.5518 74.5898H10.5127C10.4508 74.7233 10.3857 74.8535 10.3174 74.9805C10.249 75.1042 10.1693 75.2344 10.0781 75.3711L8.76953 77.3486H10.4883ZM18.3447 76.4307C18.3447 77.0068 18.2992 77.5212 18.208 77.9736C18.1201 78.4261 17.9769 78.8102 17.7783 79.126C17.583 79.4417 17.3258 79.6826 17.0068 79.8486C16.6878 80.0146 16.3005 80.0977 15.8447 80.0977C15.2718 80.0977 14.8014 79.9528 14.4336 79.6631C14.0658 79.3701 13.7939 78.9502 13.6182 78.4033C13.4424 77.8532 13.3545 77.1956 13.3545 76.4307C13.3545 75.6592 13.4342 75 13.5938 74.4531C13.7565 73.903 14.0202 73.4814 14.3848 73.1885C14.7493 72.8955 15.236 72.749 15.8447 72.749C16.4144 72.749 16.8831 72.8955 17.251 73.1885C17.6221 73.4782 17.8971 73.8981 18.0762 74.4482C18.2552 74.9951 18.3447 75.6559 18.3447 76.4307ZM14.8535 76.4307C14.8535 76.9743 14.8828 77.4284 14.9414 77.793C15.0033 78.1543 15.1058 78.4261 15.249 78.6084C15.3923 78.7907 15.5908 78.8818 15.8447 78.8818C16.0954 78.8818 16.2923 78.7923 16.4355 78.6133C16.582 78.431 16.6862 78.1592 16.748 77.7979C16.8099 77.4333 16.8408 76.9775 16.8408 76.4307C16.8408 75.887 16.8099 75.4329 16.748 75.0684C16.6862 74.7038 16.582 74.4303 16.4355 74.248C16.2923 74.0625 16.0954 73.9697 15.8447 73.9697C15.5908 73.9697 15.3923 74.0625 15.249 74.248C15.1058 74.4303 15.0033 74.7038 14.9414 75.0684C14.8828 75.4329 14.8535 75.887 14.8535 76.4307ZM20.708 72.7588C21.2679 72.7588 21.6927 72.9541 21.9824 73.3447C22.2721 73.7354 22.417 74.2839 22.417 74.9902C22.417 75.6966 22.2803 76.2484 22.0068 76.6455C21.7334 77.0426 21.3005 77.2412 20.708 77.2412C20.1579 77.2412 19.7396 77.0426 19.4531 76.6455C19.1667 76.2484 19.0234 75.6966 19.0234 74.9902C19.0234 74.2839 19.1569 73.7354 19.4238 73.3447C19.694 72.9541 20.1221 72.7588 20.708 72.7588ZM20.7178 73.7793C20.5583 73.7793 20.4411 73.8802 20.3662 74.082C20.2913 74.2839 20.2539 74.5898 20.2539 75C20.2539 75.4102 20.2913 75.7178 20.3662 75.9229C20.4411 76.1279 20.5583 76.2305 20.7178 76.2305C20.8773 76.2305 20.9945 76.1296 21.0693 75.9277C21.1475 75.7227 21.1865 75.4134 21.1865 75C21.1865 74.5898 21.1475 74.2839 21.0693 74.082C20.9945 73.8802 20.8773 73.7793 20.7178 73.7793ZM25.7764 72.8613L21.8164 80H20.6445L24.6045 72.8613H25.7764ZM25.708 75.6104C26.2679 75.6104 26.6927 75.8057 26.9824 76.1963C27.2721 76.5869 27.417 77.1354 27.417 77.8418C27.417 78.5449 27.2803 79.0951 27.0068 79.4922C26.7334 79.8893 26.3005 80.0879 25.708 80.0879C25.1579 80.0879 24.7396 79.8893 24.4531 79.4922C24.1667 79.0951 24.0234 78.5449 24.0234 77.8418C24.0234 77.1354 24.1569 76.5869 24.4238 76.1963C24.694 75.8057 25.1221 75.6104 25.708 75.6104ZM25.7178 76.6309C25.5583 76.6309 25.4411 76.7318 25.3662 76.9336C25.2913 77.1354 25.2539 77.4414 25.2539 77.8516C25.2539 78.2617 25.2913 78.5693 25.3662 78.7744C25.4411 78.9795 25.5583 79.082 25.7178 79.082C25.8773 79.082 25.9945 78.9811 26.0693 78.7793C26.1475 78.5742 26.1865 78.265 26.1865 77.8516C26.1865 77.4414 26.1475 77.1354 26.0693 76.9336C25.9945 76.7318 25.8773 76.6309 25.7178 76.6309Z" :fill "white"}]
   [:circle {:cx "83.0973" :cy "80.0974" :r "53.8664" :transform "rotate(-47.6483 83.0973 80.0974)" :fill "#F7F7FB"}]
   [:path {:d "M65.3691 74.959H63.1953V73.5879H65.3691C65.7051 73.5879 65.9785 73.5332 66.1895 73.4238C66.4004 73.3105 66.5547 73.1543 66.6523 72.9551C66.75 72.7559 66.7988 72.5312 66.7988 72.2812C66.7988 72.0273 66.75 71.791 66.6523 71.5723C66.5547 71.3535 66.4004 71.1777 66.1895 71.0449C65.9785 70.9121 65.7051 70.8457 65.3691 70.8457H63.8047V78H62.0469V69.4688H65.3691C66.0371 69.4688 66.6094 69.5898 67.0859 69.832C67.5664 70.0703 67.9336 70.4004 68.1875 70.8223C68.4414 71.2441 68.5684 71.7266 68.5684 72.2695C68.5684 72.8203 68.4414 73.2969 68.1875 73.6992C67.9336 74.1016 67.5664 74.4121 67.0859 74.6309C66.6094 74.8496 66.0371 74.959 65.3691 74.959ZM73.0273 76.5703V73.7461C73.0273 73.543 72.9941 73.3691 72.9277 73.2246C72.8613 73.0762 72.7578 72.9609 72.6172 72.8789C72.4805 72.7969 72.3027 72.7559 72.084 72.7559C71.8965 72.7559 71.7344 72.7891 71.5977 72.8555C71.4609 72.918 71.3555 73.0098 71.2812 73.1309C71.207 73.248 71.1699 73.3867 71.1699 73.5469H69.4824C69.4824 73.2773 69.5449 73.0215 69.6699 72.7793C69.7949 72.5371 69.9766 72.3242 70.2148 72.1406C70.4531 71.9531 70.7363 71.8066 71.0645 71.7012C71.3965 71.5957 71.7676 71.543 72.1777 71.543C72.6699 71.543 73.1074 71.625 73.4902 71.7891C73.873 71.9531 74.1738 72.1992 74.3926 72.5273C74.6152 72.8555 74.7266 73.2656 74.7266 73.7578V76.4707C74.7266 76.8184 74.748 77.1035 74.791 77.3262C74.834 77.5449 74.8965 77.7363 74.9785 77.9004V78H73.2734C73.1914 77.8281 73.1289 77.6133 73.0859 77.3555C73.0469 77.0938 73.0273 76.832 73.0273 76.5703ZM73.25 74.1387L73.2617 75.0938H72.3184C72.0957 75.0938 71.9023 75.1191 71.7383 75.1699C71.5742 75.2207 71.4395 75.293 71.334 75.3867C71.2285 75.4766 71.1504 75.582 71.0996 75.7031C71.0527 75.8242 71.0293 75.957 71.0293 76.1016C71.0293 76.2461 71.0625 76.377 71.1289 76.4941C71.1953 76.6074 71.291 76.6973 71.416 76.7637C71.541 76.8262 71.6875 76.8574 71.8555 76.8574C72.1094 76.8574 72.3301 76.8066 72.5176 76.7051C72.7051 76.6035 72.8496 76.4785 72.9512 76.3301C73.0566 76.1816 73.1113 76.041 73.1152 75.9082L73.5605 76.623C73.498 76.7832 73.4121 76.9492 73.3027 77.1211C73.1973 77.293 73.0625 77.4551 72.8984 77.6074C72.7344 77.7559 72.5371 77.8789 72.3066 77.9766C72.0762 78.0703 71.8027 78.1172 71.4863 78.1172C71.084 78.1172 70.7188 78.0371 70.3906 77.877C70.0664 77.7129 69.8086 77.4883 69.6172 77.2031C69.4297 76.9141 69.3359 76.5859 69.3359 76.2188C69.3359 75.8867 69.3984 75.5918 69.5234 75.334C69.6484 75.0762 69.832 74.8594 70.0742 74.6836C70.3203 74.5039 70.627 74.3691 70.9941 74.2793C71.3613 74.1855 71.7871 74.1387 72.2715 74.1387H73.25ZM77.7148 73.043V78H76.0273V71.6602H77.6152L77.7148 73.043ZM79.625 71.6191L79.5957 73.1836C79.5137 73.1719 79.4141 73.1621 79.2969 73.1543C79.1836 73.1426 79.0801 73.1367 78.9863 73.1367C78.748 73.1367 78.541 73.168 78.3652 73.2305C78.1934 73.2891 78.0488 73.377 77.9316 73.4941C77.8184 73.6113 77.7324 73.7539 77.6738 73.9219C77.6191 74.0898 77.5879 74.2812 77.5801 74.4961L77.2402 74.3906C77.2402 73.9805 77.2812 73.6035 77.3633 73.2598C77.4453 72.9121 77.5645 72.6094 77.7207 72.3516C77.8809 72.0938 78.0762 71.8945 78.3066 71.7539C78.5371 71.6133 78.8008 71.543 79.0977 71.543C79.1914 71.543 79.2871 71.5508 79.3848 71.5664C79.4824 71.5781 79.5625 71.5957 79.625 71.6191ZM83.8203 71.6602V72.8555H80.1289V71.6602H83.8203ZM81.043 70.0957H82.7305V76.0898C82.7305 76.2734 82.7539 76.4141 82.8008 76.5117C82.8516 76.6094 82.9258 76.6777 83.0234 76.7168C83.1211 76.752 83.2441 76.7695 83.3926 76.7695C83.498 76.7695 83.5918 76.7656 83.6738 76.7578C83.7598 76.7461 83.832 76.7344 83.8906 76.7227L83.8965 77.9648C83.752 78.0117 83.5957 78.0488 83.4277 78.0762C83.2598 78.1035 83.0742 78.1172 82.8711 78.1172C82.5 78.1172 82.1758 78.0566 81.8984 77.9355C81.625 77.8105 81.4141 77.6113 81.2656 77.3379C81.1172 77.0645 81.043 76.7051 81.043 76.2598V70.0957ZM86.4336 73.0137V78H84.7461V71.6602H86.3281L86.4336 73.0137ZM86.1875 74.6074H85.7305C85.7305 74.1387 85.791 73.7168 85.9121 73.3418C86.0332 72.9629 86.2031 72.6406 86.4219 72.375C86.6406 72.1055 86.9004 71.9004 87.2012 71.7598C87.5059 71.6152 87.8457 71.543 88.2207 71.543C88.5176 71.543 88.7891 71.5859 89.0352 71.6719C89.2812 71.7578 89.4922 71.8945 89.668 72.082C89.8477 72.2695 89.9844 72.5176 90.0781 72.8262C90.1758 73.1348 90.2246 73.5117 90.2246 73.957V78H88.5254V73.9512C88.5254 73.6699 88.4863 73.4512 88.4082 73.2949C88.3301 73.1387 88.2148 73.0293 88.0625 72.9668C87.9141 72.9004 87.7305 72.8672 87.5117 72.8672C87.2852 72.8672 87.0879 72.9121 86.9199 73.002C86.7559 73.0918 86.6191 73.2168 86.5098 73.377C86.4043 73.5332 86.3242 73.7168 86.2695 73.9277C86.2148 74.1387 86.1875 74.3652 86.1875 74.6074ZM94.4258 78.1172C93.9336 78.1172 93.4922 78.0391 93.1016 77.8828C92.7109 77.7227 92.3789 77.502 92.1055 77.2207C91.8359 76.9395 91.6289 76.6133 91.4844 76.2422C91.3398 75.8672 91.2676 75.4688 91.2676 75.0469V74.8125C91.2676 74.332 91.3359 73.8926 91.4727 73.4941C91.6094 73.0957 91.8047 72.75 92.0586 72.457C92.3164 72.1641 92.6289 71.9395 92.9961 71.7832C93.3633 71.623 93.7773 71.543 94.2383 71.543C94.6875 71.543 95.0859 71.6172 95.4336 71.7656C95.7812 71.9141 96.0723 72.125 96.3066 72.3984C96.5449 72.6719 96.7246 73 96.8457 73.3828C96.9668 73.7617 97.0273 74.1836 97.0273 74.6484V75.3516H91.9883V74.2266H95.3691V74.0977C95.3691 73.8633 95.3262 73.6543 95.2402 73.4707C95.1582 73.2832 95.0332 73.1348 94.8652 73.0254C94.6973 72.916 94.4824 72.8613 94.2207 72.8613C93.998 72.8613 93.8066 72.9102 93.6465 73.0078C93.4863 73.1055 93.3555 73.2422 93.2539 73.418C93.1562 73.5938 93.082 73.8008 93.0312 74.0391C92.9844 74.2734 92.9609 74.5312 92.9609 74.8125V75.0469C92.9609 75.3008 92.9961 75.5352 93.0664 75.75C93.1406 75.9648 93.2441 76.1504 93.377 76.3066C93.5137 76.4629 93.6777 76.584 93.8691 76.6699C94.0645 76.7559 94.2852 76.7988 94.5312 76.7988C94.8359 76.7988 95.1191 76.7402 95.3809 76.623C95.6465 76.502 95.875 76.3203 96.0664 76.0781L96.8867 76.9688C96.7539 77.1602 96.5723 77.3438 96.3418 77.5195C96.1152 77.6953 95.8418 77.8398 95.5215 77.9531C95.2012 78.0625 94.8359 78.1172 94.4258 78.1172ZM99.6758 73.043V78H97.9883V71.6602H99.5762L99.6758 73.043ZM101.586 71.6191L101.557 73.1836C101.475 73.1719 101.375 73.1621 101.258 73.1543C101.145 73.1426 101.041 73.1367 100.947 73.1367C100.709 73.1367 100.502 73.168 100.326 73.2305C100.154 73.2891 100.01 73.377 99.8926 73.4941C99.7793 73.6113 99.6934 73.7539 99.6348 73.9219C99.5801 74.0898 99.5488 74.2812 99.541 74.4961L99.2012 74.3906C99.2012 73.9805 99.2422 73.6035 99.3242 73.2598C99.4062 72.9121 99.5254 72.6094 99.6816 72.3516C99.8418 72.0938 100.037 71.8945 100.268 71.7539C100.498 71.6133 100.762 71.543 101.059 71.543C101.152 71.543 101.248 71.5508 101.346 71.5664C101.443 71.5781 101.523 71.5957 101.586 71.6191ZM76.2441 87.5352V87.9395C76.2441 88.5879 76.1562 89.1699 75.9805 89.6855C75.8047 90.2012 75.5566 90.6406 75.2363 91.0039C74.916 91.3633 74.5332 91.6387 74.0879 91.8301C73.6465 92.0215 73.1562 92.1172 72.6172 92.1172C72.082 92.1172 71.5918 92.0215 71.1465 91.8301C70.7051 91.6387 70.3223 91.3633 69.998 91.0039C69.6738 90.6406 69.4219 90.2012 69.2422 89.6855C69.0664 89.1699 68.9785 88.5879 68.9785 87.9395V87.5352C68.9785 86.8828 69.0664 86.3008 69.2422 85.7891C69.418 85.2734 69.666 84.834 69.9863 84.4707C70.3105 84.1074 70.6934 83.8301 71.1348 83.6387C71.5801 83.4473 72.0703 83.3516 72.6055 83.3516C73.1445 83.3516 73.6348 83.4473 74.0762 83.6387C74.5215 83.8301 74.9043 84.1074 75.2246 84.4707C75.5488 84.834 75.7988 85.2734 75.9746 85.7891C76.1543 86.3008 76.2441 86.8828 76.2441 87.5352ZM74.4688 87.9395V87.5234C74.4688 87.0703 74.4277 86.6719 74.3457 86.3281C74.2637 85.9844 74.1426 85.6953 73.9824 85.4609C73.8223 85.2266 73.627 85.0508 73.3965 84.9336C73.166 84.8125 72.9023 84.752 72.6055 84.752C72.3086 84.752 72.0449 84.8125 71.8145 84.9336C71.5879 85.0508 71.3945 85.2266 71.2344 85.4609C71.0781 85.6953 70.959 85.9844 70.877 86.3281C70.7949 86.6719 70.7539 87.0703 70.7539 87.5234V87.9395C70.7539 88.3887 70.7949 88.7871 70.877 89.1348C70.959 89.4785 71.0801 89.7695 71.2402 90.0078C71.4004 90.2422 71.5957 90.4199 71.8262 90.541C72.0566 90.6621 72.3203 90.7227 72.6172 90.7227C72.9141 90.7227 73.1777 90.6621 73.4082 90.541C73.6387 90.4199 73.832 90.2422 73.9883 90.0078C74.1445 89.7695 74.2637 89.4785 74.3457 89.1348C74.4277 88.7871 74.4688 88.3887 74.4688 87.9395ZM79.0918 87.043V92H77.4043V85.6602H78.9922L79.0918 87.043ZM81.002 85.6191L80.9727 87.1836C80.8906 87.1719 80.791 87.1621 80.6738 87.1543C80.5605 87.1426 80.457 87.1367 80.3633 87.1367C80.125 87.1367 79.918 87.168 79.7422 87.2305C79.5703 87.2891 79.4258 87.377 79.3086 87.4941C79.1953 87.6113 79.1094 87.7539 79.0508 87.9219C78.9961 88.0898 78.9648 88.2812 78.957 88.4961L78.6172 88.3906C78.6172 87.9805 78.6582 87.6035 78.7402 87.2598C78.8223 86.9121 78.9414 86.6094 79.0977 86.3516C79.2578 86.0938 79.4531 85.8945 79.6836 85.7539C79.9141 85.6133 80.1777 85.543 80.4746 85.543C80.5684 85.543 80.6641 85.5508 80.7617 85.5664C80.8594 85.5781 80.9395 85.5957 81.002 85.6191ZM85.6953 85.6602H87.2246V91.7891C87.2246 92.3672 87.0957 92.8574 86.8379 93.2598C86.584 93.666 86.2285 93.9727 85.7715 94.1797C85.3145 94.3906 84.7832 94.4961 84.1777 94.4961C83.9121 94.4961 83.6309 94.4609 83.334 94.3906C83.041 94.3203 82.7598 94.2109 82.4902 94.0625C82.2246 93.9141 82.002 93.7266 81.8223 93.5L82.5664 92.5039C82.7617 92.7305 82.9883 92.9062 83.2461 93.0312C83.5039 93.1602 83.7891 93.2246 84.1016 93.2246C84.4062 93.2246 84.6641 93.168 84.875 93.0547C85.0859 92.9453 85.248 92.7832 85.3613 92.5684C85.4746 92.3574 85.5312 92.1016 85.5312 91.8008V87.125L85.6953 85.6602ZM81.4297 88.9062V88.7832C81.4297 88.2988 81.4883 87.8594 81.6055 87.4648C81.7266 87.0664 81.8965 86.7246 82.1152 86.4395C82.3379 86.1543 82.6074 85.9336 82.9238 85.7773C83.2402 85.6211 83.5977 85.543 83.9961 85.543C84.418 85.543 84.7715 85.6211 85.0566 85.7773C85.3418 85.9336 85.5762 86.1562 85.7598 86.4453C85.9434 86.7305 86.0859 87.0684 86.1875 87.459C86.293 87.8457 86.375 88.2695 86.4336 88.7305V89C86.375 89.4414 86.2871 89.8516 86.1699 90.2305C86.0527 90.6094 85.8984 90.9414 85.707 91.2266C85.5156 91.5078 85.2773 91.7266 84.9922 91.8828C84.7109 92.0391 84.375 92.1172 83.9844 92.1172C83.5938 92.1172 83.2402 92.0371 82.9238 91.877C82.6113 91.7168 82.3438 91.4922 82.1211 91.2031C81.8984 90.9141 81.7266 90.5742 81.6055 90.1836C81.4883 89.793 81.4297 89.3672 81.4297 88.9062ZM83.1172 88.7832V88.9062C83.1172 89.168 83.1426 89.4121 83.1934 89.6387C83.2441 89.8652 83.3223 90.0664 83.4277 90.2422C83.5371 90.4141 83.6719 90.5488 83.832 90.6465C83.9961 90.7402 84.1895 90.7871 84.4121 90.7871C84.7207 90.7871 84.9727 90.7227 85.168 90.5938C85.3633 90.4609 85.5098 90.2793 85.6074 90.0488C85.7051 89.8184 85.7617 89.5527 85.7773 89.252V88.4844C85.7695 88.2383 85.7363 88.0176 85.6777 87.8223C85.6191 87.623 85.5371 87.4531 85.4316 87.3125C85.3262 87.1719 85.1895 87.0625 85.0215 86.9844C84.8535 86.9062 84.6543 86.8672 84.4238 86.8672C84.2012 86.8672 84.0078 86.918 83.8438 87.0195C83.6836 87.1172 83.5488 87.252 83.4395 87.4238C83.334 87.5957 83.2539 87.7988 83.1992 88.0332C83.1445 88.2637 83.1172 88.5137 83.1172 88.7832ZM91.9648 90.248C91.9648 90.127 91.9297 90.0176 91.8594 89.9199C91.7891 89.8223 91.6582 89.7324 91.4668 89.6504C91.2793 89.5645 91.0078 89.4863 90.6523 89.416C90.332 89.3457 90.0332 89.2578 89.7559 89.1523C89.4824 89.043 89.2441 88.9121 89.041 88.7598C88.8418 88.6074 88.6855 88.4277 88.5723 88.2207C88.459 88.0098 88.4023 87.7695 88.4023 87.5C88.4023 87.2344 88.459 86.9844 88.5723 86.75C88.6895 86.5156 88.8555 86.3086 89.0703 86.1289C89.2891 85.9453 89.5547 85.8027 89.8672 85.7012C90.1836 85.5957 90.5391 85.543 90.9336 85.543C91.4844 85.543 91.957 85.6309 92.3516 85.8066C92.75 85.9824 93.0547 86.2246 93.2656 86.5332C93.4805 86.8379 93.5879 87.1855 93.5879 87.5762H91.9004C91.9004 87.4121 91.8652 87.2656 91.7949 87.1367C91.7285 87.0039 91.623 86.9004 91.4785 86.8262C91.3379 86.748 91.1543 86.709 90.9277 86.709C90.7402 86.709 90.5781 86.7422 90.4414 86.8086C90.3047 86.8711 90.1992 86.957 90.125 87.0664C90.0547 87.1719 90.0195 87.2891 90.0195 87.418C90.0195 87.5156 90.0391 87.6035 90.0781 87.6816C90.1211 87.7559 90.1895 87.8242 90.2832 87.8867C90.377 87.9492 90.498 88.0078 90.6465 88.0625C90.7988 88.1133 90.9863 88.1602 91.209 88.2031C91.666 88.2969 92.0742 88.4199 92.4336 88.5723C92.793 88.7207 93.0781 88.9238 93.2891 89.1816C93.5 89.4355 93.6055 89.7695 93.6055 90.1836C93.6055 90.4648 93.543 90.7227 93.418 90.957C93.293 91.1914 93.1133 91.3965 92.8789 91.5723C92.6445 91.7441 92.3633 91.8789 92.0352 91.9766C91.7109 92.0703 91.3457 92.1172 90.9395 92.1172C90.3496 92.1172 89.8496 92.0117 89.4395 91.8008C89.0332 91.5898 88.7246 91.3223 88.5137 90.998C88.3066 90.6699 88.2031 90.334 88.2031 89.9902H89.8027C89.8105 90.2207 89.8691 90.4062 89.9785 90.5469C90.0918 90.6875 90.2344 90.7891 90.4062 90.8516C90.582 90.9141 90.7715 90.9453 90.9746 90.9453C91.1934 90.9453 91.375 90.916 91.5195 90.8574C91.6641 90.7949 91.7734 90.7129 91.8477 90.6113C91.9258 90.5059 91.9648 90.3848 91.9648 90.248ZM94.7832 91.1855C94.7832 90.9355 94.8691 90.7266 95.041 90.5586C95.2168 90.3906 95.4492 90.3066 95.7383 90.3066C96.0273 90.3066 96.2578 90.3906 96.4297 90.5586C96.6055 90.7266 96.6934 90.9355 96.6934 91.1855C96.6934 91.4355 96.6055 91.6445 96.4297 91.8125C96.2578 91.9805 96.0273 92.0645 95.7383 92.0645C95.4492 92.0645 95.2168 91.9805 95.041 91.8125C94.8691 91.6445 94.7832 91.4355 94.7832 91.1855Z" :fill "#3D465B"}]])
