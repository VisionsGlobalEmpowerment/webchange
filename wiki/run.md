## Pre-Run

1. Create `profiles.clj` file:

    <details>
      <summary>Example</summary>
      
      ```
      {:provided {:env {:database-url "jdbc:postgresql://localhost/webchange?user=webchange&password=webchange"
                        :public-dir   "resources/public"
                        :upload-dir   "resources/public/upload"
                        :hardcoded-courses {"english" true}         ;; which course take from hardcoded edn
                        :dev?              true                     ;; is dev environment? 
                                                                    ;; affects on the way how resources list for sw are taken
       }}
       :test     {:env {:database-url "jdbc:postgresql://localhost/webchange_test?user=webchange&password=webchange"
                        :public-dir   "resources/public"
                        :upload-dir   "resources/public/upload"}}}
      ```
    </details>

1. Prepare DB

    2.1 Apply migrations:

    ```
    $ lein migratus migrate
    $ lein with-profile test migratus migrate
    ```
    
    2.2. Load datasets:
    
    ```
    $ lein run load-dataset-force spanish concepts
    $ lein run load-dataset-force english concepts
    $ lein run update-character-skins
    $ lein run update-editor-assets
    ```
   
1. Download raw data from google disk store to `resources/public/raw/`
   
1. Clean:

    ```
    $ lein clean
    ```
   
1. Build styles:

    ```
    $ lein sass once
    ```

1. Build service worker:

    ```
    $ lein cljsbuild once sw
    ```
   
# Run

- Run back:

    ```
    $ lein repl
    
        webchange.server=> (dev)
    ```

- Run front:

    ```
    $ lein figwheel dev
    ```
- Wait a bit, then browse to [`localhost:3000`](localhost:3000).
Figwheel will automatically push cljs changes to the browser.

- Enable voice recognition:
    - Download docker image:

        ```
        $ docker pull webchange/webchange-voice-recognition:latest
        ```
    - Run it:
       ```
       $ docker run  webchange/webchange-voice-recognition:latest
       ```
     - Check status:
        ```
         $ docker containter ps
        ```
     - Stop container:
         ```
          $ docker stop <container-id>
         ```          
