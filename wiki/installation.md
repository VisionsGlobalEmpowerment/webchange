# Installation

- Update your packages index:

```
$ sudo apt-get update
```

- Install git:

```
$ sudo apt install git
```

- Install git-lfs:

```
$ curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash
$ sudo apt-get install git-lfs
$ git lfs install
```

- Get sources:
  - Set up ssh key according to [github instructions](https://help.github.com/en/articles/connecting-to-github-with-ssh)
  - Clone project locally:

  ```
  $ git clone git@github.com:VisionsGlobalEmpowerment/webchange.git
  ```

- Install npm:

```
$ curl -sL https://deb.nodesource.com/setup_10.x | sudo bash -
$ sudo apt install nodejs
```


- Install ffmpeg for audio files convert:

```
$ sudo apt-get install ffmpeg
```

- Install Karma

```
$ sudo npm install -g karma-cli
```

- Install Sass

```
$ sudo apt install ruby-sass
```

- Install npm packages:

```
$ npm install
```

- Install PostgreSQL:

```
$ sudo apt install postgresql postgresql-contrib
```

- Initialize database:

```
$ sudo -u postgres -i

postgres@user$ createuser --interactive -P // create user 'webchange'
               createdb --owner=webchange webchange
               exit
              
$ psql webchange < dump.sql
```

- Install Java:

```
$ sudo apt-get install oracle-java8-installer
```

- Install Clojure:

```
$ curl -O https://download.clojure.org/install/linux-install-1.10.0.442.sh
$ chmod +x linux-install-1.10.0.442.sh
$ sudo ./linux-install-1.10.0.442.sh
```

- Install Leiningen

```
$ sudo apt-get install leiningen
```

---

### Links

- [git](https://git-scm.com/)
- [npm](https://www.npmjs.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [Java](https://java.com)
- [Clojure](https://clojure.org)
- [Leiningen](https://leiningen.org)
  - [lein-sass](https://github.com/tuhlmann/lein-sass)
  - [lein-cljsbuild](https://github.com/emezeske/lein-cljsbuild)
  - [lein-environ](https://github.com/weavejester/environ)
  - [migratus-lein](https://github.com/yogthos/migratus-lein)
