## Pre-Run

1. Create `profiles.clj` file:

    <details>
      <summary>Example</summary>
      
      ```
      {:provided {:env {:database-url "jdbc:postgresql://localhost/webchange?user=webchange&password=webchange"
                        :public-dir   "resources/public"
                        :upload-dir   "resources/public/upload"}}
       :test     {:env {:database-url "jdbc:postgresql://localhost/webchange_test?user=webchange&password=webchange"
                        :public-dir   "resources/public"
                        :upload-dir   "resources/public/upload"}}}
      ```
    </details>

1. Prepare DB

    2.1 Apply migrations:

    ```
    $ lein migratus migrate
    ```
    
    2.2. Load datasets:
    
    ```
    $ lein run load-dataset-force test concepts
    $ lein run load-dataset-force english concepts
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
